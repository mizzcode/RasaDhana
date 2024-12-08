package com.rasadhana.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.databinding.ItemRowRecipeSearchResultBinding
import com.rasadhana.ui.detail.DetailActivity

class RecipeSearchResultAdapter : ListAdapter<RecipeEntity, RecipeSearchResultAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowRecipeSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    class MyViewHolder(
        private val binding: ItemRowRecipeSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeEntity) {
            Log.d("RecipeSearchResult", "Binding recipe: ${recipe.name} | Gambar: ${recipe.image}")
            binding.tvNameRecipe.text = recipe.name

            itemView.setOnClickListener {
                Log.d("RecipeSearchResult", "Item clicked: ${recipe.name}")
                val context = itemView.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_RECIPE, recipe)
                }
                // Safe casting context to Activity
                if (context is androidx.appcompat.app.AppCompatActivity) {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context)
                    context.startActivity(intent, options.toBundle())
                } else {
                    context.startActivity(intent) // Fallback jika bukan Activity
                }
            }

        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipeEntity>() {
            override fun areItemsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}