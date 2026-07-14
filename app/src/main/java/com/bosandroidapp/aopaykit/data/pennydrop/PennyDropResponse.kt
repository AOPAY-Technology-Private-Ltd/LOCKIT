package com.bosandroidapp.aopaykit.data.pennydrop

import com.google.gson.annotations.SerializedName

data class PennyDropResponse(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("Value")
	val value: String? = null,

	@field:SerializedName("model")
	val model: PennyDropModel? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PennyDropModel(

	@field:SerializedName("recommendedAction")
	val recommendedAction: Any? = null,

	@field:SerializedName("isNameMatch")
	val isNameMatch: Boolean? = null,

	@field:SerializedName("clientRefNum")
	val clientRefNum: String? = null,

	@field:SerializedName("paymentMode")
	val paymentMode: String? = null,

	@field:SerializedName("subCode")
	val subCode: Any? = null,

	@field:SerializedName("beneficiaryName")
	val beneficiaryName: String? = null,

	@field:SerializedName("transactionId")
	val transactionId: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("matchingScore")
	val matchingScore: Int? = null,

	@field:SerializedName("rrn")
	val rrn: String? = null,

	@field:SerializedName("desc")
	val desc: Any? = null
)
