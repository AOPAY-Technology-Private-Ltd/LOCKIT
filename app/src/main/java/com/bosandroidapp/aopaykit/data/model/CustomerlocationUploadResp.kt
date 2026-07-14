package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class CustomerlocationUploadResp(

	@field:SerializedName("totalRecords")
	val totalRecords: Int? = null,

	@field:SerializedName("data")
	val data: List<LocationDataItem?>? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class LocationDataItem(

	@field:SerializedName("locationAuditID")
	val locationAuditID: Int? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("updatedBy")
	val updatedBy: Any? = null,

	@field:SerializedName("createdBy")
	val createdBy: Any? = null,

	@field:SerializedName("latitude")
	val latitude: Any? = null,

	@field:SerializedName("ipAddress")
	val ipAddress: Any? = null,

	@field:SerializedName("customerCode")
	val customerCode: Any? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: Any? = null,

	@field:SerializedName("loanCode")
	val loanCode: Any? = null,

	@field:SerializedName("updatedDate")
	val updatedDate: Any? = null,

	@field:SerializedName("longitude")
	val longitude: Any? = null
)
