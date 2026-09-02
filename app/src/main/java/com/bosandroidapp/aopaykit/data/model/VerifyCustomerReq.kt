package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class VerifyCustomerReq(

	@field:SerializedName("primaryMobileNumber")
	val primaryMobileNumber: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null
)
