package com.bosandroidapp.aopaykit.data.model.kitoption

import com.google.gson.annotations.SerializedName

data class KitOptionResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

	@field:SerializedName("availableOnlineBalance")
	val availableOnlineBalance: Int? = null,

	@field:SerializedName("isKit")
	val isKit: Boolean? = null,

	@field:SerializedName("availableKitBalance")
	val availableKitBalance: Int? = null,

	@field:SerializedName("offlineMaxLoanLimit")
	val offlineMaxLoanLimit: Int? = null,

	@field:SerializedName("availableOfflineBalance")
	val availableOfflineBalance: Int? = null,

	@field:SerializedName("onlineMaxLoanLimit")
	val onlineMaxLoanLimit: Int? = null,

	@field:SerializedName("kitMaxLoanLimit")
	val kitMaxLoanLimit: Int? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("isOnline")
	val isOnline: Boolean? = null,

	@field:SerializedName("isOffline")
	val isOffline: Boolean? = null
)
