package com.bosandroidapp.aopaykit.data.enach

import com.google.gson.annotations.SerializedName

data class EnachDateUploadReq(

	@field:SerializedName("isEmandateVerified")
	val isEmandateVerified: String? = null,

	@field:SerializedName("emAccountType")
	val emAccountType: String? = null,

	@field:SerializedName("isPannydropVerified")
	val isPannydropVerified: String? = null,

	@field:SerializedName("emAccountNumber")
	val emAccountNumber: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("loanCode")
	val loanCode: String? = null,

	@field:SerializedName("emBankName")
	val emBankName: String? = null,

	@field:SerializedName("emIfscCode")
	val emIfscCode: String? = null
)
