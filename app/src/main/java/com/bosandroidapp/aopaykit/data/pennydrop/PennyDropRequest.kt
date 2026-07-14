package com.bosandroidapp.aopaykit.data.pennydrop

import com.google.gson.annotations.SerializedName

data class PennyDropRequest(

	@field:SerializedName("BankName")
	val bankName: String? = null,

	@field:SerializedName("BenificiaryName")
	val benificiaryName: String? = null,

	@field:SerializedName("Address")
	val address: String? = null,

	@field:SerializedName("paymentMode")
	val paymentMode: String? = null,

	@field:SerializedName("IFSCCode")
	val iFSCCode: String? = null,

	@field:SerializedName("RegistrationID")
	val registrationID: String? = null,

	@field:SerializedName("RefID")
	val refID: String? = null,

	@field:SerializedName("AccountNumber")
	val accountNumber: String? = null
)
