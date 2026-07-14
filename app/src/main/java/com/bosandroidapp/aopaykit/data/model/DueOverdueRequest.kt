package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class DueOverdueRequest(

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("reportType")
	val reportType: String? = null

)
