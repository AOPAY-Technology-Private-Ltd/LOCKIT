package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class ValidateSessionRequest(

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("deviceId")
	val deviceId: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null

)
