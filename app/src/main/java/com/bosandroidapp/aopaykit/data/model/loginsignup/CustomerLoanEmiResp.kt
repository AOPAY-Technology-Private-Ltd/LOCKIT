package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class CustomerLoanEmiResp(

	@field:SerializedName("globalTimeUTC")
	val globalTimeUTC: String? = null,

	@field:SerializedName("data")
	val data: List<CustomerDataItem?>? = null,

	@field:SerializedName("indiaTimeIST")
	val indiaTimeIST: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class CustomerDataItem(

	@field:SerializedName("gracePeriod")
	val gracePeriod: String? = null,

	@field:SerializedName("customerGracePeriod")
	val customerGracePeriod: String? = null,

	@field:SerializedName("endDate")
	val endDate: String? = null,

	@field:SerializedName("customerCode")
	val customerCode: String? = null,

	@field:SerializedName("applicableOtherCharge")
	val applicableOtherCharge: String? = null,

	@field:SerializedName("applicableLateFine")
	val lateFine: String? = null,

	@field:SerializedName("applicableWaiveOff")
	val applicableWaiveOff: String? = null,

	/*@field:SerializedName("emiAmount")
	val emiAmount: Double? = null,*/

	@field:SerializedName("nextDueDate")
	val nextDueDate: String? = null,

	@field:SerializedName("nextEMIAmount")
	val emiAmount: String? = null,

	@field:SerializedName("downPayment")
	val downPayment: Double? = null,

	@field:SerializedName("duesEMI")
	val duesEMI: String? = null,

	@field:SerializedName("value")
	val value: String? = null,

	@field:SerializedName("tenure")
	val tenure: Int? = null,

	/*@field:SerializedName("lateFine")
	val lateFine: String? = null,*/

	@field:SerializedName("avlbColors")
	val avlbColors: String? = null,

	@field:SerializedName("interestRate")
	val interestRate: Double? = null,

	@field:SerializedName("brandName")
	val brandName: String? = null,

	@field:SerializedName("paidEMI")
	val paidEMI: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("netDueAmount")
	val netDueAmount: String? = null,

	@field:SerializedName("statuss")
	val statuss: String? = null,

	@field:SerializedName("loanAmount")
	val loanAmount: Double? = null,

	@field:SerializedName("isEmandateVerified")
	val isEmandateVerified: String? = null,

	@field:SerializedName("modelName")
	val modelName: String? = null,

	@field:SerializedName("isPannydropVerified")
	val isPannydropVerified: String? = null,

	@field:SerializedName("loanCode")
	val loanCode: String? = null,

	@field:SerializedName("variantName")
	val variantName: String? = null,

	@field:SerializedName("applicableBounceCharge")
	val applicableBounceCharge: String? = null,

	@field:SerializedName("startDate")
	val startDate: String? = null
)
