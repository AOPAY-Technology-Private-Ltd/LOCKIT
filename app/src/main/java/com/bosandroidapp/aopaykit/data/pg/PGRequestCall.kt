package com.bosandroidapp.aopaykit.data.pg

import com.google.gson.annotations.SerializedName

data class PGRequestCall(

	@field:SerializedName("PayCustomerPhoneNo")
	val payCustomerPhoneNo: String? = null,

	@field:SerializedName("CustomerEmailID")
	val customerEmailID: String? = null,

	@field:SerializedName("RegistrationID")
	val registrationID: String? = null,

	@field:SerializedName("PayCartAmount")
	val payCartAmount: String? = null,

	@field:SerializedName("EMINumbers")
	val eMINumbers: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("PayCustomerName")
	val payCustomerName: String? = null,

	@field:SerializedName("LoanCode")
	val loanCode: String? = null,

	@field:SerializedName("RetailerCode")
	val retailerCode: String? = null
)
