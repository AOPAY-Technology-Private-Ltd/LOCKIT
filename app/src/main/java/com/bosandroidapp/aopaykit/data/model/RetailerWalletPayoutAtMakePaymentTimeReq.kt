package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class RetailerWalletPayoutAtMakePaymentTimeReq(

	@field:SerializedName("amountType")
	val amountType: String? = null,

	@field:SerializedName("gstAmount")
	val gstAmount: Int? = null,

	@field:SerializedName("transferFrom")
	val transferFrom: String? = null,

	@field:SerializedName("serviceschargeAmount")
	val serviceschargeAmount: Int? = null,

	@field:SerializedName("actualCommissionAmount")
	val actualCommissionAmount: Int? = null,

	@field:SerializedName("transIpAddress")
	val transIpAddress: String? = null,

	@field:SerializedName("transferAmount")
	val transferAmount: Double? = null,

	@field:SerializedName("remark")
	val remark: String? = null,

	@field:SerializedName("customerCommission_withoutGST")
	val customerCommissionWithoutGST: Int? = null,

	@field:SerializedName("transferTo")
	val transferTo: String? = null,

	@field:SerializedName("transferToMsg")
	val transferToMsg: String? = null,

	@field:SerializedName("serviceschargeWithoutGST")
	val serviceschargeWithoutGST: Int? = null,

	@field:SerializedName("customerCommission")
	val customerCommission: Int? = null,

	@field:SerializedName("customerCommissionGST")
	val customerCommissionGST: Int? = null,

	@field:SerializedName("commissionWithoutGST")
	val commissionWithoutGST: Int? = null,

	@field:SerializedName("transferFromMsg")
	val transferFromMsg: String? = null,

	@field:SerializedName("registrationId")
	val registrationId: String? = null,

	@field:SerializedName("tdsAmount")
	val tdsAmount: Int? = null,

	@field:SerializedName("serviceschargeGSTAmount")
	val serviceschargeGSTAmount: Int? = null,

	@field:SerializedName("transactionStatus")
	val transactionStatus: String? = null,

	//loanCode
	@field:SerializedName("loanCode")
	val loanCode: String? = null
)
