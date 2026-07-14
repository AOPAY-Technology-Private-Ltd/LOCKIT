package com.bosandroidapp.aopaykit.data.pennydrop

import com.google.gson.annotations.SerializedName

data class BankListResponse(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("StatusDesc")
	val statusDesc: String? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("Value")
	val value: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("StatusCode")
	val statusCode: String? = null
)

data class BanksItem(

	@field:SerializedName("mode")
	val mode: String? = null,

	@field:SerializedName("bank_code")
	val bankCode: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)


data class Data(
	@field:SerializedName("banks")
	val banks: List<BanksItem?>? = null
)
