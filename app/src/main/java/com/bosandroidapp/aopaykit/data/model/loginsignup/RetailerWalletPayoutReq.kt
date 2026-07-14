package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class RetailerWalletPayoutReq(
    @SerializedName("registrationId")
    var registrationId:String ,

    @SerializedName("paymentMode")
    var paymentMode:String,

    @SerializedName("paymentDate")
    var paymentDate:String,

    @SerializedName("amount")
    var transferAmount:Double,

    @SerializedName("beneId")
    var beneId:String,

    @SerializedName("accountHolder")
    var accountHolder:String,

    @SerializedName("ifscCode")
    var ifscCode:String,

    @SerializedName("branchName")
    var branchName:String,

    @SerializedName("remarks")
    var remarks:String

)
