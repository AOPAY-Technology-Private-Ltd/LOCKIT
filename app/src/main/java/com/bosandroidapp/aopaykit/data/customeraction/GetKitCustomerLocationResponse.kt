package com.bosandroidapp.aopaykit.data.customeraction

import com.google.gson.annotations.SerializedName

data class GetKitCustomerLocationResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Data(

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("locationTime")
	val locationTime: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("rid")
	val rid: Int? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)
