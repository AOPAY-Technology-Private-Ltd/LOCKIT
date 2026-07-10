package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName


data class AdminBankDetailsResp(

	@field:SerializedName("data")
	val data: MutableList<AdminBankDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null

)


data class AdminBankDataItem(

	@field:SerializedName("upi_intend")
	val upiIntend: String? = null,

	@field:SerializedName("accountName")
	val accountName: String? = null,

	@field:SerializedName("branchName")
	val branchName: String? = null,

	@field:SerializedName("branchAddress")
	val branchAddress: String? = null,

	@field:SerializedName("bankName")
	val bankName: String? = null,

	@field:SerializedName("rid")
	val rid: Int? = null,

	@field:SerializedName("accountNumber")
	val accountNumber: String? = null,

	@field:SerializedName("qrCode_Path")
	val qrCodePath: String? = null,

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("activeStatus")
	val activeStatus: String? = null,

	@field:SerializedName("createdBy")
	val createdBy: String? = null,

	@field:SerializedName("modifiedDate")
	val modifiedDate: Any? = null,

	@field:SerializedName("modifiedBy")
	val modifiedBy: Any? = null,

	@field:SerializedName("ifscCode")
	val ifscCode: String? = null
)
