package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class RetailerProfileReq(
    @SerializedName("mode")
    var mode : String ,
    @SerializedName("customerType")
    var customerType : String ,
    @SerializedName("customerCode")
    var customerCode : String ,
    @SerializedName("firstName")
    var firstName : String ,
    @SerializedName("lastName")
    var lastName : String ,
    @SerializedName("mobileNo")
    var mobileNo : String ,
    @SerializedName("emailid")
    var emailid : String ,
    @SerializedName("address")
    var address : String ,
    @SerializedName("aadharNumber")
    var aadharNumber : String ,
    @SerializedName("panNumber")
    var panNumber : String ,
    @SerializedName("activeStatus")
    var activeStatus : String

)
