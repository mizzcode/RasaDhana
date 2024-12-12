package com.rasadhana.ui.recommendation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.data.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecommendationViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    fun saveRecipeToFavorite(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeRepository.setRecipeFavorite(recipe, true)
        }
    }

    fun deleteRecipeFromFavorite(recipe: RecipeEntity) {
        viewModelScope.launch {
            recipeRepository.setRecipeFavorite(recipe, false)
        }
    }
}