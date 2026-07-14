package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class MakePaymentAdminReportResponse(

	@field:SerializedName("data")
	val data: List<MakePaymentReportDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class MakePaymentReportDataItem(

	@field:SerializedName("recordStatus")
	val recordStatus: String? = null,

	@field:SerializedName("activeStatus")
	val activeStatus: String? = null,

	@field:SerializedName("transactionDateTime")
	val transactionDateTime: String? = null,

	@field:SerializedName("requestDateTime")
	val requestDateTime: String? = null,

	@field:SerializedName("retailerName")
	val retailerName: String? = null,

	@field:SerializedName("transactionNo")
	val transactionNo: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("requestAmount")
	val requestAmount: Double? = null,

	@field:SerializedName("utrNumber")
	val utrNumber: String? = null
)
