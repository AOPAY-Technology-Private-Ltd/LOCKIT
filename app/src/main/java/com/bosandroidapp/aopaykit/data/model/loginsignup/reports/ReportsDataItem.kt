package com.bosandroidapp.aopaykit.data.model.loginsignup.reports

import com.google.gson.annotations.SerializedName


data class ReportsDataItem(
    @SerializedName("interestRate")
    val interestRate: Double = 0.0,
    @SerializedName("paidEmi")
    val paidEmi: Int = 0,
    @SerializedName("customerCode")
    val customerCode: String = "",
    @SerializedName("productDetails")
    val productDetails: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("statuss")
    val statuss: String = "",
    @SerializedName("loanAmount")
    val loanAmount: Double = 0.0,
    @SerializedName("customerName")
    val customerName: String = "",
    @SerializedName("emiAmount")
    val emiAmount: Double = 0.0,
    @SerializedName("downPayment")
    val downPayment: Double = 0.0,
    @SerializedName("retailerName")
    val retailerName: String = "",
    @SerializedName("duesEmi")
    val duesEmi: Int = 0,
    @SerializedName("retailerCode")
    val retailerCode: String = "",
    @SerializedName("loanCode")
    val loanCode: String = "",
    @SerializedName("value")
    val value: String = "",
    @SerializedName("tenure")
    val tenure: Int = 0,
    @SerializedName("custMobileNo")
    val custerMob: String = "",
    @SerializedName("dueDate")
    val dueDate: String = "",
    @SerializedName("customerImagePath")
    val customerImage: String = "",
    @SerializedName("recordStatus")
    val recordStatus: String = "",
    @SerializedName("startDate")
    val startDate: String = "",
    @SerializedName("endDate")
    val endDate: String = ""

)