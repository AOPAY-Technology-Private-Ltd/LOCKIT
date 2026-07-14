package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class CustomerlocationUploadReq(

	@field:SerializedName("taskType")
	val taskType: String? = null,

	@field:SerializedName("locationAuditID")
	val locationAuditID: Int? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("ipAddress")
	val ipAddress: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("loanCode")
	val loanCode: String? = null,

	@field:SerializedName("userName")
	val userName: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null
)
