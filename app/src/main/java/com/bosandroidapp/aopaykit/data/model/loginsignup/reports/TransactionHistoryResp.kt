package com.bosandroidapp.aopaykit.data.model.loginsignup.reports

import com.google.gson.annotations.SerializedName

data class TransactionHistoryResp(

	@field:SerializedName("data")
	val data: List<TransactionHistoryDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)


data class TransactionHistoryDataItem(

	@field:SerializedName("amountType")
	val amountType: String? = null,

	@field:SerializedName("gstAmount")
	val gstAmount: Double? = null,

	@field:SerializedName("actualTransactionAmount")
	val actualTransactionAmount: Double? = null,

	@field:SerializedName("transactionStatus")
	val transactionStatus: String? = null,

	@field:SerializedName("remark")
	val remark: String? = null,

	@field:SerializedName("servicesChargeGSTAmount")
	val servicesChargeGSTAmount: Double? = null,

	@field:SerializedName("debitAmount")
	val debitAmount: Double? = null,

	@field:SerializedName("rid")
	val rid: Int? = null,

	@field:SerializedName("transactionDate")
	val transactionDate: String? = null,

	@field:SerializedName("transactionTime")
	val transactionTime: String? = null,

	@field:SerializedName("transactionID")
	val transactionID: String? = null,

	@field:SerializedName("transactionType")
	val transactionType: String? = null,

	@field:SerializedName("servicesChargeAmount")
	val servicesChargeAmount: Double? = null,

	@field:SerializedName("tdsAmount")
	val tdsAmount: Double? = null,

	@field:SerializedName("creditAmount")
	val creditAmount: Double? = null,

	@field:SerializedName("transactionMessage")
	val transactionMessage: String? = null

)
