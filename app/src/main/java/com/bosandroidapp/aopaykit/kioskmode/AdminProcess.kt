package com.bosandroidapp.aopaykit.kioskmode

import android.accessibilityservice.AccessibilityService.MODE_PRIVATE
import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.app.admin.FactoryResetProtectionPolicy
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.UserManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.bosandroidapp.aopaykit.R

fun Context.imposeRestrictions(){
    dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    admin = ComponentName(this, KioskDeviceAdminReceiver::class.java)
    if (dpm.isDeviceOwnerApp(packageName)) {
        dpm.addUserRestriction(admin, UserManager.DISALLOW_FACTORY_RESET)
    }
}


@RequiresApi(Build.VERSION_CODES.R)
fun Context.removeRestrictions(){
    dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    admin = ComponentName(this, KioskDeviceAdminReceiver::class.java)
    if (dpm.isDeviceOwnerApp(packageName)) {
        val policy = FactoryResetProtectionPolicy.Builder()
            .setFactoryResetProtectionAccounts(emptyList())
            .build()

        dpm.setFactoryResetProtectionPolicy(
            admin,
            policy
        )
        dpm.clearUserRestriction(admin, UserManager.DISALLOW_FACTORY_RESET)
        dpm.clearDeviceOwnerApp(getPackageName())
        dpm.removeActiveAdmin(admin)
    }
}



fun Activity.setKioskMode() {
    dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    admin = ComponentName(this, KioskDeviceAdminReceiver::class.java)


    if (dpm.isDeviceOwnerApp(packageName)) {
        dpm.setLockTaskPackages(admin, arrayOf(packageName))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            dpm.setLockTaskFeatures(admin, 0)
            dpm.setStatusBarDisabled(admin, true)
        }
        dpm.addUserRestriction(admin, UserManager.DISALLOW_FACTORY_RESET)
        startLockTask()
    }


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && dpm.isDeviceOwnerApp(packageName)) {
        dpm.setKeyguardDisabled(admin, true)
    }

    // Apply kiosk policies if we are the Device Owner
    ensureKioskPolicies()

    // Enter immersive full screen
    enterImmersiveMode()

    // Try to start lock task
    startLockIfPermitted()

    removeAllGoogleAccounts()
}


fun Context.removeAllGoogleAccounts() {
    val accountManager = AccountManager.get(this)
    val googleAccounts: Array<Account> = accountManager.getAccountsByType("com.google")

    for (account in googleAccounts) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            accountManager.removeAccount(
                account,                null,   // No activity needed
                { future ->
                    try {
                        val result = future.result
                        // Success/failure result can be checked here
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                null
            )
        } else {
            @Suppress("DEPRECATION")
            accountManager.removeAccount(account, null, null)
        }
    }
}


fun Activity.addGoogleAccount() {
    val am = AccountManager.get(this)

    am.addAccount(
        "com.google",  // Google account type
        null,          // authTokenType
        null,          // requiredFeatures
        null,          // options
        this,      // activity for user interaction
        { future ->
            try {
                val bundle = future.result
                val accountName = bundle?.getString(AccountManager.KEY_ACCOUNT_NAME)
                println("Google account added: $accountName")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
        null
    )
}


fun Context.ensureKioskPolicies() {
    if (!dpm.isDeviceOwnerApp(packageName)) {
        Toast.makeText(this, "Not device owner. Lock task will require confirmation or may fail.", Toast.LENGTH_LONG).show()
        return
    }

    // Allowlist this package for Lock Task mode
    dpm.setLockTaskPackages(admin, arrayOf(packageName))

    // Tighten allowed features during lock task (Android 9+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        // 0 = no special features; blocks notifications, etc.
        dpm.setLockTaskFeatures(admin, 0)
    }

    // Optional: Hide status bar on fully managed devices (Android 9+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        try {
            dpm.setStatusBarDisabled(admin, true)
        } catch (_: SecurityException) {
        }
    }
}


fun Activity.startLockIfPermitted() {
    val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val inLockTask = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        am.lockTaskModeState != ActivityManager.LOCK_TASK_MODE_NONE
    } else false

    if (!inLockTask) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dpm.isLockTaskPermitted(packageName)) {
                startLockTask()
            } else {
                // Will throw on API 34 if not allowlisted
                try {
                    startLockTask()
                } catch (_: SecurityException) {
                    Toast.makeText(this, "Not allowlisted for lock task.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        else {
            try {
                startLockTask()
            } catch (_: SecurityException) {
            }
        }
    }
}


fun Activity.stopLockIfRunning() {

    val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    val admin = ComponentName(this, KioskDeviceAdminReceiver::class.java)
    val packageName = packageName // your app's package

         // Check if this app is device owner
    if (dpm.isDeviceOwnerApp(packageName)) {

        // Stop lock task mode
        stopLockTask()

        // Reset lock task packages (allow other apps to run normally)
        dpm.setLockTaskPackages(admin, arrayOf())


        // Re-enable status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            dpm.setStatusBarDisabled(admin, false)
            dpm.setLockTaskFeatures(admin, DevicePolicyManager.LOCK_TASK_FEATURE_NONE)
        }

        // Re-enable keyguard
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dpm.setKeyguardDisabled(admin, false)
        }

        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isLocked", false) // key: isLoggedIn, value: true
        editor.apply()

    }


}


fun Activity.enterImmersiveMode() {
    window.decorView.systemUiVisibility =
        (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
}


fun Activity.showExitPinAdminDialog() {
    val view = layoutInflater.inflate(R.layout.dialog_pin, null)
    val input = view.findViewById<TextView>(R.id.etPin)

    AlertDialog.Builder(this)
        .setTitle("Exit Kiosk")
        .setView(view)
        .setPositiveButton("Unlock") { d, _ ->
            val pin = input.text?.toString()?.trim()
            if (pin == "1234") { // TODO: store securely
                stopLockIfRunning()

                Toast.makeText(this, "Unlocked", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Wrong PIN", Toast.LENGTH_SHORT).show()
            }
            d.dismiss()
        }
        .setNegativeButton("Cancel", null)
        .setCancelable(false)
        .show()
}