package com.bosandroidapp.aopaykit.data.notification

import com.google.gson.annotations.SerializedName

data class NotificationSendTokenRequest(

	@field:SerializedName("deviceType")
	val deviceType: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("fcmToken")
	val fcmToken: String? = null
)
