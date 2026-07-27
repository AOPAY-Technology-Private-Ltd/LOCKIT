package com.bosandroidapp.aopaykit.kioskmode

import android.app.Activity
import android.app.AppOpsManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bosandroidapp.aopaykit.R


lateinit var dpm: DevicePolicyManager
lateinit var admin: ComponentName

fun Activity.showExitPinDialog() {
    val view = layoutInflater.inflate(R.layout.dialog_pin, null)
   /* val input = view.findViewById<TextView>(R.id.etPin)

    AlertDialog.Builder(this)
        .setTitle("Exit Kiosk")
        .setView(view)
        .setPositiveButton("Unlock") { d, _ ->
            val pin = input.text?.toString()?.trim()
            if (pin == "1234") { // TODO: store securely
                stopLockSituation()
                Toast.makeText(this, "Unlocked", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Wrong PIN", Toast.LENGTH_SHORT).show()
            }
            d.dismiss()
        }
        .setNegativeButton("Cancel", null)
        .setCancelable(false)
        .show()*/
}


fun Context.isOverLay(): Boolean {
    return Settings.canDrawOverlays(this)
}



fun Activity.setAppasAdmin() {
    val compName = ComponentName(this, KioskDeviceAdminReceiver::class.java)
    val intent   = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).
    apply {
        putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
        putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Enable admin to protect app")
     }
     startActivityForResult(intent, 1)
}



fun Activity.setAppasAccessibility() {
    val intent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivityForResult(intent, 2)
}



fun isAccessibilityServiceEnabled(context: Context, service: Class<*>): Boolean {
    val serviceName = context.packageName + "/" + service.name
    try {
        val enabledServices = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)

        if (!TextUtils.isEmpty(enabledServices)) {
            val splitter = TextUtils.SimpleStringSplitter(':')
            splitter.setString(enabledServices)
            while (splitter.hasNext()) {
                val enabledService = splitter.next()
                if (enabledService.equals(serviceName, ignoreCase = true)) {
                    return true
                }
            }
        }
    }
    catch (e: Exception) {
        Log.e("AccessibilityUtil", "Error checking accessibility: ${e.message}")
    }
    return false
}



fun Context.isBatteryOptimized(): Boolean {
    val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
    val packageName = packageName

    return pm.isIgnoringBatteryOptimizations(packageName)
}



fun Activity.optimizeBatterry() {
    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
        data = Uri.parse("package:$packageName")
    }
    try {
        startActivityForResult(intent, 3)
    } catch (e: Exception) {
        // Fallback: open battery optimization settings
        val settingsIntent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        startActivityForResult(settingsIntent, 3)
    }
}



fun Activity.isStatsPermissionGranted():Boolean{
    val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        packageName
    )
    return (mode == AppOpsManager.MODE_ALLOWED)
}



fun Activity.askforUsagePermission(){
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    startActivityForResult(intent,4)
}