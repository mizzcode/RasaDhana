package com.rasadhana.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserDataResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("expireToken")
	val expireToken: String
)

data class Data(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("photoUrl")
	val photoUrl: String
)
