package com.rasadhana.data.remote.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)
