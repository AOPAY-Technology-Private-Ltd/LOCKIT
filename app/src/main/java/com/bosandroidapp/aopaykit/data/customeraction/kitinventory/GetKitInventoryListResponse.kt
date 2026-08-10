package com.bosandroidapp.aopaykit.data.customeraction.kitinventory

import com.google.gson.annotations.SerializedName

data class GetKitInventoryListResponse(

	@field:SerializedName("totalKits")
	val totalKits: Int? = null,

	@field:SerializedName("availableKits")
	val availableKits: Int? = null,

	@field:SerializedName("kitList")
	val kitList: List<KitListItem?>? = null,

	@field:SerializedName("usedKits")
	val usedKits: Int? = null
)

data class KitListItem(

	@field:SerializedName("installedOn")
	val installedOn: String? = null,

	@field:SerializedName("serialNumber")
	val serialNumber: String? = null,

	@field:SerializedName("kitStatus")
	val kitStatus: String? = null,

	@field:SerializedName("imeiNumber")
	val imeiNumber: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("model")
	val model: String? = null,

	@field:SerializedName("loanCode")
	val loanCode: Int? = null,

	@field:SerializedName("deviceName")
	val deviceName: String? = null,

	@field:SerializedName("brand")
	val brand: String? = null,

	@field:SerializedName("customerName")
	val customerName: String? = null,

	@field:SerializedName("serialNo")
	val serialNo: Int? = null,

	@field:SerializedName("manufacturer")
	val manufacturer: String? = null
)
