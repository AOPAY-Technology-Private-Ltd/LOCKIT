package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class DueOverdueResponse(

	@field:SerializedName("totalRecords")
	val totalRecords: Int? = null,

	@field:SerializedName("data")
	val data: MutableList<OverdueDataItem?>? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class OverdueDataItem(

	@field:SerializedName("paidEMI")
	val paidEMI: Int? = null,

	@field:SerializedName("dueDate")
	val dueDate: String? = null,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("refmobileNo")
	val refmobileNo: String? = null,

	@field:SerializedName("ipAddress")
	val ipAddress: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("dueEMI")
	val dueEMI: Int? = null,

	@field:SerializedName("customerName")
	val customerName: String? = null,

	@field:SerializedName("loanAmount")
	val loanAmount: Double? = null,

	@field:SerializedName("lastLocationDate")
	val lastLocationDate: String? = null,

	@field:SerializedName("emiAmount")
	val emiAmount: Double? = null,

	@field:SerializedName("customerPhoto")
	val customerPhoto: String? = null,

	@field:SerializedName("downPayment")
	val downPayment: Double? = null,

	@field:SerializedName("loanStatus")
	val loanStatus: String? = null,

	@field:SerializedName("alternateMobileNumber")
	val alternateMobileNumber: String? = null,

	@field:SerializedName("primaryMobileNumber")
	val primaryMobileNumber: String? = null,

	@field:SerializedName("loanCode")
	val loanCode: String? = null,

	@field:SerializedName("tenure")
	val tenure: Int? = null,

	@field:SerializedName("centerName")
	val centerName: String? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null

)
