package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class GetRetailerLedgerReq(

	@field:SerializedName("dealerCode")
	val dealerCode: String? = null,

	@field:SerializedName("fromDate")
	val fromDate: String? = null,

	@field:SerializedName("toDate")
	val toDate: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null
)
