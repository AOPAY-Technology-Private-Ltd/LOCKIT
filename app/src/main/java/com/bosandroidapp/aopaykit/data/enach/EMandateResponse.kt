package com.bosandroidapp.aopaykit.data.enach

import com.google.gson.annotations.SerializedName

data class BankAccountsItem(

	@field:SerializedName("ifsc_code")
	val ifscCode: String? = null,

	@field:SerializedName("pancard")
	val pancard: Any? = null,

	@field:SerializedName("auth_type")
	val authType: String? = null,

	@field:SerializedName("account_type")
	val accountType: String? = null,

	@field:SerializedName("account_holder_name")
	val accountHolderName: String? = null,

	@field:SerializedName("bank_account_no")
	val bankAccountNo: String? = null,

	@field:SerializedName("company_ifsc_code")
	val companyIfscCode: Any? = null
)

data class EMandateResponse(

	@field:SerializedName("StatusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("StatusCode")
	val statusCode: String? = null,

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("Value")
	val Value: String? = null,
)

data class Data(

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("customer")
	val customer: Customer? = null,

	@field:SerializedName("notice")
	val notice: String? = null
)

data class Customer(

	@field:SerializedName("addnl5")
	val addnl5: Any? = null,

	@field:SerializedName("addnl3")
	val addnl3: String? = null,

	@field:SerializedName("addnl4")
	val addnl4: Any? = null,

	@field:SerializedName("loan_no")
	val loanNo: String? = null,

	@field:SerializedName("addnl2")
	val addnl2: String? = null,

	@field:SerializedName("colltn_until_cncl")
	val colltnUntilCncl: Boolean? = null,

	@field:SerializedName("mobile_no")
	val mobileNo: String? = null,

	@field:SerializedName("frqcy")
	val frqcy: String? = null,

	@field:SerializedName("fnl_colltn_dt")
	val fnlColltnDt: String? = null,

	@field:SerializedName("colltn_amt")
	val colltnAmt: Int? = null,

	@field:SerializedName("submitted_on")
	val submittedOn: String? = null,

	@field:SerializedName("expires_at")
	val expiresAt: String? = null,

	@field:SerializedName("frst_colltn_dt")
	val frstColltnDt: String? = null,

	@field:SerializedName("tel_no")
	val telNo: Any? = null,

	@field:SerializedName("accptd")
	val accptd: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("debit_type")
	val debitType: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("bank_accounts")
	val bankAccounts: List<BankAccountsItem?>? = null,

	@field:SerializedName("nupay_ref_no")
	val nupayRefNo: String? = null,

	@field:SerializedName("seq_tp")
	val seqTp: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
