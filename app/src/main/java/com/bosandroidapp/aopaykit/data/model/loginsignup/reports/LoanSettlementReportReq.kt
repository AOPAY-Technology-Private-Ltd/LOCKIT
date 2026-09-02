package com.bosandroidapp.aopaykit.data.model.loginsignup.reports

import com.google.gson.annotations.SerializedName

data class LoanSettlementReportReq(

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null
)
