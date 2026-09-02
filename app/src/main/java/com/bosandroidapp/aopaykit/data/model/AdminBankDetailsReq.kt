package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class AdminBankDetailsReq(

	@field:SerializedName("adminCode")
	val adminCode: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null
)
