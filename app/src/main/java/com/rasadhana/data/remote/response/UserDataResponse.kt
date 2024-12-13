package com.rasadhana.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserDataResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("expireToken")
	val expireToken: String
)

data class Data(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("resetToken")
	val resetToken: String? = null,

	@field:SerializedName("email")
	val email: String,
	
)