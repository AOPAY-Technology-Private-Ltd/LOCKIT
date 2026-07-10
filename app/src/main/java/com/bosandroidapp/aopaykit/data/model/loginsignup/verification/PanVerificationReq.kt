package com.bosandroidapp.aopaykit.data.model.loginsignup.verification

import com.google.gson.annotations.SerializedName

data class PanVerificationReq(
    @SerializedName("PanNumber")
    var panNumber:String ,
    @SerializedName("RegistrationID")
    var registrationId:String
)



/*@SerializedName("FirstName")
var firstName:String,*/
