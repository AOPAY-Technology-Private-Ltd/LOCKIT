package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class SmsResponse(@SerializedName("code")
                       val code: String = "",
                       @SerializedName("data")
                       val data: SMSData,
                       @SerializedName("description")
                       val description: String = "",
                       @SerializedName("status")
                       val status: String = "")

data class SMSData(@SerializedName("totnumber")
                val totnumber: Int = 0,
                @SerializedName("campid")
                val campid: String = "",
                @SerializedName("messageid")
                val messageid: String = "",
                @SerializedName("totalcredit")
                val totalcredit: Int = 0)