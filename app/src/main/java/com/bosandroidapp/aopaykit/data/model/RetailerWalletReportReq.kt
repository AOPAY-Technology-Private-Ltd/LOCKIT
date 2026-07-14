package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class RetailerWalletReportReq(
    @SerializedName("retailerID")
    var retailerID : String ,

    @SerializedName("reportType")
    var reportType : String ,

    @SerializedName("fromDate")
    var fromDate : String ? = null,

    @SerializedName("toDate")
    var toDate : String ? = null,

    )
