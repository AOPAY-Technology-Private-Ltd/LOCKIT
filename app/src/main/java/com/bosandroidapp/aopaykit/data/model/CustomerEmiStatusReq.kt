package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class CustomerEmiStatusReq(

	@field:SerializedName("loanCode")
	val loanCode: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null
)
