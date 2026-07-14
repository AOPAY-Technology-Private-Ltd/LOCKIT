package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class GetIsEligibleLoanReq(
    @SerializedName("panNumber")
    var panNumber:String,

    @SerializedName("aadharNumber")
    var aadharNumber : String

)