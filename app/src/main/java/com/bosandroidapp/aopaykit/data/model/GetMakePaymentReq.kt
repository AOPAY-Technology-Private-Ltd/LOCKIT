package com.bosandroidapp.aopaykit.data.model

import com.google.gson.annotations.SerializedName


data class GetMakePaymentReq(
    @SerializedName("RegistrationId"  ) var RegistrationId: String? = null,
    @SerializedName("Amount"          ) var Amount: String? = null,
    @SerializedName("DepositBankName" ) var DepositBankName: String? = null,
    @SerializedName("PaymentMode"     ) var PaymentMode: String? = null,
    @SerializedName("TransactionID"   ) var TransactionID: String? = null,
    @SerializedName("DcoumentPath"    ) var DcoumentPath: String? = null,
    @SerializedName("Remarks"         ) var Remarks: String? = null,
    @SerializedName("CompanyCode"     ) var CompanyCode: String? = null

)