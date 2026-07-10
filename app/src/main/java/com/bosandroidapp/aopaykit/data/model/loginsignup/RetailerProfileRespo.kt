package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class RetailerProfileRespo(

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("aadharNumber")
	val aadharNumber: String? = null,

	@field:SerializedName("activeStatus")
	val activeStatus: Any? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("emailid")
	val emailid: String? = null,

	@field:SerializedName("mobileNo")
	val mobileNo: String? = null,

	@field:SerializedName("panNumber")
	val panNumber: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statuss")
	val statuss: String? = null,

	@field:SerializedName("value")
	val value: String? = null
)
