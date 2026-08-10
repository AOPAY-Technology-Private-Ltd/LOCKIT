package com.bosandroidapp.aopaykit.data.customeraction

import com.google.gson.annotations.SerializedName

data class SaveRetailerDeviceTokenResponse(

	@field:SerializedName("data")
	val data: Any? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
