package com.rasadhana.ui.detail

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rasadhana.R
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recipe = intent.getParcelableExtra<RecipeEntity>(EXTRA_RECIPE)

        Log.d("DetailActivity", "Title: ${recipe?.name}, Image: ${recipe?.image}")

        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@DetailActivity, R.color.blue)))
            setDisplayHomeAsUpEnabled(true)
            title = "Resep ${recipe?.name}"

        }

        val titleTextView = findViewById<TextView>(android.R.id.title)
        titleTextView?.setTextColor(ContextCompat.getColor(this@DetailActivity, android.R.color.white))

        Glide.with(this)
            .load(recipe?.image)
            .placeholder(R.drawable.ic_place_holder)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Menyimpan gambar di disk cache
            .error(R.drawable.ic_place_holder)
            .into(binding.ivRecipe)

        binding.tvIngredient.text = "Bahan-Bahan :\n\n${recipe?.ingredients}"
        binding.tvHowToMake.text = "Cara Membuat :\n\n${recipe?.howToMake}"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_RECIPE = "extra_recipe"
    }
}