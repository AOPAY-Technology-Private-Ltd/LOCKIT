package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class GetRetailerLedgerResponse(

	@field:SerializedName("data")
	val data: List<LedgerReportDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class LedgerReportDataItem(

	@field:SerializedName("emI_No")
	val emINo: Int? = null,

	@field:SerializedName("dealerCode")
	val dealerCode: String? = null,

	@field:SerializedName("dueDate")
	val dueDate: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("remark")
	val remark: String? = null,

	@field:SerializedName("deductedHoldAmount")
	val deductedHoldAmount: Double? = null,

	@field:SerializedName("emiAmount")
	val emiAmount: Double? = null,

	@field:SerializedName("accountNo")
	val accountNo: String? = null,

	@field:SerializedName("remainingHoldBalance")
	val remainingHoldBalance: Double? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("paymentStatus")
	val paymentStatus: String? = null,

	@field:SerializedName("dealerName")
	val dealerName: String? = null,

	@field:SerializedName("paymentMode")
	val paymentMode: String? = null,

	@field:SerializedName("mobileNo")
	val mobileNo: String? = null,

	@field:SerializedName("transactionDirection")
	val transactionDirection: String? = null,

	@field:SerializedName("transactionDate")
	val transactionDate: String? = null,

	@field:SerializedName("customerName")
	val customerName: String? = null,

	@field:SerializedName("transactionType")
	val transactionType: String? = null,

	@field:SerializedName("emiReferenceNo")
	val emiReferenceNo: String? = null,

	@field:SerializedName("recordStatus")
	val recordStatus: String? = null,

	@field:SerializedName("createdBy")
	val createdBy: String? = null,

	@field:SerializedName("receiptNo")
	val receiptNo: String? = null,

	@field:SerializedName("narration")
	val narration: String? = null,

	@field:SerializedName("loanCode")
	val loanCode: String? = null,

	@field:SerializedName("refTransactionId")
	val refTransactionId: String? = null,

	@field:SerializedName("previousHoldBalance")
	val previousHoldBalance: Double? = null,

	@field:SerializedName("bouncingCharge")
	val bouncingCharge: Double? = null,

	@field:SerializedName("lateFine")
	val lateFine: Double? = null,

	@field:SerializedName("otherCharge")
	val otherCharge: Double? = null,

	@field:SerializedName("waiveOffAmount")
	val waiveOffAmount: Double? = null,
)
