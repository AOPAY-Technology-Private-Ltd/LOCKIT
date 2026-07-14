package com.bosandroidapp.aopaykit.kioskmode

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

fun Activity.initiateBlocking( checkEmiStatus: Boolean) {

    if (isDeviceAdmin()) {
        /*if (!isStatsPermissionGranted()) {
            askforUsagePermission()
            return
        }
*/
        if (!isAccessibilityServiceEnabled(this, MyAccessibilityService::class.java)) {
            setAppasAccessibility()
            return
        }

    }
    else if(checkEmiStatus){

        /*if (!isStatsPermissionGranted()) {
            askforUsagePermission()
            return
        }*/

        if (!isBatteryOptimized()) {
            optimizeBatterry()
            return
        }

        if (!isOverLay()) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, 1001)
            return
        }

        if (!isAdmin()) {
            setAppasAdmin()
            return
        }

        if (!isAccessibilityServiceEnabled(this, MyAccessibilityService::class.java)) {
            setAppasAccessibility()
            return
        }

    }


}



fun Activity.startBlocking() {
    startLockSituation()
    if (isDeviceAdmin()) setKioskMode()
}


fun Activity.stopBlocking() {
    if (isDeviceAdmin()) showExitPinAdminDialog()
    else showExitPinDialog()
}