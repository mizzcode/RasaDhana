package com.rasadhana.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserDataResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean
)

data class Data(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("__v")
	val v: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("resetToken")
	val resetToken: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
