package com.bosandroidapp.aopaykit.data.model.loginsignup.verification

import com.google.gson.annotations.SerializedName

data class AadharVerificationResp(@SerializedName("Status")
                                  val status: String = "",
                                  @SerializedName("code")
                                  val code: String = "",
                                  @SerializedName("Value")
                                  val value: String = "",
                                  @SerializedName("model")
                                  val model: Model,
                                  @SerializedName("message")
                                  val message: String = "")

data class Model(@SerializedName("url")
                 val url: String = "",
                 @SerializedName("transactionId")
                 val transactionId: String = "",
                 @SerializedName("kycUrl")
                 val kycUrl: String = ""
    )