package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class AddBankAccountReq(
    @SerializedName("action")
    var action : String ,

    @SerializedName("retailerID")
    var retailerID : String ,

    @SerializedName("accountNumber")
    var accountNumber : String ,

    @SerializedName("accountName")
    var accountName : String ,

    @SerializedName("bankName")
    var bankName : String ,

    @SerializedName("ifscCode")
    var ifscCode : String ,

    @SerializedName("branchName")
    var branchName : String ,

    @SerializedName("branchAddress")
    var branchAddress : String ,

    @SerializedName("mobileNumber")
    var mobilenumber : String ,

    @SerializedName("emailID")
    var emailID : String,
    @SerializedName("clientCode")
    var clientCode: String? = null
)
