package com.rasadhana.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.rasadhana.data.Result
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.data.local.room.RecipeDao
import com.rasadhana.data.remote.retrofit.DummyApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(private val dummyApiService: DummyApiService, private val recipeDao: RecipeDao) {

    fun getDummyRecipes(): LiveData<Result<List<RecipeEntity>>> = liveData {
        emit(Result.Loading)

        try {
            val response = dummyApiService.getDummyRecipes()

            Log.d("response", response.toString())

            withContext(Dispatchers.IO) {
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
            }
        } catch (e: Exception) {
            Log.d("RecipeRepository", "getDummyRecipe: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }

        val localData: LiveData<Result<List<RecipeEntity>>> = recipeDao.getDummyRecipes().map { Result.Success(it) }
        emitSource(localData)
    }
}