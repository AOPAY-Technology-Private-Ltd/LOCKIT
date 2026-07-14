package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class EmiSplitRes(@SerializedName("data")
                       val data: MutableList<DataItems>?,
                       @SerializedName("message")
                       val message: String = "",
                       @SerializedName("status")
                       val status: String = "")


data class DataItems(@SerializedName("processingFees")
                    val processingFees: String = "",
                    @SerializedName("interestPerc")
                    val interestPerc: String = "",
                    @SerializedName("rid")
                    val rid: String = "",
                    @SerializedName("message")
                    val message: String = "",
                    @SerializedName("statuss")
                    val statuss: String = "",
                    @SerializedName("value")
                    val value: String = "",
                    @SerializedName("downPaymentPerc")
                    val downPaymentPerc: String = "",
                    @SerializedName("tenure")
                    val tenure: String = "",
                     @SerializedName("sellingPrice")
                     val sellingPrice: String = "",
                     @SerializedName("variantName")
                     val variantName: String = "",
                     @SerializedName("mrpPrice")
                     val mrpPrice: String = "",
                     @SerializedName("avlbColors")
                     val avlbColors: String = "")