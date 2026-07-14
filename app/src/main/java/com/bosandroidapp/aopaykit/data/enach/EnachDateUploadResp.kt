package com.bosandroidapp.aopaykit.data.enach

import com.google.gson.annotations.SerializedName

data class EnachDateUploadResp(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
