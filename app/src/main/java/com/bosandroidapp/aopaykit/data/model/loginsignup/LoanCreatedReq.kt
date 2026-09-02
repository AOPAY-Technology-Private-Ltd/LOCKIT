package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class LoanCreatedReq (
    @SerializedName("mode")
    var modetype:String,
    
    @SerializedName("rid")
    var rid:Int,

    @SerializedName("customerCode")
    var customerCode:String,

    @SerializedName("loanAmount")
    var loanAmount:Double,

    @SerializedName("downPayment")
    var downPayment:Double,

    @SerializedName("emiAmount")
    var emiAmount:Double,

    @SerializedName("tenure")
    var tenure:Int,

    @SerializedName("interestRate")
    var interestRate:Double,

    @SerializedName("startDate")
    var startDate:String,

    @SerializedName("endDate")
    var endDate:String,

    @SerializedName("imeiNumber")
    var imeiNumber:String,

    @SerializedName("createdBy")
    var createdBy:String,

    @SerializedName("brandName")
    var brandname:String,

    @SerializedName("modelName")
    var modelname:String,

    @SerializedName("variantName")
    var variantname:String,

    @SerializedName("avlbColors")
    var avlcolor:String,

    @SerializedName("retailerCode")
    var retailerCode:String,

    @SerializedName("processingFees")
    var processingFees:String,

    @SerializedName("interestAmt")
    var interestAmt:String,

    @SerializedName("remarks")
    var remarks:String,

    @SerializedName("recordStatus")
    var recordStatus:String,

    @SerializedName("creditScore")
    var creditScore:String,

    @SerializedName("validatekey")
    var validateKey :String,

    @SerializedName("defaulterEmiDebit")
    var defaultEmidebit :String,

    @SerializedName("sellingPrice")
    var sellingPrice : Double,

    @SerializedName("loanmode")
    var loanMode :String,
    @SerializedName("clientCode")
    var clientCode: String? = null
)
