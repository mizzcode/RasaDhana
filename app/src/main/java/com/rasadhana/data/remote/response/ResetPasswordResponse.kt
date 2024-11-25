package com.rasadhana.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)
