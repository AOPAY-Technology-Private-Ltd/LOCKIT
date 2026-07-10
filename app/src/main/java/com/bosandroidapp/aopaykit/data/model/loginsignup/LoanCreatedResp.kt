package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class LoanCreatedResp(

	@field:SerializedName("qrCodeBase64")
	val qrCodeBase64: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("installUrl")
	val installUrl: String? = null
)

data class Data(

	@field:SerializedName("interestRate")
	val interestRate: Double? = null,

	@field:SerializedName("emiAmount")
	val emiAmount: Double? = null,

	@field:SerializedName("imeiNumber")
	val imeiNumber: String? = null,

	@field:SerializedName("endDate")
	val endDate: String? = null,

	@field:SerializedName("downPayment")
	val downPayment: Double? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("loanCode")
	val loanCode: String? = null,

	@field:SerializedName("rid")
	val rid: Int? = null,

	@field:SerializedName("loanAmount")
	val loanAmount: Double? = null,

	@field:SerializedName("tenure")
	val tenure: Int? = null,

	@field:SerializedName("startDate")
	val startDate: String? = null
)
