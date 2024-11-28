package com.rasadhana.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.databinding.ItemColRecipeHistoryBinding

class RecipeHistoryAdapter : ListAdapter<RecipeEntity, RecipeHistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemColRecipeHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    class MyViewHolder(
        private val binding: ItemColRecipeHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: RecipeEntity) {
            binding.tvNameRecipeHistory.text = recipe.name
            Glide.with(itemView.context)
                .load(recipe.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Menyimpan gambar di disk cache
                .into(binding.ivRecipeHistory)

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