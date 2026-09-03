package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class ProductListModelResponse(

	@field:SerializedName("data")
	val data: List<ProductDataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ProductDataItem(

	@field:SerializedName("modelName")
	val modelName: String? = null,

	@field:SerializedName("brandName")
	val brandName: String? = null,

	@field:SerializedName("imagePath")
	val imagePath: String? = null,

	@field:SerializedName("remark")
	val remark: String? = null,

	@field:SerializedName("rid")
	val rid: Int? = null,

	@field:SerializedName("variantName")
	val variantName: String? = null,

	@field:SerializedName("mrpPrice")
	val mrpPrice: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("statuss")
	val statuss: String? = null,

	@field:SerializedName("value")
	val value: String? = null,

	@field:SerializedName("avlbColors")
	val avlbColors: String? = null
)
