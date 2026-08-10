package com.bosandroidapp.aopaykit.data.customeraction

import com.google.gson.annotations.SerializedName

data class CustomerSideUpdateUnInstallAppRequest(

	@field:SerializedName("clientCode")
	val clientCode: String? = null,

	@field:SerializedName("appName")
	val appName: String? = null,

	@field:SerializedName("eventTime")
	val eventTime: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("packageName")
	val packageName: String? = null
)
