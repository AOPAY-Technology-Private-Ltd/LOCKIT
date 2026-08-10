package com.bosandroidapp.aopaykit.data.customeraction

import com.google.gson.annotations.SerializedName

data class SendInstalledAppOnServerRequest(

	@field:SerializedName("createdBy")
	val createdBy: String? = null,

	@field:SerializedName("categories")
	val categories: List<CategoriesItem?>? = null
)

data class CategoriesItem(

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("apps")
	val apps: List<AppsItem?>? = null
)

data class AppsItem(

	@field:SerializedName("appName")
	val appName: String? = null,

	@field:SerializedName("packageName")
	val packageName: String? = null
)
