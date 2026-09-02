package com.bosandroidapp.aopaykit.data.pennydrop

import com.google.gson.annotations.SerializedName

data class BankListReq(

	@field:SerializedName("RegistrationID")
	val registrationID: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null
)
