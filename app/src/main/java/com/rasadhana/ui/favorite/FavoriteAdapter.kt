package com.rasadhana.ui.favorite

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rasadhana.R
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.databinding.ItemRowFavoriteRecipesBinding
import com.rasadhana.ui.detail.DetailActivity
import com.rasadhana.ui.detail.DetailActivity.Companion.EXTRA_RECIPE

class FavoriteAdapter(private val onFabClicked: (RecipeEntity) -> Unit) : ListAdapter<RecipeEntity, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowFavoriteRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe, onFabClicked)
    }

    class MyViewHolder(
        private val binding: ItemRowFavoriteRecipesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeEntity, onFabClicked: (RecipeEntity) -> Unit) {
            Log.d("FavoriteAdapter", "Binding recipe: ${recipe.name} | Gambar: ${recipe.image}")
            binding.tvNameRecipe.text = recipe.name
            Glide.with(itemView.context)
                .load(recipe.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Menyimpan gambar di disk cache
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .into(binding.ivRecipe)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_RECIPE, recipe)
                itemView.context.startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity
                    ).toBundle())
            }

            binding.floatingActionButton.setImageResource(if (recipe.isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24)

            binding.floatingActionButton.setOnClickListener {
                binding.floatingActionButton.setImageResource(if (recipe.isFavorite) R.drawable.baseline_favorite_border_24 else R.drawable.baseline_favorite_24)

                onFabClicked(recipe)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipeEntity>() {
            override fun areItemsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
                return oldItem.name == newItem.name
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}