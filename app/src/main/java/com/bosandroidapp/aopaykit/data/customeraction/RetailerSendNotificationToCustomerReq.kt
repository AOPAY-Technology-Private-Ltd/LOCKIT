package com.bosandroidapp.aopaykit.data.customeraction

import com.google.gson.annotations.SerializedName

data class RetailerSendNotificationToCustomerReq(

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
	val notificationCode: String? = null,

	@field:SerializedName("devicePin")
	val devicePin: String? = null,

	@field:SerializedName("selectedApps")
	val selectedApps: List<RetailerSendNotificationToCustomer?>? = null

)




