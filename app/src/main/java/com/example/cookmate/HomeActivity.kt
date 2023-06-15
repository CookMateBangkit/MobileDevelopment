package com.example.cookmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cookmate.adapter.RecipeAdapter
import com.example.cookmate.databinding.ActivityHomeBinding
import com.example.cookmate.viewmodel.RecipeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val adapter by lazy {
        RecipeAdapter{
            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", it)
                startActivity(this)
            }
        }
    }

    private val homeViewModel by viewModels<RecipeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.rvRecipe.layoutManager = GridLayoutManager(this, 2)
        binding.rvRecipe.isNestedScrollingEnabled()
        binding.rvRecipe.setHasFixedSize(true)
        binding.rvRecipe.adapter = adapter

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

        homeViewModel.resultSucces.observe(this){
            adapter.setData(it)
        }

        homeViewModel.resultLoading.observe(this){
            binding.progressBar2.isVisible = it
        }

        homeViewModel.getRecipe()

    }

}