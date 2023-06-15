package com.example.cookmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cookmate.adapter.RecipeAdapter
import com.example.cookmate.databinding.ActivityExploreBinding
import com.example.cookmate.viewmodel.RecipeViewModel

class ExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExploreBinding
    private val adapter by lazy {
        RecipeAdapter{
            Intent(this, DetailActivity::class.java).apply {
                putExtra("item", it)
                startActivity(this)
            }
        }
    }

    private val exploreViewModel by viewModels<RecipeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                exploreViewModel.searchRecipe(query.toString())
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean = false
        })

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

        exploreViewModel.resultSucces.observe(this){
            adapter.setData(it)
        }

        exploreViewModel.resultLoading.observe(this){
            binding.progressBar.isVisible = it
        }

        exploreViewModel.getRecipe()

//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                exploreViewModel.getUser(query.toString())
//                return true
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean = false
//
//        })
    }
}