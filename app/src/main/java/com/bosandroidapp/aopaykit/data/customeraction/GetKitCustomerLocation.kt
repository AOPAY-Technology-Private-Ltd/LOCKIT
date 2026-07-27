package com.bosandroidapp.aopaykit.data.customeraction

import com.google.gson.annotations.SerializedName

data class GetKitCustomerLocation(

	@field:SerializedName("clientCode")
	val clientCode: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null
)
