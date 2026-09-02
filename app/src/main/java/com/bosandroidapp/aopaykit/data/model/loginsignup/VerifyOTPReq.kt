package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class VerifyOTPReq(
    @SerializedName("mobileOrEmail")
    var mobileormailid:String,
    @SerializedName("enteredOTP")
    var otp:String,
    @SerializedName("login_Type")
    var logintype:String,
    @SerializedName("clientCode")
    var clientCode: String? = null
)
