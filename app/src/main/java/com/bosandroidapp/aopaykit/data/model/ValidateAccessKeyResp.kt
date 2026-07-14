package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class ValidateAccessKeyResp(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statusCode")
	val statusCode: Int? = null
)
