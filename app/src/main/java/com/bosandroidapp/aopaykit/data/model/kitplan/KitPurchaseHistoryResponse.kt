package com.bosandroidapp.aopaykit.data.model.kitplan

import com.google.gson.annotations.SerializedName

data class KitPurchaseHistoryResponse(

	@field:SerializedName("data")
	val data: List<PurchaseHistory?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class PurchaseHistory(

	@field:SerializedName("gstAmount")
	val gstAmount: Double? = null,

	@field:SerializedName("purchaseCode")
	val purchaseCode: String? = null,

	@field:SerializedName("purchaseDate")
	val purchaseDate: String? = null,

	@field:SerializedName("netAmount")
	val netAmount: Double? = null,

	@field:SerializedName("paymentMode")
	val paymentMode: String? = null,

	@field:SerializedName("transactionNo")
	val transactionNo: String? = null,

	@field:SerializedName("planName")
	val planName: String? = null,

	@field:SerializedName("discountAmount")
	val discountAmount: Double? = null,

	@field:SerializedName("isActive")
	val isActive: Boolean? = null,

	@field:SerializedName("planCode")
	val planCode: String? = null,

	@field:SerializedName("planStartDate")
	val planStartDate: String? = null,

	@field:SerializedName("planEndDate")
	val planEndDate: String? = null,

	@field:SerializedName("planAmount")
	val planAmount: Double? = null,

	@field:SerializedName("paymentStatus")
	val paymentStatus: String? = null
)
