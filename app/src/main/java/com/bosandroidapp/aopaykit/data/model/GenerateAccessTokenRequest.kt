package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class GenerateAccessTokenRequest(

	@field:SerializedName("fcmToken")
	val fcmToken: String? = null
)
