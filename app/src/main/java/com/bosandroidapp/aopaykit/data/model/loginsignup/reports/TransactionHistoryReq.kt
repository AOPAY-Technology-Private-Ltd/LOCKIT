package com.bosandroidapp.aopaykit.data.model.loginsignup.reports

import com.google.gson.annotations.SerializedName

data class TransactionHistoryReq(

	@field:SerializedName("fromDate")
	val fromDate: String? = null,

	@field:SerializedName("toDate")
	val toDate: String? = null,

	@field:SerializedName("registrationId")
	val registrationId: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null
)
