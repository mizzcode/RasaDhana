package com.rasadhana.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.data.repository.RecipeRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {
    fun getRecipesFavorite() = recipeRepository.getRecipesFavorite()

    fun deleteRecipeFromFavorite(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeRepository.setRecipeFavorite(recipe, false)
        }
    }
}