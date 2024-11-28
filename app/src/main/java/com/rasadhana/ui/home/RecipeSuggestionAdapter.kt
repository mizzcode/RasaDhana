package com.rasadhana.ui.home

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rasadhana.GlideApp
import com.rasadhana.R
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.databinding.ItemColRecipeMaybeYouLikeItBinding

class RecipeSuggestionAdapter : ListAdapter<RecipeEntity, RecipeSuggestionAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemColRecipeMaybeYouLikeItBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    class MyViewHolder(
        private val binding: ItemColRecipeMaybeYouLikeItBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeEntity) {
            Log.d("RecipeSuggestionAdapter", "Binding recipe: ${recipe.name} | Gambar: ${recipe.image}")
            binding.tvNameRecipe.text = recipe.name
            GlideApp.with(itemView.context)
                .load(recipe.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Menyimpan gambar di disk cache
                .error(R.drawable.ic_place_holder)
                .into(binding.ivRecipe)

//            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, DetailActivity::class.java)
//                intent.putExtra(EXTRA_ID, story.id)
//                itemView.context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
//            }
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