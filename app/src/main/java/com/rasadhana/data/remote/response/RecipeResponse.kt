package com.rasadhana.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecipeResponseItem(

	@field:SerializedName("dummy")
	val dummy: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("howToMake")
	val howToMake: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("ingredients")
	val ingredients: String,

	@field:SerializedName("isFavorite")
	val isFavorite: String
)