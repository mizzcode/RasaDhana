package com.rasadhana.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.rasadhana.R
import com.rasadhana.data.Result
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.data.local.room.RecipeDao
import com.rasadhana.data.remote.retrofit.ApiService
import com.rasadhana.data.remote.retrofit.MlApiService
import retrofit2.HttpException

class RecipeRepository(
    private val apiService: ApiService,
    private val recipeDao: RecipeDao,
    private val mlApiService: MlApiService,
) {

    fun searchRecipes(query: String): LiveData<List<RecipeEntity>>  {
        return recipeDao.searchRecipes("%$query%")
    }

    fun getAllRecipe(): LiveData<Result<List<RecipeEntity>>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.getAllRecipe()
            Log.d("RecipeRepository", "Fetched recipes from API: ${response.recipes}")

            val recipes = response.recipes.map { recipe ->
                RecipeEntity(
                    name = recipe.title,
                    image = recipe.recipeImage,
                    ingredients = recipe.ingredients,
                    howToMake = recipe.steps,
                    isFavorite = false,
                )
            }
            recipeDao.insertRecipes(recipes)

        } catch (e: HttpException) {
            val errorMessage = e.message ?: "Oops! Something went wrong. Please try again later."
            Log.e("RecipeRepository", "HttpException: $errorMessage")
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            val errorMessage = "Please check your connection and try again."
            Log.e("RecipeRepository", "Exception: $errorMessage", e)
            emit(Result.Error(errorMessage))
        }

        val localData: LiveData<Result<List<RecipeEntity>>> = recipeDao.getAllRecipe().map { Result.Success(it) }
        emitSource(localData)
    }


    fun generateRecipe(userId: String, context: Context): LiveData<Result<List<RecipeEntity>>> = liveData {
        emit(Result.Loading)

        try {
            val response = mlApiService.generateRecipes(userId)

            val recipes = response.recipes

            val data = recipes.map { recipe ->

                RecipeEntity(
                    name = recipe.title,
                    image = "android.resource://${context.packageName}/${R.drawable.image_wings}",
                    ingredients = recipe.ingredients,
                    howToMake = recipe.steps,
                    isFavorite = false,
                    isFromRecommendation = true
                )
            }

            recipeDao.insertRecipes(data)

            emit(Result.Success(data))
        } catch (e: HttpException) {
            val errorMessage = "Oops! Something went wrong. Please try again later."
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error("Please check your connection and try again."))
        }
    }

    fun getHistoryRecommendationRecipes(): LiveData<Result<List<RecipeEntity>>> = liveData {
        emit(Result.Loading)

        try {
            val localData: LiveData<Result<List<RecipeEntity>>> = recipeDao.getHistoryRecommendationRecipes().map { Result.Success(it) }
            emitSource(localData)
        } catch (e: Exception) {
            val errorMessage = "Failed to fetch data: ${e.message}"
            emit(Result.Error(errorMessage))
        }
    }


    fun getRecipesFavorite(): LiveData<List<RecipeEntity>> {
        return recipeDao.getRecipesFavorite()
    }

    suspend fun setRecipeFavorite(recipe: RecipeEntity, favoriteState: Boolean) {
        val existingRecipe = recipeDao.getRecipeByTitle(recipe.name)

        if (existingRecipe != null) {
            existingRecipe.isFavorite = favoriteState
            recipeDao.updateRecipe(existingRecipe)
        } else {
            recipe.isFavorite = favoriteState
            recipeDao.upsertRecipe(recipe)
        }
    }
}