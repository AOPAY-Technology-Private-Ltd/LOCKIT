package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class RetailerWalletResponse(

	@field:SerializedName("walletBalance")
	val walletBalance: String? = null,

	@field:SerializedName("holdAmount")
	val holdAmount: String? = null,

	@field:SerializedName("loanSecurityHoldAmount")
	val loanSecurityHoldAmount: String? = null,

	@field:SerializedName("maxHoldReleaseAmount")  //maxHoldingAmount
	val maxholdAmount: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statuss")
	val statuss: String? = null,

	@field:SerializedName("value")
	val value: String? = null,

	@SerializedName("minHoldReleaseAmount")  //miniholdamountRequestvalue
	val miniholdamountrequest: String? = "",

	@SerializedName("retailerPayoutServicechargeType")
	val servicechargetype: String? = "",

	@SerializedName("bouncingcharges")
	val bounseCharge: String? = "",

	@SerializedName("othercharges")
	val othercharges: String? = "",

	@SerializedName("waveoff")
	val waveOff: String? = "",

	@SerializedName("loan_autoapproval_status")
	val loanApprovalStatus: String? = "",

	@SerializedName("cibilscore")
	val cibilScore: String? = ""

)
