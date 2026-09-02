package com.bosandroidapp.aopaykit.data.model.kitplan

import com.google.gson.annotations.SerializedName

data class KitPurchaseHistoryRequest(

	@field:SerializedName("companyCode")
	val companyCode: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null
)
