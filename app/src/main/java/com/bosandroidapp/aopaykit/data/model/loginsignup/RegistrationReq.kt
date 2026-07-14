package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName
import java.io.File

data class RegistrationReq(
    @SerializedName("firstName")
    var firstName:String,

    @SerializedName("lastName")
    var lastName:String,

    @SerializedName("mobileNumber")
    var mobileNumber:String,

    @SerializedName("emailID")
    var emailId:String,

    @SerializedName("password")
    var password:String,

    @SerializedName("confirmPassword")
    var confrmpassword:String,

    @SerializedName("address")
    var address:String,

    @SerializedName("aadharNumber")
    var aadharnumber:String,

    @SerializedName("panNumber")
    var panNumber:String,

    @SerializedName("storeName")
    var storeName:String,

    @SerializedName("storeAddress")
    var storeAddress:String,

    val profilePhoto: File?,

    val aadhaarfront: File?,

    val aadhaarback: File?,

    val pancardfront: File?,

    val cancelcheque: File?,

    val storefront: File?,

    val companydoc: File?
)
