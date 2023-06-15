package com.example.cookmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import coil.load
import com.example.cookmate.databinding.ActivityDetailBinding
import com.example.cookmate.response.DataItem
import com.example.cookmate.viewmodel.DetailRecipeViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private val detailViewModel by viewModels<DetailRecipeViewModel> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getParcelableExtra<DataItem>("item")
        val id = item?.id ?: ""


        detailViewModel.resultSucces.observe(this){
            val bahanHtml = it.ingredients
            val bahan = HtmlCompat.fromHtml(bahanHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)

            val membuatHtml = it.steps
            val membuat = HtmlCompat.fromHtml(membuatHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)

            binding.ivResep.load(it.picsUrl)
            binding.tvResep.text = it.name
            binding.tvBahan.text = bahan
            binding.tvMembuat.text = membuat

            binding.btnBack.setOnClickListener{
                finish()
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

        detailViewModel.getDetailStory(id)

    }
}