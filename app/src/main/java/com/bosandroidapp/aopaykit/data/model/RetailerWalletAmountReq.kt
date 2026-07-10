package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class RetailerWalletAmountReq(
    @SerializedName("retailerID")
    var retailerID: String,

    @SerializedName("amountType")
    var amountType: String

)
