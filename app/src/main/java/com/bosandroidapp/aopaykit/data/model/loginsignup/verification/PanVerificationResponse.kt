package com.bosandroidapp.aopaykit.data.model.loginsignup.verification

import com.google.gson.annotations.SerializedName

data class PanVerificationResponse(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("Value")
	val value: String? = null,

	@field:SerializedName("result_code")
	val resultCode: Int? = null,

	@field:SerializedName("client_ref_num")
	val clientRefNum: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("request_id")
	val requestId: String? = null,

	@field:SerializedName("http_response_code")
	val httpResponseCode: Int? = null
)

data class Address(

	@field:SerializedName("building_name")
	val buildingName: String? = null,

	@field:SerializedName("pincode")
	val pincode: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("locality")
	val locality: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("street_name")
	val streetName: String? = null
)

data class Result(

	@field:SerializedName("address")
	val address: Address? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("middle_name")
	val middleName: String? = null,

	@field:SerializedName("aadhaar_linked")
	val aadhaarLinked: Boolean? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("aadhaar_number")
	val aadhaarNumber: String? = null,

	@field:SerializedName("pan_type")
	val panType: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("pan")
	val pan: String? = null,

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
