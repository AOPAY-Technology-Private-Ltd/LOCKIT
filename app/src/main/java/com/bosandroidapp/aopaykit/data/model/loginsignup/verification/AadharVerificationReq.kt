package com.bosandroidapp.aopaykit.data.model.loginsignup.verification

import com.google.gson.annotations.SerializedName

data class AadharVerificationReq(
    @SerializedName("FirstName")
    var firstName:String,

    @SerializedName("LastName")
    var lastName:String,

    @SerializedName("MobileNumber")
    var mobileNumber:String,

    @SerializedName("EmailID")
    var emailId:String,

    @SerializedName("RegistrationID")
    var registrationId:String,
)
