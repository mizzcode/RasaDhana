package com.rasadhana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.rasadhana.data.Result
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.data.local.room.RecipeDao
import com.rasadhana.data.remote.response.FileUploadResponse
import com.rasadhana.data.remote.response.HomeResponse
import com.rasadhana.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RecipeRepository(private val apiService: ApiService, private val recipeDao: RecipeDao) {

    fun searchRecipes(query: String): LiveData<List<RecipeEntity>>  {
        return recipeDao.searchRecipes("%$query%")
    }

    fun getAllRecipe(): LiveData<Result<List<RecipeEntity>>> = liveData {
        emit(Result.Loading)

        val localDataRecipes = recipeDao.getAllRecipe()

        if (localDataRecipes.isNotEmpty()) {
            Log.d("RecipeRepository", "getAllRecipe: $localDataRecipes")
            emit(Result.Success(localDataRecipes))
            return@liveData
        }

        try {
            val response = apiService.getAllRecipe()

            Log.d("response", response.toString())

            val recipes = response.recipes

            withContext(Dispatchers.IO) {
                val data = recipes.map { recipe ->
                    RecipeEntity(
                        name = recipe.title,
                        image = recipe.recipeImage,
                        ingredients = recipe.ingredients,
                        howToMake = recipe.steps,
                    )
                }
                recipeDao.insertRecipes(data)
            }
        } catch (e: HttpException) {
            val errorMessage = "Oops! Something went wrong. Please try again later."
            emit(Result.Error(errorMessage))
        } catch (e: Exception) {
            emit(Result.Error("Unable to complete the request. Please check your connection and try again."))
        }

        val localData = recipeDao.getAllRecipe()
        emit(Result.Success(localData))
    }
}