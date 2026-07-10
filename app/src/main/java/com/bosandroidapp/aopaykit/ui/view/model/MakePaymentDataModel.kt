package com.bosandroidapp.aopaykit.ui.view.model

data class MakePaymentDataModel(
    var loanCode : String,
    var tenure : Int,
    var emiAmount : Double,
    var paid : String,
    var due : String,
    var startDate : String,
    var allgracePeriod : String,
    var customergracePeriod : String,
    var latefine : String
)
