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
	val manufacturer: String? = null
)
