package com.bosandroidapp.aopaykit.data.model.loginsignup

/*data class CustomerLoanEmiReceiveReq(
    @SerializedName("mode")
    var mode:String,

    @SerializedName("rid")
    var rid:Int,

    @SerializedName("loanCode")
    var loanCode:String,

    @SerializedName("emiAmount")
    var emiAmount:String,

    @SerializedName("paymentDate")
    var paymentDate:String,

    @SerializedName("paidAmount")
    var paidAmount:String,

    @SerializedName("paymentMode")
    var paymentMode:String,

    @SerializedName("utrNumber")
    var utrNumber:String,

    @SerializedName("remarks")
    var remarks:String,

    @SerializedName("createdBy")
    var creatdeBy:String,

    @SerializedName("receiptNo")
    var receiptNo:String,

    @SerializedName("customercode")
    var customerCode:String,

    @SerializedName("retailerCode")
    var retailercode:String,

    @SerializedName("fine")
    var fine:Double,

    @SerializedName("interestAmt")
    var interestAmt:Double,

)*/

data class CustomerLoanEmiReceiveReq(
    val mode: String,
    /* val rid: String,*/
    val loanCode: String,
    val paymentDate: String,
    val paymentMode: String,
    val utrNumber: String,
    val remarks: String,
    val createdBy: String,
    val receiptNo: String,
    val customerCode: String,
    val retailerCode: String,
    /*val fine: String,
    val bounceCharge: String,
    val otherCharge: String,
    val waiveOff: String,
    val netDueAmount: String,*/
    val bankName: String,
    val receiptImagePath: String,
    /*val imageFile: File?*/ // optional image file
)

