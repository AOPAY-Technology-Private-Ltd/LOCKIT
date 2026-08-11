package com.bosandroidapp.aopaykit.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class RegistrationRes(@SerializedName("lastName")
                           val lastName: String? = "",
                           @SerializedName("address")
                           val address: String? = "",
                           @SerializedName("mobileNumber")
                           val mobileNumber: String? = "",
                           @SerializedName("customerCode")
                           val customerCode: String? = "",
                           @SerializedName("emailID")
                           val emailID: String? = "",
                           @SerializedName("panNumber")
                           val panNumber: String? = "",
                           @SerializedName("message")
                           val message: String = "",
                           @SerializedName("statuss")
                           val statuss: Boolean = false,
                           @SerializedName("firstName")
                           val firstName: String? = "",
                           @SerializedName("password")
                           val password: String? = "",
                           @SerializedName("aadharNumber")
                           val aadharNumber: String? = "",
                           @SerializedName("activeStatus")
                           val activeStatus: String? = "",
                           @SerializedName("value")
                           val value: String = "",
                           @SerializedName("mobileNo")
                           val mobileno: String? = "",
                           @SerializedName("retailerCode")
                           val retailerCode: String? = "",
                           @SerializedName("clientCode")
                           val clientCode: String? = "",
                           @SerializedName("profile_Photo")
                           val profilePhoto: String? = "",
                           @SerializedName("adhaar_front_photo")
                           val adhaarfront: String? = "",
                           @SerializedName("adhaar_back_Photo")
                           val adhaarback: String? = "",
                           @SerializedName("panCard_fornt_Photo")
                           val panCardfornt: String? = "",
                           @SerializedName("cancle_cheque_Photo")
                           val canclecheque: String? = "",
                           @SerializedName("store_front_Photo")
                           val storefront: String? = "",
                           @SerializedName("company_doc_Photo")
                           val companydoc: String? = ""

)