package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class ValidateAccessKeyReq(

	@field:SerializedName("apiacessKey")
	val apiacessKey: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null
)
