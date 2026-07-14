package com.bosandroidapp.aopaykit.data.model.loginsignup.reports

import com.google.gson.annotations.SerializedName

data class ReportsResp(@SerializedName("data")
                       val data: List<com.bosandroidapp.aopaykit.data.model.loginsignup.reports.ReportsDataItem>?,
                       @SerializedName("message")
                       val message: String = "",
                       @SerializedName("status")
                       val status: String = "")