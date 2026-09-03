package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class GenerateAccessTokenRequest(

	@field:SerializedName("fcmToken")
	val fcmToken: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

)
