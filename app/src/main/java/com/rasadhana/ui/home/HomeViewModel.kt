package com.rasadhana.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.data.pref.UserModel
import com.rasadhana.data.repository.RecipeRepository
import com.rasadhana.data.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository, private val recipeRepository: RecipeRepository) : ViewModel() {
    private val _recipes = MediatorLiveData<List<RecipeEntity>>()
    val recipes: LiveData<List<RecipeEntity>> get() = _recipes

    fun searchRecipes(query: String) {
        val source = recipeRepository.searchRecipes(query)
        _recipes.addSource(source) { result ->
            _recipes.value = result
            _recipes.removeSource(source)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun getAllRecipe() = recipeRepository.getAllRecipe()

    fun getHistoryRecommendationRecipes() = recipeRepository.getHistoryRecommendationRecipes()

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