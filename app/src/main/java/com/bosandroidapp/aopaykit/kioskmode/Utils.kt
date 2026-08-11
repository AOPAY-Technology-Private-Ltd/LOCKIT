package com.bosandroidapp.aopaykit.kioskmode

import android.accessibilityservice.AccessibilityService.MODE_PRIVATE
import android.app.admin.DevicePolicyManager
import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

fun Context.isLocked(): Boolean {
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    return sharedPref.getBoolean("isLocked", false)
}

// for logout condition
fun Context.isEMIsCompleted(): Boolean {
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    return sharedPref.getBoolean("isEMIsCompleted", true)
}

fun Context.stopLockSituation() {
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putBoolean("isLocked", false) // key: isLoggedIn, value: true
    editor.apply()
    try {
        (this as KioskActivity).finish()
    }catch (e:Exception){
    }
}

fun Context.startLockSituation() {
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putBoolean("isLocked", true) // key: isLoggedIn, value: true
    editor.apply()
}

fun Context.setEMICompleted() {
    removeRestrictions()
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putBoolean("isEMIsCompleted", true) // key: isLoggedIn, value: true
    editor.apply()
}

fun Context.setEMINotCompleted() {
    imposeRestrictions()
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putBoolean("isEMIsCompleted", false) // key: isLoggedIn, value: true
    editor.apply()
}

fun Context.startInternetAlertSituation() {
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putBoolean("isInternet", true) // key: isLoggedIn, value: true
    editor.apply()
}

fun Context.stopInternetAlertSituation() {
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val editor = sharedPref.edit()
    editor.putBoolean("isInternet", false) // key: isLoggedIn, value: true
    editor.apply()
}


fun Context.isInternetAlertSituationCompleted(): Boolean {
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    return sharedPref.getBoolean("isInternet", true)
}


fun Context.showToast(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}

fun Context.isAdmin():Boolean{
    val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val componentName = ComponentName(this, KioskDeviceAdminReceiver::class.java)
    return dpm.isAdminActive(componentName)
}


fun Context.isDeviceAdmin():Boolean{
    val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    return dpm.isDeviceOwnerApp(packageName)
}

fun Context.saveToken(data: ByteArray) {
    val prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val encoded = Base64.encodeToString(data, Base64.DEFAULT)
    prefs.edit().putString("token", encoded).apply()
}

fun Context.getByteArray(): ByteArray? {
    val prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val encoded = prefs.getString("token", null) ?: return null
    return Base64.decode(encoded, Base64.DEFAULT)
}


fun RecyclerView.fillApps(context: Context,apps:List<PaymentApp>){
    val adapter = AppAdapter(context, apps)
    this.adapter = adapter
}


fun String.isAllowedApp():Boolean{
    val list= arrayListOf<String>().apply {
        this.add("com.google.android.apps.nbu.paisa.user")
        this.add("com.phonepe.app")
        this.add("net.one97.paytm")
        this.add("in.org.npci.upiapp")
        this.add("com.mobikwik_new")
        this.add("com.freecharge.mobile")
        this.add("com.icici.pockets")
        this.add("com.axis.axispay")
        this.add("com.hdfcbank.payzapp")
        this.add("sbi.mobile.apps.in")
        this.add("in.amazon.mShop.android.shopping")
    }

    return list.contains(this)
}


 fun Context.isPaymentAppRunning(): Boolean {
     return getForegroundApp().isAllowedApp()
}


fun Context.getForegroundApp(): String {
    val usm = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val time = System.currentTimeMillis()
    // get app usage for last 5 seconds
    val appList = usm.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        time - 60000,
        time
    )
    if (appList != null && appList.isNotEmpty()) {
        val sorted = appList.sortedByDescending { it.lastTimeUsed }
        Log.d("Accessibility",sorted[0].packageName)
        return sorted[0].packageName
    }
    Log.d("Accessibility","No App")
    return ""
}