package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class LogoutReq (
    @SerializedName("retailerCode")
    var retailerCode:String,
    @SerializedName("clientCode")
    var clientCode:String? = null

)
