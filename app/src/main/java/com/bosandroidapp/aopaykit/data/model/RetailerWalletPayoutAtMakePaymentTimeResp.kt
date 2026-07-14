package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class RetailerWalletPayoutAtMakePaymentTimeResp(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statuss")
	val statuss: String? = null,

	@field:SerializedName("value")
	val value: String? = null
)
