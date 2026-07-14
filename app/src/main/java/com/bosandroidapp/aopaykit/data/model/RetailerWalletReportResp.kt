package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class RetailerWalletReportResp(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("amountType")
	val amountType: String? = null,

	@field:SerializedName("amount")
	val amount: Any? = null,

	@field:SerializedName("transactionStatus")
	val transactionStatus: String? = null,

	@field:SerializedName("transactionDate")
	val transactionDate: String? = null,

	@field:SerializedName("retailerID")
	val retailerID: String? = null,

	@field:SerializedName("transactionID")
	val transactionID: String? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null
)
