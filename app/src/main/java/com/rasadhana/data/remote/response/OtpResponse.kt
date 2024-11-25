package com.rasadhana.data.remote.response

import com.google.gson.annotations.SerializedName

data class OtpResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("otp")
	val otp: String,

	@field:SerializedName("message")
	val message: String
)
