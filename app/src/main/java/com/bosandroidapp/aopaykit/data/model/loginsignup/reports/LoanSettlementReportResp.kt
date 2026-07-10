package com.bosandroidapp.aopaykit.data.model.loginsignup.reports

import com.google.gson.annotations.SerializedName

data class LoanSettlementReportResp(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)


data class DataItem(

	@field:SerializedName("processingFees")
	val processingFees: Double? = null,

	@field:SerializedName("downPayment")
	val downPayment: Double? = null,

	@field:SerializedName("memberShip")
	val memberShip: Double? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("holdingAmount")
	val holdingAmount: Double? = null,

	@field:SerializedName("settlementAmount")
	val settlementAmount: Double? = null,

	@field:SerializedName("customerName")
	val customerName: String? = null,

	@field:SerializedName("productName")
	val productName: String? = null,

	@field:SerializedName("loanAmount")
	val loanAmount: Double? = null

)
