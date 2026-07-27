package com.bosandroidapp.aopaykit.data.model.kitoption

import com.google.gson.annotations.SerializedName

data class KitCustomerListResponse(

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("customerList")
	val customerList: List<CustomerListItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statuss")
	val statuss: String? = null,

	@field:SerializedName("value")
	val value: String? = null
)

data class CustomerListItem(

	@field:SerializedName("refRelationShip")
	val refRelationShip: String? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("imeiNumber2_SealPhotoFile")
	val imeiNumber2SealPhotoFile: Any? = null,

	@field:SerializedName("appVersion")
	val appVersion: String? = null,

	@field:SerializedName("memberShipFees")
	val memberShipFees: Any? = null,

	@field:SerializedName("custPanNumberPhoto_File")
	val custPanNumberPhotoFile: Any? = null,

	@field:SerializedName("aadharNumberVerified")
	val aadharNumberVerified: String? = null,

	@field:SerializedName("refPanNumber")
	val refPanNumber: String? = null,

	@field:SerializedName("isActive")
	val isActive: String? = null,

	@field:SerializedName("deviceName")
	val deviceName: String? = null,

	@field:SerializedName("refAdhaarNumberBackPhoto_Path")
	val refAdhaarNumberBackPhotoPath: Any? = null,

	@field:SerializedName("aadhaarApiResponse")
	val aadhaarApiResponse: Any? = null,

	@field:SerializedName("custAadharPhoto_File")
	val custAadharPhotoFile: Any? = null,

	@field:SerializedName("emiAmount")
	val emiAmount: String? = null,

	@field:SerializedName("eMailID")
	val eMailID: String? = null,

	@field:SerializedName("stateName")
	val stateName: String? = null,

	@field:SerializedName("aearSector")
	val aearSector: String? = null,

	@field:SerializedName("downPayment")
	val downPayment: String? = null,

	@field:SerializedName("bankIFSCCode")
	val bankIFSCCode: String? = null,

	@field:SerializedName("model")
	val model: String? = null,

	@field:SerializedName("refName")
	val refName: String? = null,

	@field:SerializedName("brand")
	val brand: String? = null,

	@field:SerializedName("tenure")
	val tenure: String? = null,

	@field:SerializedName("refAdhaarNumberFrontPhoto_Path")
	val refAdhaarNumberFrontPhotoPath: Any? = null,

	@field:SerializedName("brandName")
	val brandName: String? = null,

	@field:SerializedName("upiMandate")
	val upiMandate: String? = null,

	@field:SerializedName("accountType")
	val accountType: String? = null,

	@field:SerializedName("custAadharBackPhoto_File")
	val custAadharBackPhotoFile: Any? = null,

	@field:SerializedName("refPanNumberPhoto_Path")
	val refPanNumberPhotoPath: Any? = null,

	@field:SerializedName("imeiNumber2_SealPhotoPath")
	val imeiNumber2SealPhotoPath: String? = null,

	@field:SerializedName("panNumber")
	val panNumber: String? = null,

	@field:SerializedName("pAlternateMobileVerified")
	val pAlternateMobileVerified: String? = null,

	@field:SerializedName("custPhoto_File")
	val custPhotoFile: Any? = null,

	@field:SerializedName("firstName")
	val firstName: String? = null,

	@field:SerializedName("flatNo")
	val flatNo: String? = null,

	@field:SerializedName("imeiNumberPhotoFile")
	val imeiNumberPhotoFile: Any? = null,

	@field:SerializedName("invoive_Path")
	val invoivePath: String? = null,

	@field:SerializedName("pinCode")
	val pinCode: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("sdkVersion")
	val sdkVersion: String? = null,

	@field:SerializedName("lastName")
	val lastName: String? = null,

	@field:SerializedName("imeiNumber1_SealPhotoPath")
	val imeiNumber1SealPhotoPath: String? = null,

	@field:SerializedName("refPanNumberPhoto_File")
	val refPanNumberPhotoFile: Any? = null,

	@field:SerializedName("imeiNumber_PhotoPath")
	val imeiNumberPhotoPath: String? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("custAadharBackPhoto_Path")
	val custAadharBackPhotoPath: String? = null,

	@field:SerializedName("customerCodes")
	val customerCodes: String? = null,

	@field:SerializedName("panApiResponse")
	val panApiResponse: Any? = null,

	@field:SerializedName("refAdhaarNumberFrontPhoto_File")
	val refAdhaarNumberFrontPhotoFile: Any? = null,

	@field:SerializedName("imeiNumber1")
	val imeiNumber1: String? = null,

	@field:SerializedName("imeiNumber2")
	val imeiNumber2: String? = null,

	@field:SerializedName("bankName")
	val bankName: String? = null,

	@field:SerializedName("primaryOTP")
	val primaryOTP: String? = null,

	@field:SerializedName("modelVariant")
	val modelVariant: String? = null,

	@field:SerializedName("cibilApiResponse")
	val cibilApiResponse: Any? = null,

	@field:SerializedName("manufacturer")
	val manufacturer: String? = null,

	@field:SerializedName("invoiceFile")
	val invoiceFile: Any? = null,

	@field:SerializedName("sellingPrice")
	val sellingPrice: String? = null,

	@field:SerializedName("isAggrementVerified")
	val isAggrementVerified: String? = null,

	@field:SerializedName("cityName")
	val cityName: String? = null,

	@field:SerializedName("osVersion")
	val osVersion: String? = null,

	@field:SerializedName("cibilScore")
	val cibilScore: String? = null,

	@field:SerializedName("alternateMobileNumber")
	val alternateMobileNumber: String? = null,

	@field:SerializedName("primaryMobileNumber")
	val primaryMobileNumber: String? = null,

	@field:SerializedName("custPanNumberPhoto_Path")
	val custPanNumberPhotoPath: String? = null,

	@field:SerializedName("refAdhaarNumber")
	val refAdhaarNumber: String? = null,

	@field:SerializedName("refAddress")
	val refAddress: String? = null,

	@field:SerializedName("panNumberVerified")
	val panNumberVerified: String? = null,

	@field:SerializedName("refAdhaarNumberBackPhoto_File")
	val refAdhaarNumberBackPhotoFile: Any? = null,

	@field:SerializedName("primaryMobileVerified")
	val primaryMobileVerified: String? = null,

	@field:SerializedName("custPhoto_path")
	val custPhotoPath: String? = null,

	@field:SerializedName("imeiNumber1_SealPhotoFile")
	val imeiNumber1SealPhotoFile: Any? = null,

	@field:SerializedName("isRetailerAggrementVerified")
	val isRetailerAggrementVerified: String? = null,

	@field:SerializedName("refmobileNo")
	val refmobileNo: String? = null,

	@field:SerializedName("branchName")
	val branchName: String? = null,

	@field:SerializedName("accountNumber")
	val accountNumber: String? = null,

	@field:SerializedName("currentAddress")
	val currentAddress: String? = null,

	@field:SerializedName("modelName")
	val modelName: String? = null,

	@field:SerializedName("alternateMobileOTP")
	val alternateMobileOTP: String? = null,

	@field:SerializedName("aadharNumber")
	val aadharNumber: String? = null,

	@field:SerializedName("imeiNumber")
	val imeiNumber: String? = null,

	@field:SerializedName("debitOrCreditCard")
	val debitOrCreditCard: String? = null,

	@field:SerializedName("createdBy")
	val createdBy: Any? = null,

	@field:SerializedName("middleName")
	val middleName: String? = null,

	@field:SerializedName("custAadharPhoto_Path")
	val custAadharPhotoPath: String? = null,

	@field:SerializedName("isrefKycVerified")
	val isrefKycVerified: String? = null,

	@field:SerializedName("customerActiveStatus")
	val customerActiveStatus: String? = null,

	@field:SerializedName("devicePin")
	val devicePin: String? = null,

	@field:SerializedName("isDeviceLocked")
	val isDeviceLocked: Boolean? = null,

	@field:SerializedName("serialNumber")
	val serialNumber: String? = null

)
