package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class LowCibilCustomerReportResp(

	@field:SerializedName("data")
	val data: List<CibilDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class CibilDataItem(

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("custAadharBackPhoto_Path")
	val custAadharBackPhotoPath: String? = null,

	@field:SerializedName("panApiResponse")
	val panApiResponse: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("aadharNumberVerified")
	val aadharNumberVerified: String? = null,

	@field:SerializedName("primaryOTP")
	val primaryOTP: String? = null,

	@field:SerializedName("cibilApiResponse")
	val cibilApiResponse: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("isAggrementVerified")
	val isAggrementVerified: String? = null,

	@field:SerializedName("cityName")
	val cityName: String? = null,

	@field:SerializedName("eMailID")
	val eMailID: String? = null,

	@field:SerializedName("stateName")
	val stateName: String? = null,

	@field:SerializedName("cibilScore")
	val cibilScore: String? = null,

	@field:SerializedName("aearSector")
	val aearSector: String? = null,

	@field:SerializedName("alternateMobileNumber")
	val alternateMobileNumber: String? = null,

	@field:SerializedName("primaryMobileNumber")
	val primaryMobileNumber: String? = null,

	@field:SerializedName("custPanNumberPhoto_Path")
	val custPanNumberPhotoPath: String? = null,

	@field:SerializedName("panNumberVerified")
	val panNumberVerified: String? = null,

	@field:SerializedName("primaryMobileVerified")
	val primaryMobileVerified: String? = null,

	@field:SerializedName("panNumber")
	val panNumber: String? = null,

	@field:SerializedName("pAlternateMobileVerified")
	val pAlternateMobileVerified: String? = null,

	@field:SerializedName("currentAddress")
	val currentAddress: String? = null,

	@field:SerializedName("custPhoto_Path")
	val custPhotoPath: String? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("alternateMobileOTP")
	val alternateMobileOTP: String? = null,

	@field:SerializedName("flatNo")
	val flatNo: String? = null,

	@field:SerializedName("aadharNumber")
	val aadharNumber: String? = null,

	@field:SerializedName("activeStatus")
	val activeStatus: String? = null,

	@field:SerializedName("createdBy")
	val createdBy: String? = null,

	@field:SerializedName("pinCode")
	val pinCode: String? = null,

	@field:SerializedName("middleName")
	val middleName: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("loanID")
	val loanID: String? = null,

	@field:SerializedName("custAadharPhoto_Path")
	val custAadharPhotoPath: String? = null
)
