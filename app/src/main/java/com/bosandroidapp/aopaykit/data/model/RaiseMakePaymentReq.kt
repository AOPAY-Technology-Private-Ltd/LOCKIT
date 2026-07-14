package com.bosandroidapp.aopaykit.data.model

import java.io.File

data class RaiseMakePaymentReq(
    val RetailerCode: String,
    val RequestAmount: String,
    val PaymentMode: String,
    val BankName: String,
    val AccountHolderName: String,
    val AccountNumber: String,
    val IFSCCode: String,
    val UTRNumber: String,
    val UPIID: String,
    val Remarks: String,
    val ApprovedRemarks: String,
    val CreatedBy: String,
    val RecordStatus: String,
    val ActiveStatus: String,
    val imagefile1: File ?
)









