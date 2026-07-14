package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class GenerateAccessTokenResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)

data class Data(

	@field:SerializedName("apiacessKey")
	val apiacessKey: String? = null
)
