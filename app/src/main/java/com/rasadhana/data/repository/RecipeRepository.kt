package com.rasadhana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.rasadhana.data.Result
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.data.local.room.RecipeDao
import com.rasadhana.data.remote.retrofit.DummyApiService

class RecipeRepository(private val dummyApiService: DummyApiService, private val recipeDao: RecipeDao) {

    fun searchRecipes(query: String): LiveData<List<RecipeEntity>>  {
        return recipeDao.searchRecipes("%$query%")
    }

    fun getDummyRecipes(): LiveData<Result<List<RecipeEntity>>> = liveData {
        emit(Result.Loading)

        val localDataRecipes = recipeDao.getDummyRecipes()

        if (localDataRecipes.isNotEmpty()) {
            Log.d("RecipeRepository", "getDummyRecipe1: $localDataRecipes")
            emit(Result.Success(localDataRecipes))
            return@liveData
        }

        try {
            val response = dummyApiService.getDummyRecipes()

            Log.d("response", response.toString())

            val data = response.map { recipe ->
                RecipeEntity(
                    name = recipe.name,
                    image = recipe.image,
                    ingredients = recipe.ingredients,
                    howToMake = recipe.howToMake,
                    isFavorite = recipe.isFavorite == "1",
                    dummy = recipe.dummy == "1"
                )
            }
            recipeDao.insertRecipes(data)
        } catch (e: Exception) {
            Log.d("RecipeRepository", "getDummyRecipe: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }

        val localData = recipeDao.getDummyRecipes()
        emit(Result.Success(localData))
    }
}