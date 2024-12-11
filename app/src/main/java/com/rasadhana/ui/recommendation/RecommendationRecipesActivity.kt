package com.rasadhana.ui.recommendation

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsetsController
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.rasadhana.R
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.databinding.ActivityRecommendationRecipesBinding

class RecommendationRecipesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendationRecipesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecommendationRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 12 and above, use WindowInsetsController
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
            window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        } else {
            // For Android below Android 12, use the legacy method
            window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        }

        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@RecommendationRecipesActivity, R.color.blue)))
            setDisplayHomeAsUpEnabled(true)
            title = "Rekomendasi Resep"
        }

        val titleTextView = findViewById<TextView>(android.R.id.title)
        titleTextView?.setTextColor(ContextCompat.getColor(this@RecommendationRecipesActivity, android.R.color.white))

        val recipes: ArrayList<RecipeEntity>? = intent.getParcelableArrayListExtra(EXTRA_RECIPES)

        if (recipes != null) {
            val adapter = RecommendationRecipesAdapter()
            adapter.submitList(recipes)

            binding.rvRecommendationRecipes.apply {
                layoutManager = GridLayoutManager(context, 2)
                setHasFixedSize(true)
                this.adapter = adapter
            }
        } else {
            Log.d("RecommendationRecipes", "No recipes received from intent.")
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_RECIPES = "extra_recipes"
    }
}