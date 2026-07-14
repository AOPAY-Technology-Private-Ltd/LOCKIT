package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class LoginReq (
 @SerializedName("mobileOrEmailID")
 var mobileormailid:String,
 @SerializedName("password")
 var password:String,
 @SerializedName("login_Type")
 var logintype:String,
 @SerializedName("deviceId")
 var deviceId:String,
 @SerializedName("token")
 var token:String
)



