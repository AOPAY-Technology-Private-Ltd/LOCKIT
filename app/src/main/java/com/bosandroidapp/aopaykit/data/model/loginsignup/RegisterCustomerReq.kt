package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class RegisterCustomerReq(
    @SerializedName("mode")
    var mode:String,
    @SerializedName("firstName")
    var firstName:String,
    @SerializedName("middleName")
    var middleName:String,
    @SerializedName("lastName")
    var lastName:String,
    @SerializedName("primaryMobileNumber")
    var primaryMobileNumber:String,
    @SerializedName("primaryOTP")
    var primaryOTP:String,
    @SerializedName("primaryMobileVerified")
    var primaryMobileVerified:String,
    @SerializedName("alternateMobileNumber")
    var alternateMobileNumber:String,
    @SerializedName("alternateMobileOTP")
    var alternateMobileOTP:String,
    @SerializedName("pAlternateMobileVerified")
    var pAlternateMobileVerified:String,
    @SerializedName("eMailID")
    var eMailID:String,
    @SerializedName("flatNo")
    var flatNo:String,
    @SerializedName("aearSector")
    var aearSector:String,
    @SerializedName("pinCode")
    var pinCode:String,
    @SerializedName("currentAddress")
    var currentAddress:String,
    @SerializedName("stateName")
    var stateName:String,
    @SerializedName("cityName")
    var cityName:String,
    @SerializedName("custPhoto_path")
    var custPhoto_path:String ?,
    @SerializedName("country")
    var country:String ?,
    @SerializedName("aadharNumber")
    var aadharNumber:String,
    @SerializedName("aadharNumberVerified")
    var aadharNumberVerified:String,
    @SerializedName("panNumber")
    var panNumber:String,
    @SerializedName("panNumberVerified")
    var PanNumberVerified:String,
    @SerializedName("brandName")
    var brandName:String,
    @SerializedName("modelName")
    var modelName:String,
    @SerializedName("modelVariant")
    var modelVariant:String,
    @SerializedName("color")
    var color:String,
    @SerializedName("sellingPrice")
    var sellingPrice:String,
    @SerializedName("downPayment")
    var downPayment:String,
    @SerializedName("tenure")
    var tenure:String,
    @SerializedName("emiAmount")
    var emiAmount:String,
    @SerializedName("imeiNumber1")
    var imeiNumber1:String,
    @SerializedName("imeiNumber1_SealPhotoPath")
    var imeiNumber1_SealPhotoPath:String,
    @SerializedName("imeiNumber2")
    var imeiNumber2:String,
    @SerializedName("imeiNumber2_SealPhotoPath")
    var imeiNumber2_SealPhotoPath:String,
    @SerializedName("invoive_Path")
    var invoive_Path:String,
    @SerializedName("imeiNumber_PhotoPath")
    var imeiNumber_PhotoPath:String,
    @SerializedName("accountNumber")
    var accountNumber:String,
    @SerializedName("bankIFSCCode")
    var bankIFSCCode:String,
    @SerializedName("bankName")
    var bankName:String,
    @SerializedName("accountType")
    var accountType:String,
    @SerializedName("branchName")
    var branchName:String,
    @SerializedName("refName")
    var refName:String,
    @SerializedName("refRelationShip")
    var refRelationShip:String,
    @SerializedName("refmobileNo")
    var refmobileNo:String,
    @SerializedName("refAddress")
    var refAddress:String,
    @SerializedName("debitOrCreditCard")
    var debitOrCreditCard:String,
    @SerializedName("upiMandate")
    var upiMandate:String,
    @SerializedName("createdBy")
    var createdBy:String

)
