package com.rasadhana.data.remote.response

import com.google.gson.annotations.SerializedName

data class HomeResponse(
	val recipes: List<RecipesItem> = emptyList(),
	val success: Boolean
)

data class RecipesItem(
	val recipeImage: String,
	val ingredients: String,

	@field:SerializedName("_id")
	val id: String,

	val title: String,
	val steps: String
)

