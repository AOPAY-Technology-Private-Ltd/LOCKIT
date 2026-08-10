package com.bosandroidapp.aopaykit.data

import com.google.gson.annotations.SerializedName

data class CustomerInstalledAppListResponse(

	@field:SerializedName("categories")
	val categories: MutableList<CategoriesItem?>? = null
)

data class SubactionListItem(

	@field:SerializedName("subactionName")
	val subactionName: String? = null,

	@field:SerializedName("active")
	var active: Boolean? = null,

	@field:SerializedName("packageName")
	val packageName: String? = null
)

data class CategoriesItem(

	@field:SerializedName("subactionList")
	val subactionList: List<SubactionListItem?>? = null,

	@field:SerializedName("check")
	var check: Boolean ? = null,

	@field:SerializedName("notificationCode")
	val notificationCode: String? = null,

	@field:SerializedName("actionName")
	val actionName: String? = null
)
