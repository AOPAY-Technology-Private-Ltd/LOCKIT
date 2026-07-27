package com.bosandroidapp.aopaykit.data.model.kitplan

import com.google.gson.annotations.SerializedName

data class KitPlanResponse(

	@field:SerializedName("data")
	val data: List<KitPlanListDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class KitPlanListDataItem(

	@field:SerializedName("noOfKits")
	val noOfKits: Int? = null,

	@field:SerializedName("gstAmount")
	val gstAmount: Double? = null,

	@field:SerializedName("totalAmount")
	val totalAmount: Double? = null,

	@field:SerializedName("isDefault")
	val isDefault: Boolean? = null,

	@field:SerializedName("discountPercent")
	val discountPercent: Double? = null,

	@field:SerializedName("pricePerKit")
	val pricePerKit: Double? = null,

	@field:SerializedName("planName")
	val planName: String? = null,

	@field:SerializedName("discountAmount")
	val discountAmount: Double? = null,

	@field:SerializedName("mappingCode")
	val mappingCode: String? = null,

	@field:SerializedName("planCode")
	val planCode: String? = null,

	@field:SerializedName("planAmount")
	val planAmount: Double? = null,

	@field:SerializedName("gstPercent")
	val gstPercent: Double? = null,

	var isSelected: Boolean = false
)
