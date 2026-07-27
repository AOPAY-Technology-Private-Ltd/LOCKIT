package com.bosandroidapp.aopaykit.data.customeraction

import com.google.gson.annotations.SerializedName

data class UpdateCustomerDeviceActionRequest(
	@field:SerializedName("rid")
	val rid: Int? = null,

	@field:SerializedName("updatedBy")
	val updatedBy: String? = null,

	@field:SerializedName("executionStatus")
	val executionStatus: String? = null,

	@field:SerializedName("failureReason")
	val failureReason: String? = null,


	@field:SerializedName("devicePin")
	val devicePin: String? = null,


	@field:SerializedName("iccid")
	val iccid: String? = null,


	@field:SerializedName("subscriptionId")
	val subscriptionId: Int? = null,


	@field:SerializedName("carrierName")
	val carrierName: String? = null,


	@field:SerializedName("mcc")
	val mcc: String? = null,


	@field:SerializedName("mnc")
	val mnc: String? = null,


	@field:SerializedName("slotIndex")
	val slotIndex: Int? = null


)
