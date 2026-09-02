package com.bosandroidapp.aopaykit.data.pennydrop

import com.google.gson.annotations.SerializedName

data class PennyDropCheckStatusRequest(

	@field:SerializedName("RegistrationID")
	val registrationID: String? = null,

	@field:SerializedName("RefID")
	val refID: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null
)
