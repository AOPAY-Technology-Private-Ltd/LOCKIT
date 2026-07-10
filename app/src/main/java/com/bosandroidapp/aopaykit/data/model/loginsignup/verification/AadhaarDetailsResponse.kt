package com.bosandroidapp.aopaykit.data.model.loginsignup.verification

import com.google.gson.annotations.SerializedName

data class AadhaarDetailsResponse(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("Value")
	val value: String? = null,

	@field:SerializedName("model")
	val model: AadhaarModel? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AadhaarModel(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("xmlResponse")
	val xmlResponse: String? = null,

	@field:SerializedName("maskedAdharNumber")
	val maskedAdharNumber: String? = null,

	@field:SerializedName("address")
	val address: AadhaarAddress? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("careOf")
	val careOf: String? = null,

	@field:SerializedName("dob")
	val dob: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("digilockerFiles")
	val digilockerFiles: List<DigilockerFilesItem?>? = null,

	@field:SerializedName("uniqueId")
	val uniqueId: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class AadhaarAddress(

	@field:SerializedName("loc")
	val loc: String? = null,

	@field:SerializedName("subdist")
	val subdist: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("pc")
	val pc: String? = null,

	@field:SerializedName("vtc")
	val vtc: String? = null,

	@field:SerializedName("street")
	val street: String? = null,

	@field:SerializedName("dist")
	val dist: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("landmark")
	val landmark: String? = null,

	@field:SerializedName("house")
	val house: String? = null,

	@field:SerializedName("po")
	val po: String? = null
)

data class DigilockerFilesItem(

	@field:SerializedName("docLink")
	val docLink: String? = null,

	@field:SerializedName("docExtension")
	val docExtension: String? = null,

	@field:SerializedName("docType")
	val docType: String? = null
)
