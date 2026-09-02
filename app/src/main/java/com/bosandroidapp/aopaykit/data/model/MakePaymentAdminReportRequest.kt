package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class MakePaymentAdminReportRequest(

	@field:SerializedName("fromDate")
	val fromDate: String? = null,

	@field:SerializedName("toDate")
	val toDate: String? = null,

	@field:SerializedName("activeStatus")
	val activeStatus: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null
)
