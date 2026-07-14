package com.bosandroidapp.aopaykit.kioskmode

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

data class PaymentApp(
    val appName: String,
    val packageName: String,
    val icon: Drawable
)

fun Activity.getPaymentApps(): List<PaymentApp> {
    val pm = packageManager
    val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
    val paymentKeywords = listOf("pay", "wallet", "bank", "upi", "card")
    val paymentApps = apps.filter { app ->
        val name = pm.getApplicationLabel(app).toString().lowercase()
        paymentKeywords.any { keyword -> name.contains(keyword) }
    }
    val paymentAppList = paymentApps.filter { app ->
        app.packageName.isAllowedApp()
    }.map { app ->
        PaymentApp(
            appName = pm.getApplicationLabel(app).toString(),
            packageName = app.packageName,
            icon = pm.getApplicationIcon(app)
        )
    }
    return paymentAppList
}