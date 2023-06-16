package com.example.cookmate

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookmate.adapter.RecipeAdapter
import com.example.cookmate.api.ApiConfig
import com.example.cookmate.databinding.ActivityPhotoToRecipeBinding
import com.example.cookmate.response.DataPhotoItem
import com.example.cookmate.utilities.rotateFile
import com.example.cookmate.utilities.uriToFile
import com.example.cookmate.viewmodel.ResultPhotoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PhotoToRecipeActivity : AppCompatActivity() {

    private var getFile: File? = null

    private lateinit var binding: ActivityPhotoToRecipeBinding

    private val resultPhotoViewModel by viewModels<ResultPhotoViewModel>()

    private val adapter by lazy {
        RecipeAdapter{
            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", it)
                startActivity(this)
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoToRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.progressBar3.isVisible = false

        binding.view3.isVisible = false

        binding.ivTakePhoto.setOnClickListener {
            startCameraX()
        }
        binding.ivGalery.setOnClickListener{
            startGallery()
        }
        binding.btnUpload.setOnClickListener{
            uploadImage()
        }
        binding.rvRecipePhoto.layoutManager = GridLayoutManager(this, 2)
        binding.rvRecipePhoto.setHasFixedSize(true)
        binding.rvRecipePhoto.adapter = adapter

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeVec -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }
                R.id.searchVec -> {
                    val intent = Intent(this, ExploreActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }
                R.id.addVec -> {
                    val intent = Intent(this, TeksOrPhotoActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    true
                }

                else -> super.onOptionsItemSelected(it)
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraxActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                getFile = myFile
                binding.ivPreviewPhoto.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this)
                getFile = myFile
                binding.ivPreviewPhoto.setImageURI(uri)
            }
        }
    }


    private fun uploadImage() {
        if (getFile != null){
            val file = getFile as File

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestImageFile
            )
            GlobalScope.launch (Dispatchers.IO){
                launch(Dispatchers.Main){
                    flow {
                        val response = ApiConfig
                            .getApiService()
                            .uploadPhoto(imageMultipart)
                        emit(response)
                    }.onStart {
                        binding.progressBar3.isVisible = true
                    }.onCompletion {
                        binding.progressBar3.isVisible = false
                    }.catch {
                        Toast.makeText(this@PhotoToRecipeActivity, "Error", Toast.LENGTH_SHORT).show()
                    }.collect{
                        Toast.makeText(this@PhotoToRecipeActivity, "Berhasil Upload", Toast.LENGTH_SHORT).show()
                        adapter.setData(it.data)
                        binding.tvCocok.text = "Berikut resep yang dapat kamu buat"
                        binding.view3.isVisible = true
//                        resultSucces.observe(this@PhotoToRecipeActivity){
//                            adapter.setData(it)
//                        }
                    }
                }
            }
        }else{
            Toast.makeText(this, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }
}