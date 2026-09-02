package com.bosandroidapp.aopaykit.data.customeraction

import com.google.gson.annotations.SerializedName

data class GetPendingDeviceActionReq(

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null
)
