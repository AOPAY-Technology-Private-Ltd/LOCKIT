package com.bosandroidapp.aopaykit.data.model.kitplan

import com.google.gson.annotations.SerializedName

data class KitPlanRequest(

	@field:SerializedName("companyCode")
	val companyCode: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null
)
