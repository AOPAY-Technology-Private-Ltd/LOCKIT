package com.bosandroidapp.aopaykit.data.enach

import com.google.gson.annotations.SerializedName

data class EMandateRequest(

	@field:SerializedName("CategoryID")
	val categoryID: Int? = null,

	@field:SerializedName("CollectionAmount")
	val collectionAmount: Int? = null,

	@field:SerializedName("CollectCollectionUntilCancle")
	val collectCollectionUntilCancle: Boolean? = null,

	@field:SerializedName("SeqType")
	val seqType: String? = null,

	@field:SerializedName("IFSCCode")
	val iFSCCode: String? = null,

	@field:SerializedName("Frequncy")
	val frequncy: String? = null,

	@field:SerializedName("RegistrationID")
	val registrationID: String? = null,

	@field:SerializedName("AccountHolderName")
	val accountHolderName: String? = null,

	@field:SerializedName("FinalCollectionDate")
	val finalCollectionDate: String? = null,

	@field:SerializedName("LoanNo")
	val loanNo: String? = null,

	@field:SerializedName("AccountType")
	val accountType: String? = null,

	@field:SerializedName("EmailAddress")
	val emailAddress: String? = null,

	@field:SerializedName("FirstCollectionDate")
	val firstCollectionDate: String? = null,

	@field:SerializedName("MobileNumber")
	val mobileNumber: String? = null,

	@field:SerializedName("BankAccountNumberConfirmation")
	val bankAccountNumberConfirmation: String? = null,

	@field:SerializedName("AddIn2")
	val addIn2: String? = null,

	@field:SerializedName("AddIn3")
	val addIn3: String? = null,

	@field:SerializedName("DebitType")
	val debitType: Boolean? = null,

	@field:SerializedName("TeleNumber")
	val teleNumber: String? = null,

	@field:SerializedName("authType")
	val authType: String? = null,

	@field:SerializedName("BankID")
	val bankID: Int? = null,

	@field:SerializedName("BankAccountNumber")
	val bankAccountNumber: String? = null,

	@field:SerializedName("clientCode")
	val clientCode: String? = null
)
