package com.rasadhana.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: User
)

data class User(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("email")
	val email: String
)
