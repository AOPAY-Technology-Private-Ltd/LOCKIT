package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class UploadDeviceInfoReq(

	@field:SerializedName("appVersion")
	val appVersion: String? = null,

	@field:SerializedName("imeiNumber")
	val imeiNumber: String? = null,

	@field:SerializedName("osVersion")
	val osVersion: String? = null,

	@field:SerializedName("model")
	val model: String? = null,

	@field:SerializedName("sdkVersion")
	val sdkVersion: String? = null,

	@field:SerializedName("deviceID")
	val deviceID: String? = null,

	@field:SerializedName("brand")
	val brand: String? = null,

	@field:SerializedName("deviceName")
	val deviceName: String? = null,

	@field:SerializedName("manufacturer")
	val manufacturer: String? = null,

	@field:SerializedName("serialNumber")
	val serialNumber: String? = null,

	@field:SerializedName("iccid")
	val iccid: String? = null,

	@field:SerializedName("subscriptionId")
	val subscriptionId: Int? = null,

	@field:SerializedName("carrierName")
	val carrierName: String? = null,

	@field:SerializedName("mcc")
	val mcc: String? = null,

	@field:SerializedName("mnc")
	val mnc: String? = null,

	@field:SerializedName("slotIndex")
	val slotIndex: Int? = null

)
