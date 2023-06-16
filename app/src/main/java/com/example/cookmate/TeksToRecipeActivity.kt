package com.example.cookmate

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookmate.adapter.RecipeAdapter
import com.example.cookmate.api.ApiConfig
import com.example.cookmate.databinding.ActivityTeksToRecipeBinding
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class TeksToRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeksToRecipeBinding

    private val adapter by lazy {
        RecipeAdapter{
            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", it)
                startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeksToRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar1.isVisible = false
        binding.view2.isVisible = false

        binding.rvTextRecipe.layoutManager = GridLayoutManager(this, 2)
        binding.rvTextRecipe.setHasFixedSize(true)
        binding.rvTextRecipe.adapter = adapter

        binding.btnText.setOnClickListener{
            val query = binding.etBahan.text.toString().trim()

            if (query.isEmpty()) {
                binding.etBahan.error = "Bahan kamu masih kosong"
                binding.etBahan.requestFocus()
                return@setOnClickListener
            }

            GlobalScope.launch(Dispatchers.IO){
                launch(Dispatchers.Main){
                    try {
                        val bahan = binding.etBahan.text.toString().trim()

                        val jsonObject = JSONObject()
                        jsonObject.put("ingredients", bahan)

                        val requestBody =
                            jsonObject.toString().toRequestBody("application/json".toMediaType())

                        val response = ApiConfig.getApiService().uploadText(requestBody)
                        launch(Dispatchers.Main) {
                            adapter.setData(response.data)
                            binding.progressBar1.isVisible = false
                            binding.tvCocok.text = "Berikut resep yg dapat kamu buat"
                            binding.view2.isVisible = true
                        }
                    } catch (e: Exception) {
                        launch(Dispatchers.Main) {
                            Toast.makeText(this@TeksToRecipeActivity, "Error", Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBar1.isVisible = false
                        }
                    }
                }
            }
        }

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

}