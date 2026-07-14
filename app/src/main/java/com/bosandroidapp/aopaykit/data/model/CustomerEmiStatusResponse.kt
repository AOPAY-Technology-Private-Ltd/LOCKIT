package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class CustomerEmiStatusResponse(

	@field:SerializedName("data")
	val data: List<CustomerEMIDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class CustomerEMIDataItem(

	@field:SerializedName("paymentMode")
	val paymentMode: String? = null,

	@field:SerializedName("otherCharges")
	val otherCharges: Double? = null,

	@field:SerializedName("waiveOffAmount")
	val waiveOffAmount: Double? = null,

	@field:SerializedName("bouncingCharges")
	val bouncingCharges: Double? = null,

	@field:SerializedName("netDueAmount")
	val netDueAmount: Double? = null,

	@field:SerializedName("emiStatus")
	val emiStatus: String? = null,

	@field:SerializedName("recordStatus")
	val recordStatus: String? = null,

	@field:SerializedName("emiAmount")
	val emiAmount: Double? = null,

	@field:SerializedName("srNo")
	val srNo: Int? = null,

	@field:SerializedName("emI_DueDate")
	val emIDueDate: String? = null,

	@field:SerializedName("fine")
	val fine: Double? = null,

	@field:SerializedName("receiptNo")
	val receiptNo: String? = null,

	@field:SerializedName("overdueDays")
	val overdueDays: Int? = null,

	@field:SerializedName("totalCharges")
	val totalCharges: Any? = null,

	@field:SerializedName("paymentDate")
	val paymentDate: String? = null,

	@field:SerializedName("pendingAmount")
	val pendingAmount: Double? = null
)
