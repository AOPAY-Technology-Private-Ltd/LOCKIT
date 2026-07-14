package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class UploadDeviceInfoResp(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
