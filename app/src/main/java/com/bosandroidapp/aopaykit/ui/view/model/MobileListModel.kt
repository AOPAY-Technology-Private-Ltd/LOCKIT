package com.bosandroidapp.aopaykit.ui.view.model

data class MobileListModel(
    var MobileIcon:Int,
    var MobileName:String,
    var MobilePrice: String,
    var colorgradientList:MutableList<Int> = mutableListOf()
)

data class ColorList(
    var color:Int
)
