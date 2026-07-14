package com.bosandroidapp.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore

import com.google.gson.annotations.SerializedName

data class CibilScoreReq(

    @SerializedName("FirstName")
    var firstName:String ,

    @SerializedName("LastName")
    var lastName: String ,

    @SerializedName("MobileNumber")
    var mobilenumber: String ,

    @SerializedName("DateOfBirth")
    var dob: String ,

    @SerializedName("EmailID")
    var mailid: String ,

    @SerializedName("PanNumber")
    var pannumber: String ,

    @SerializedName("OTP")
    var otp: String ,

    @SerializedName("ConsentMessage")
    var consentmessage: String,

    @SerializedName("ConsentAcceptance")
    var consentacceptence: String,

    @SerializedName("RegistrationID")
    var registrationID: String


)
