package com.bosandroidapp.aopaykit.data.model.loginsignup.reports

import com.google.gson.annotations.SerializedName

data class PayoutReportResp(

	@field:SerializedName("data")
	val data: List<PayoutDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PayoutDataItem(

	@field:SerializedName("servicesChargeAmt")
	val servicesChargeAmt: Double? = null,

	@field:SerializedName("recordTime")
	val recordTime: String? = null,

	@field:SerializedName("transactionStatus")
	val transactionStatus: String? = null,

	@field:SerializedName("recordDate")
	val recordDate: String? = null,

	@field:SerializedName("rid")
	val rid: Int? = null,

	@field:SerializedName("transferToMsg")
	val transferToMsg: String? = null,

	@field:SerializedName("gstAmt")
	val gstAmt: Double? = null,

	@field:SerializedName("transactionID")
	val transactionID: String? = null,

	@field:SerializedName("servicesChargeGSTAmt")
	val servicesChargeGSTAmt: Double? = null,

	@field:SerializedName("transferAmt")
	val transferAmt: Double? = null
)
