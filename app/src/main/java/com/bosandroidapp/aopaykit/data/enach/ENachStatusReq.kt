package com.bosandroidapp.aopaykit.data.enach

import com.google.gson.annotations.SerializedName

data class ENachStatusReq(

	@field:SerializedName("RegistrationID")
	val registrationID: String? = null,

	@field:SerializedName("EMandateID")
	val eMandateID: String? = null
)
