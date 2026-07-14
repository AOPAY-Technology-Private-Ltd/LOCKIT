package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class LowCibilCustomerReportReq(

	@field:SerializedName("reportType")
	val reportType: String? = null,

	@field:SerializedName("fromDate")
	val fromDate: String? = null,

	@field:SerializedName("toDate")
	val toDate: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null
)
