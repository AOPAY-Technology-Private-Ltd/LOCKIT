package com.bosandroidapp.aopaykit.data.loancharge

import com.google.gson.annotations.SerializedName

data class LoanChargeResp(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("Value")
	val value: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
