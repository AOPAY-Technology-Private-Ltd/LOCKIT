package com.bosandroidapp.aopaykit.data.customeraction

import com.google.gson.annotations.SerializedName

data class GetPendingDeviceActionResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DataItem(

	@field:SerializedName("createdDate")
	val createdDate: String? = null,

	@field:SerializedName("actionStatus")
	val actionStatus: Boolean? = null,

	@field:SerializedName("selectedApps")
	val selectedApps: List<SelectedAppsItem?>? = null,

	@field:SerializedName("rid")
	val rid: Int? = null,

	@field:SerializedName("notificationName")
	val notificationName: String? = null,

	@field:SerializedName("notificationCode")
	val notificationCode: String? = null
)

data class SelectedAppsItem(

	@field:SerializedName("action")
	val action: String? = null,

	@field:SerializedName("packageName")
	val packageName: String? = null
)
