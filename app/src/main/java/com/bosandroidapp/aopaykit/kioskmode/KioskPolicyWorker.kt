package com.bosandroidapp.aopaykit.kioskmode

import android.app.admin.DevicePolicyManager
import android.app.admin.FactoryResetProtectionPolicy
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.UserManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bosandroidapp.aopaykit.constant.ConstantClass
import org.json.JSONObject
import java.security.SecureRandom

import android.content.Intent
import android.content.pm.PackageManager
import android.util.Base64
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsClient.getPackageName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CheckCompleteEmiStatus
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.google.android.datatransport.runtime.scheduling.persistence.EventStoreModule_PackageNameFactory.packageName

class KioskPolicyWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    private val preference: SharedPreference by lazy { SharedPreference.getInstance(applicationContext)!! }

    @RequiresApi(Build.VERSION_CODES.R)
    override suspend fun doWork(): Result {
        val payload = inputData.getString(KEY_PAYLOAD) ?: return Result.failure()
        Toast.makeText(applicationContext, "Payload = $payload", Toast.LENGTH_SHORT).show()

        return try {
            val dpm = applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

            val admin = ComponentName(applicationContext, KioskDeviceAdminReceiver::class.java)

            if (!dpm.isDeviceOwnerApp(applicationContext.packageName) || !dpm.isAdminActive(admin)) {
                Log.e(TAG, "DPC / Device Owner is not active")
                return Result.failure()
            }
            val json = JSONObject(payload)

            if (json.optString(ConstantClass.NotificationCode) == ConstantClass.SocialApps ||
                json.optString(ConstantClass.NotificationCode) == ConstantClass.GamingApps||
                json.optString(ConstantClass.NotificationCode) == ConstantClass.UPIApps) {
                handleSocialApps(dpm, admin, json)
            }

            when(json.optString(ConstantClass.NotificationCode)){

                ConstantClass.Lock -> {

                    val tokenString = preference.getStringValue(ConstantClass.SecretKey,"")
                    val customerCode = preference.getStringValue(ConstantClass.CustomerCode,"")

                    val resetToken = Base64.decode(tokenString, Base64.DEFAULT)

                    val changed =  dpm.resetPasswordWithToken(admin, "123456", resetToken, 0)

                    if (changed) {
                        dpm.lockNow()
                    }
                    dpm.lockNow()
                }

                ConstantClass.UnLock -> {
                    val resetToken = ByteArray(32).also {
                        SecureRandom().nextBytes(it)
                    }

                    dpm.resetPasswordWithToken(
                        admin,
                        "",       // obtain securely; never hard-code
                        resetToken,
                        0
                    )

                }


            }


            /*when (action) {
                "DISABLE_CAMERA" ->
                    dpm.setCameraDisabled(admin, true)

                "ENABLE_CAMERA" ->
                    dpm.setCameraDisabled(admin, false)

                "ENABLE_WIFI" ->
                    dpm.clearUserRestriction(admin, UserManager.DISALLOW_CONFIG_WIFI)

                "DISABLE_WIFI" ->
                    dpm.addUserRestriction(admin, UserManager.DISALLOW_CONFIG_WIFI)

                "ENABLE_AIRPLANE_MODE" ->
                    dpm.clearUserRestriction(admin, UserManager.DISALLOW_AIRPLANE_MODE)

                "DISABLE_AIRPLANE_MODE" ->
                    dpm.addUserRestriction(admin, UserManager.DISALLOW_AIRPLANE_MODE)

                "DISABLE_USB" ->
                    dpm.addUserRestriction(admin, UserManager.DISALLOW_USB_FILE_TRANSFER)

                "ENABLE_USB" ->
                    dpm.clearUserRestriction(admin, UserManager.DISALLOW_USB_FILE_TRANSFER)

                "ENABLE_BLUETOOTH" ->
                    dpm.clearUserRestriction(admin, UserManager.DISALLOW_BLUETOOTH)

                "DISABLE_BLUETOOTH" ->
                    dpm.addUserRestriction(admin, UserManager.DISALLOW_BLUETOOTH)

                "APP_HIDE" -> {
                    val targetPackage = json.optString("PackageName")
                    if (targetPackage.isNotBlank()) {
                        dpm.setApplicationHidden(admin, targetPackage, true)
                    }
                }

                "APP_SHOW" -> {
                    val targetPackage = json.optString("PackageName")
                    if (targetPackage.isNotBlank()) {
                        dpm.setApplicationHidden(admin, targetPackage, false)
                    }
                }

                "REBOOT" ->
                    dpm.reboot(admin)

                else -> {
                    handleSocialApps(dpm, admin, json)
                }
            }*/

            // Call your backend here with APPLIED status.
            Log.d(TAG, "Command applied: $payload")
            Result.success()

        } catch (e: SecurityException) {
            Log.e(TAG, "DPC permission error", e)
            Result.failure()
        } catch (e: Exception) {
            Log.e(TAG, "Command failed", e)
            Result.retry()
        }
    }

    private fun handleSocialApps(dpm: DevicePolicyManager, admin: ComponentName, json: JSONObject) {
        val appActions = json.optJSONArray("AppActions") ?: return

        for (index in 0 until appActions.length()) {
            val app = appActions.getJSONObject(index)
            val appName = app.optString("PackageName")
            val action = app.optString("Action")

            updateAppStatus(dpm, admin, appName, action)
        }

    }

    private fun updateAppStatus(dpm: DevicePolicyManager, admin: ComponentName, appName: String, action: String) {
        val packages = appPackages[appName].orEmpty()

        if (packages.isEmpty()) {
            Log.e("DPC", "Package mapping not found for: $appName")
            return
        }

        // true = suspend/disable, false = resume/enable
        val suspendApp = action.toLowerCase().equals("disable", ignoreCase = true)

        val failedPackages = dpm.setPackagesSuspended(admin, packages.toTypedArray(), suspendApp)

        if (failedPackages.isEmpty()) {
            Log.d("DPC", "$appName $action successfully")
        } else {
            Log.e("DPC", "Failed packages: ${failedPackages.joinToString()}")
        }
    }

    private val appPackages = mapOf(
        // Social Apps
        "Facebook" to listOf("com.facebook.katana"),
        "WhatsApp" to listOf("com.whatsapp"),
        "Instagram" to listOf("com.instagram.android"),
        "Telegram" to listOf("org.telegram.messenger"),
        "Snapchat" to listOf("com.snapchat.android"),
        "YouTube" to listOf("com.google.android.youtube"),

        // Gaming Apps
        "Candy Crush" to listOf("com.king.candycrushsaga"),
        "Battle Ground mobile" to listOf(
            "com.pubg.imobile",  // BGMI
            "com.tencent.ig"     // PUBG Mobile
        ),
        "Chess" to listOf(
            "com.chess",                 // Chess.com
            "org.lichess.mobileapp"      // Lichess
        ),
        "Free Fire" to listOf("com.dts.freefireth", "com.dts.freefiremax"),
        "Call of duty" to listOf("com.activision.callofduty.shooter"),
        "8 Ball Pool" to listOf("com.miniclip.eightballpool"),

        // UPI Apps
        "Phonepe" to listOf("com.phonepe.app"),
        "Googlepay" to listOf("com.google.android.apps.nbu.paisa.user"),
        "Paytm" to listOf("net.one97.paytm"),
        "Cred" to listOf("com.dreamplug.androidapp"),
        "BHIM" to listOf("in.org.npci.upiapp")
    )

    companion object {
        const val KEY_PAYLOAD = "payload"
        private const val TAG = "KioskPolicyWorker"
    }

}