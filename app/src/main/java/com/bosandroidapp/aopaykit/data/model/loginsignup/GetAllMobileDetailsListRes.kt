package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class GetAllMobileDetailsListRes(@SerializedName("data")
                                      val data: MutableList<DataItem>?,
                                      @SerializedName("message")
                                      val message: String = "",
                                      @SerializedName("status")
                                      val status: String = "")


data class DataItem(@SerializedName("modelName")
                    val modelName: String = "",
                    @SerializedName("brandName")
                    val brandName: String = "",
                    @SerializedName("mrpPrice")
                    val mrpPrice: String = "",
                    @SerializedName("imagePath")
                    val imagePath: String = "",
                    @SerializedName("remark")
                    val remark: String = "",
                    @SerializedName("rid")
                    val rid: Int = 0,
                    @SerializedName("variantName")
                    val variantName: String = "",
                    @SerializedName("message")
                    val message: String = "",
                    @SerializedName("statuss")
                    val statuss: String = "",
                    @SerializedName("value")
                    val value: String = "",
                    @SerializedName("avlbColors")
                    val avlbColors: String = "")