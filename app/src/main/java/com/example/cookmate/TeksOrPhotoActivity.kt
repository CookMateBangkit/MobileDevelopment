package com.example.cookmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookmate.databinding.ActivityPhotoToRecipeBinding
import com.example.cookmate.databinding.ActivityTeksOrPhotoBinding

class TeksOrPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeksOrPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeksOrPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPhoto.setOnClickListener{
            val intent = Intent(this, PhotoToRecipeActivity::class.java)
            startActivity(intent)
        }

        binding.btnTeks.setOnClickListener{
            val intent = Intent(this, TeksToRecipeActivity::class.java)
            startActivity(intent)
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