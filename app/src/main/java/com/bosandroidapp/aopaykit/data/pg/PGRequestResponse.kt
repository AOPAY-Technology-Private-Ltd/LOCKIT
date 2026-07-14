package com.bosandroidapp.aopaykit.data.pg

import com.google.gson.annotations.SerializedName

data class PGRequestResponse(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("PreparePOSTForm")
	val preparePOSTForm: String? = null,

	@field:SerializedName("Message")
	val message: String? = null,

	@field:SerializedName("PGOrderID")
	val pGOrderID: String? = null
)
