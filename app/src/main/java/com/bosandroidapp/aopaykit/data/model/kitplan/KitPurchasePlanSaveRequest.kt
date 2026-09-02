package com.bosandroidapp.aopaykit.data.model.kitplan

import com.google.gson.annotations.SerializedName

data class KitPurchasePlanSaveRequest(

	@field:SerializedName("companyCode")
	val companyCode: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null,

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

	@field:SerializedName("discountAmount")
	val discountAmount: Double? = null,

	@field:SerializedName("paymentReferenceNo")
	val paymentReferenceNo: String? = null,

	@field:SerializedName("mappingCode")
	val mappingCode: String? = null,

	@field:SerializedName("isActive")
	val isActive: Boolean? = null,

	@field:SerializedName("planCode")
	val planCode: String? = null,

	@field:SerializedName("createdBy")
	val createdBy: String? = null,

	@field:SerializedName("retailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("planStartDate")
	val planStartDate: String? = null,

	@field:SerializedName("invoiceNo")
	val invoiceNo: String? = null,

	@field:SerializedName("planEndDate")
	val planEndDate: String? = null,

	@field:SerializedName("planAmount")
	val planAmount: Double? = null,

	@field:SerializedName("paymentStatus")
	val paymentStatus: String? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null,

	@field:SerializedName("noOfKits")
	val noOfKits: String? = null

)
