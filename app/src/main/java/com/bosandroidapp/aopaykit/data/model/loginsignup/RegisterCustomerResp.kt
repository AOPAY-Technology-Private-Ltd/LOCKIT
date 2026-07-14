package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class RegisterCustomerResp(@SerializedName("message")
                                val message: String = "",
                                @SerializedName("customerCode")
                                val customerCode: String = "",
                                @SerializedName("statuss")
                                val statuss: String = "",
                                @SerializedName("value")
                                val value: String = ""
)