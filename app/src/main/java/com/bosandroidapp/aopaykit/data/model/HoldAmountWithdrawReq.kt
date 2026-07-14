package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName

data class HoldAmountWithdrawReq(
    @SerializedName("retailerID")
    var retailerID: String,

    @SerializedName("amount")
    var amount: String,


    @SerializedName("remarks")
    var remarks: String,

)
