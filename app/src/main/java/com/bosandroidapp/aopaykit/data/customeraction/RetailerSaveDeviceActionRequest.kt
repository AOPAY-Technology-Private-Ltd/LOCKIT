package com.bosandroidapp.aopaykit.data.customeraction

import com.google.gson.annotations.SerializedName

data class RetailerSaveDeviceActionRequest(

	@field:SerializedName("NotificationCode")
	val notificationCode: String? = null,

	@field:SerializedName("SelectedApps")
	val selectedApps: List<RetailerSendNotificationToCustomer?>? = null,

	@field:SerializedName("CreatedBy")
	val createdBy: String? = null,

	@field:SerializedName("CustomerCode")
	val customerCode: String? = null,

	@field:SerializedName("ClientCode")
	val clientCode: String? = null,

	@field:SerializedName("RetailerCode")
	val retailerCode: String? = null,

	@field:SerializedName("ActionStatus")
	val actionStatus: Boolean? = null
)


data class RetailerSendNotificationToCustomer(
	@field:SerializedName("packageName")
	val packageName: String? = null,

	@field:SerializedName("actionStatus")
	val actionStatus: Boolean? = null

)
