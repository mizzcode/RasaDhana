package com.rasadhana.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rasadhana.data.local.entity.RecipeEntity
import com.rasadhana.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, RecipeEntity::class], version = 8, exportSchema = false)
abstract class RasadhanaDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun recipeDao(): RecipeDao
}