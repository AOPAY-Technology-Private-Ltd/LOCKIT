package com.bosandroidapp.aopaykit.data.customeraction.kitinventory

import com.google.gson.annotations.SerializedName

data class GetKitInventoryListRequest(

	/*@field:SerializedName("clientCode")
	val clientCode: String? = null,*/

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
