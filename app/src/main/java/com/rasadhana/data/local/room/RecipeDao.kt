package com.rasadhana.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rasadhana.data.local.entity.RecipeEntity

@Dao
interface RecipeDao {
    @Query("SELECT * FROM Recipe WHERE isFromRecommendation = 0")
    fun getAllRecipe(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM Recipe WHERE isFromRecommendation = 1")
    fun getHistoryRecommendationRecipes(): LiveData<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("SELECT * FROM Recipe WHERE name LIKE :query")
    fun searchRecipes(query: String): LiveData<List<RecipeEntity>>

    @Query("SELECT EXISTS(SELECT * FROM recipe WHERE name = :title AND isFavorite = 1)")
    suspend fun isRecipeFavorite(title: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecipe(recipe: RecipeEntity)

    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM Recipe where isFavorite = 1")
    fun getRecipesFavorite(): LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM Recipe WHERE name = :title LIMIT 1")
    suspend fun getRecipeByTitle(title: String): RecipeEntity?
}