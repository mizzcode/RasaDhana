package com.rasadhana.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipe")
class RecipeEntity (
    @PrimaryKey
    val name: String,

    val image: String,
    val howToMake: String,
    val ingredients: String,
    val isFavorite: Boolean,
    val dummy: Boolean
)