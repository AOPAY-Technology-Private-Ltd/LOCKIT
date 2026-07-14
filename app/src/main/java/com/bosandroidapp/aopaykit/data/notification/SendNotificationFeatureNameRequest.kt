package com.bosandroidapp.aopaykit.data.notification

import com.google.gson.annotations.SerializedName

data class SendNotificationFeatureNameRequest(

	@field:SerializedName("clientCode")
	val clientCode: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("notificationCode")
	val notificationCode: String? = null
)
