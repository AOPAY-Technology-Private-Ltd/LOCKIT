package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class SessionOutReq(

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@SerializedName("clientcode")
	var clientCode: String
)
