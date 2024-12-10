package com.rasadhana.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecipeResponse(

	@field:SerializedName("recipes")
	val recipes: List<RecipeDetails> = emptyList(),

	@field:SerializedName("confidence")
	val confidence: String,

	@field:SerializedName("class")
	val className: String,

//	@field:SerializedName("success")
//	val success: Boolean
)

data class RecipeDetails(

	@field:SerializedName("Steps")
	val steps: String,

	@field:SerializedName("Ingredients")
	val ingredients: String,

	@field:SerializedName("Title")
	val title: String
)