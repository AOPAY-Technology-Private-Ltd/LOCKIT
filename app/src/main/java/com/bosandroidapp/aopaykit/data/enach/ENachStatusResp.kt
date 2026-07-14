package com.bosandroidapp.aopaykit.data.enach

import com.google.gson.annotations.SerializedName

data class ENachStatusResp(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("data")
	val data: EnachStatusData? = null,

	@field:SerializedName("Value")
	val value: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("StatusCode")
	val statusCode: String? = null
)

data class BankAccountsStatusItem(

	@field:SerializedName("ifsc_code")
	val ifscCode: String? = null,

	@field:SerializedName("auth_type")
	val authType: String? = null,

	@field:SerializedName("account_type")
	val accountType: String? = null,

	@field:SerializedName("account_holder_name")
	val accountHolderName: String? = null,

	@field:SerializedName("bank_account_no")
	val bankAccountNo: String? = null,

	@field:SerializedName("company_ifsc_code")
	val companyIfscCode: String? = null
)

data class EnachStatusData(

	@field:SerializedName("customer")
	val customer: Customer? = null
)

data class EnachStatusCustomer(

	@field:SerializedName("addnl5")
	val addnl5: Any? = null,

	@field:SerializedName("addnl3")
	val addnl3: String? = null,

	@field:SerializedName("addnl4")
	val addnl4: Any? = null,

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

	@field:SerializedName("reason_code")
	val reasonCode: Any? = null,

	@field:SerializedName("reject_by")
	val rejectBy: Any? = null,

	@field:SerializedName("submitted_on")
	val submittedOn: String? = null,

	@field:SerializedName("expires_at")
	val expiresAt: String? = null,

	@field:SerializedName("npci_ref_no")
	val npciRefNo: Any? = null,

	@field:SerializedName("tel_no")
	val telNo: String? = null,

	@field:SerializedName("accptd")
	val accptd: String? = null,

	@field:SerializedName("debit_type")
	val debitType: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("seq_tp")
	val seqTp: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("umrn")
	val umrn: Any? = null,

	@field:SerializedName("loan_no")
	val loanNo: String? = null,

	@field:SerializedName("colltn_amt")
	val colltnAmt: Int? = null,

	@field:SerializedName("accpt_ref_no")
	val accptRefNo: Any? = null,

	@field:SerializedName("reason_desc")
	val reasonDesc: Any? = null,

	@field:SerializedName("frst_colltn_dt")
	val frstColltnDt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("bank_accounts")
	val bankAccounts: List<BankAccountsItem?>? = null
)
