package com.bosandroidapp.aopaykit.kioskmode

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.app.admin.FactoryResetProtectionPolicy
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle

class KioskDeviceAdminReceiver : DeviceAdminReceiver(){

    var frpAccounts = arrayListOf("116164541526712076874")


    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        // Called when admin is enabled
    }


    override fun onTransferOwnershipComplete(context: Context, bundle: PersistableBundle?) {
        super.onTransferOwnershipComplete(context, bundle)


        // 🔥 This is where you set up kiosk mode
        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val admin = ComponentName(context, KioskDeviceAdminReceiver::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dpm.setFactoryResetProtectionPolicy(
                admin,
                FactoryResetProtectionPolicy.Builder()
                    .setFactoryResetProtectionAccounts(frpAccounts)
                    .setFactoryResetProtectionEnabled(true)
                    .build()
            )
        }

        // 🚀 Launch your kiosk activity
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

    }




}