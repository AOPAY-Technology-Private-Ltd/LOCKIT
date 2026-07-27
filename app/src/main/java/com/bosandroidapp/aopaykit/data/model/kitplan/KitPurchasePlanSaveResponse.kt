package com.bosandroidapp.aopaykit.data.model.kitplan

import com.google.gson.annotations.SerializedName

data class KitPurchasePlanSaveResponse(

	@field:SerializedName("data")
	val data: Any? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
