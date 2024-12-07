package com.rasadhana.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rasadhana.data.local.entity.RecipeEntity

@Dao
interface RecipeDao {
    @Query("SELECT * FROM Recipe")
    suspend fun getAllRecipe(): List<RecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("SELECT * FROM Recipe WHERE name LIKE :query")
    fun searchRecipes(query: String): LiveData<List<RecipeEntity>>
}