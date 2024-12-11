package com.rasadhana.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Recipe")
class RecipeEntity (
    @PrimaryKey
    val name: String,

    val image: String,
    val howToMake: String,
    val ingredients: String,
    var isFavorite: Boolean = false,
    var isRecommendation: Boolean = false
) : Parcelable