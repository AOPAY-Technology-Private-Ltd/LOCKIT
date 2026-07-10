package com.bosandroidapp.aopaykit.kioskmode

import android.accessibilityservice.AccessibilityService
import android.app.ActivityManager
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.SETTINGS_PKG
import com.bosandroidapp.aopaykit.constant.ConstantClass.gpsSettingsOpened
import com.bosandroidapp.aopaykit.utils.ACCESSIBILITYTAG
import com.bosandroidapp.aopaykit.utils.Logger
import com.bosandroidapp.aopaykit.utils.syncEmis

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyAccessibilityService : AccessibilityService() {


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        CoroutineScope(Dispatchers.IO).launch {
            syncEmis()
        }

        if (isMyAppInfoPage() && !isEMIsCompleted()) {
            Logger.d(ACCESSIBILITYTAG, "On App Info Page: Global Back")
            performGlobalAction(GLOBAL_ACTION_BACK)
        }

        if (isFactoryResetting(event?.text?.toString() ?: "") && !isEMIsCompleted()) {
            Logger.d(ACCESSIBILITYTAG, "On Factory Reset Page: Global Back")
            performGlobalAction(GLOBAL_ACTION_BACK)
            this.showToast("You are not allowed to Factory reset your device when your EMIs are pending.")
        }

        val currentPkg = event?.packageName?.toString() ?: ""

        if (!isGpsEnabled(this) && !isEMIsCompleted()) {
            // Open GPS settings ONLY ONCE
            if (!gpsSettingsOpened) {
                gpsSettingsOpened = true
                showToast("GPS must be enabled to use this device")
                openGpsSettings()
                return
            }
            if (!currentPkg.contains(SETTINGS_PKG)) {
                openGpsSettings()   // FORCE BACK
            }

            return // STOP all other processing
        }

        // ✅ GPS ENABLED → RELEASE LOCK
        if (gpsSettingsOpened) {
            gpsSettingsOpened = false
        }

        /*if (isGoogleLogin(event) && !isEMIsCompleted()) {
            Logger.d(ACCESSIBILITYTAG, "On Google Login Page: Global Back")
            performGlobalAction(GLOBAL_ACTION_BACK)
        }*/


        /* if (isLocked()) {
             Logger.d(ACCESSIBILITYTAG, "Phone Locked")
             isMyAppMinimizedOrRemoved(event)
         }*/


        if (isLocked()) {

            val packageName = event?.packageName?.toString()


            if(packageName==null){
                refreshService()
            }

            Log.d("packageName", packageName.toString())

            // Allow soft keyboard
            if (isKeyboardPackage(packageName)) {
                return
            }

            // Allow chooser/share sheet
            if (isAllowedSystemPackage(packageName)) {
                return
            }


            /*if (isActivityRunning(this, PGWebViewActivity::class.java)) {
                return
            }*/

            if (ConstantClass.isPgClosing) {
                Handler(Looper.getMainLooper()).postDelayed({
                    ConstantClass.isPgClosing = false
                }, 1000)
                return
            }

            Logger.d(ACCESSIBILITYTAG, "Phone Locked")
            isMyAppMinimizedOrRemoved(event)
        }


    }

    override fun onInterrupt() {
        Log.d("Accessibility", "Service interrupted")
    }

    private fun isFactoryResetting(t: String): Boolean {
        val text = t.toLowerCase()
        return if (text.contains("reset phone", false) || text.contains(
                "erase all data",
                false
            ) || text.contains(
                "erase data",
                false
            ) || text.contains("factory reset", false) || text.contains("factory reset", false)
        ) true
        else false
    }


    private fun isMyAppMinimizedOrRemoved(event: AccessibilityEvent?) {
        Log.d("Accessibility Package Name", "Package Name: ${event?.packageName}")
        if (!((event?.packageName?.equals("com.google.android.apps.nbu.paisa.user")) ?: false)
            && !((event?.packageName?.equals("com.phonepe.app")) ?: false)
            && !((event?.packageName?.equals("net.one97.paytm")) ?: false)
            && !((event?.packageName?.equals("in.org.npci.upiapp")) ?: false)
            && !((event?.packageName?.equals("com.mobikwik_new")) ?: false)
            && !((event?.packageName?.equals("com.freecharge.mobile")) ?: false)
            && !((event?.packageName?.equals("com.icici.pockets")) ?: false)
            && !((event?.packageName?.equals("com.axis.axispay")) ?: false)
            && !((event?.packageName?.equals("com.hdfcbank.payzapp")) ?: false)
            && !((event?.packageName?.equals("sbi.mobile.apps.in")) ?: false)
            && !((event?.packageName?.equals("in.amazon.mShop.android.shopping")) ?: false)
            && !((event?.packageName?.equals("com.bosandroidapp.aopayfinance")) ?: false)
            && !(event?.packageName == null) &&!isMyAppOnTop()&& /*!isActivityRunning(this, KioskActivity::class.java)*/  event?.packageName != null && !isPaymentAppRunning()) {

            Logger.d(ACCESSIBILITYTAG, "${event.packageName}")
            Logger.d(ACCESSIBILITYTAG, "Performing KioskActivity Intent")
            val intent = Intent(this, KioskActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
            startActivity(intent, options.toBundle())

        }


    }


    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = getServiceInfo()

        // Fetch all installed packages on the device
        val packages = getPackageManager().getInstalledPackages(0)
        val packageNames = arrayOfNulls<String>(packages.size)
        for (i in packages.indices) {
            packageNames[i] = packages.get(i)!!.packageName
        }

        // Explicitly map them to the service info
        info.packageNames = packageNames
        setServiceInfo(info)
    }


    fun refreshService() {
        val info = getServiceInfo()
        if (info != null) {
            // Re-applying the exact same info forces the system to refresh the channel
            setServiceInfo(info)
        }
    }


    // changes by me
    private fun isMyAppOnTop(): Boolean {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        for (task in am.appTasks) {
            val top = task.taskInfo.topActivity
            if (top?.packageName == packageName) {
                return true
            }
        }
        return false
    }


    private fun isActivityRunning(context: Context, activityClass: Class<*>): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.appTasks
        for (task in tasks) {
            val base = task.taskInfo.baseActivity
            val top = task.taskInfo.topActivity
            if (base?.className == activityClass.name && top?.className == activityClass.name) {
                return true
            }
        }
        return false
    }


    private fun isMyAppInfoPage(): Boolean {
        val rootNode = rootInActiveWindow ?: return false
        val texts = getVisibleText(rootNode)
        var isAppInfo = false
        var isMyApp = false

        // Search for known "App info" patterns
        for (t in texts) {
            if (t.contains("App info", true) || t.contains(
                    "Device admin app",
                    true
                ) || t.contains("Erase app data", true)
            ) {
                isAppInfo = true
            }
            // Many devices show package name directly
            if (t.contains("", true)) { // crude check for package
                isMyApp = true
            }
        }
        return (isAppInfo && isMyApp)
    }

    private fun isGoogleLogin(event: AccessibilityEvent?): Boolean {
        Log.d("Accessibility",(event?.packageName?:"").toString())
        if (event?.packageName?.contains("com.google.android.gms") ?: false ||
            event?.packageName?.contains("com.google.android.gsf.login") ?: false
        ) {
            return true
        }
        return false
    }

    private fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
    }


    private fun openGpsSettings() {
        val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }


    private fun getVisibleText(node: AccessibilityNodeInfo?): List<String> {
        val result = mutableListOf<String>()
        if (node == null) return result
        node.text?.toString()?.let { result.add(it) }

        for (i in 0 until node.childCount) {
            result.addAll(getVisibleText(node.getChild(i)))
        }
        return result
    }


    private fun isKeyboardPackage(packageName: String?): Boolean {

        return packageName.equals("com.google.android.inputmethod.latin", true) ||
                packageName.equals("com.samsung.android.honeyboard", true) ||
                packageName.equals("com.microsoft.swiftkey", true) ||
                packageName.equals("com.touchtype.swiftkey", true) ||
                packageName.equals("com.google.android.tts", true) ||

                // System Chooser / Share Sheet
                packageName.equals("android", true) ||
                packageName.equals("com.android.systemui", true) ||
                packageName.equals("com.android.intentresolver", true) ||
                packageName.equals("com.android.permissioncontroller", true) ||
                packageName.equals("com.google.android.permissioncontroller", true) ||
                packageName.equals("com.google.android.permissioncontroller", true) ||
                packageName.equals("com.android.permissioncontroller", true) ||

                // Your App
                packageName.equals("com.bosandroidapp.aopayfinance", true) ||

                // Payment Apps
                packageName.equals("sbi.mobile.apps.in", true) ||
                packageName.equals("com.hdfcbank.payzapp", true) ||
                packageName.equals("net.one97.paytm", true) ||
                packageName.equals("com.mobikwik_new", true) ||
                packageName.equals("com.freecharge.mobile", true) ||
                packageName.equals("com.axis.axispay", true) ||
                packageName.equals("in.amazon.mShop.android.shopping", true) ||
                packageName.equals("com.phonepe.app", true) ||
                packageName.equals("com.google.android.apps.nbu.paisa.user", true)||
                // CRED
                packageName.equals("com.dreamplug.androidapp", true)||
                packageName.equals("com.flipkart.supermoney", true)||
                packageName.equals("com.whatsapp", true)


    }


    private fun isAllowedSystemPackage(packageName: String?): Boolean {
        return packageName.equals("android", true) ||
                packageName.equals("com.android.settings", true) ||
                packageName.equals("com.android.systemui", true) ||
                packageName.equals("com.android.intentresolver", true) ||
                packageName.equals("com.android.permissioncontroller", true) ||
                packageName.equals("com.google.android.permissioncontroller", true)||

                // Oppo / OnePlus / Realme
                packageName.equals("com.oplus.safecenter", true) ||

                packageName.equals("com.coloros.safecenter", true) ||

                // Xiaomi / Redmi / Poco
                packageName.equals("com.miui.securitycenter", true) ||

                // Samsung
                packageName.equals("com.samsung.android.lool", true) || // Device Care
                packageName.equals("com.samsung.android.sm.devicesecurity", true) ||
                packageName?.contains("biometric", true) ?: false ||
                packageName?.contains("biometrics", true) ?: false ||

                // Vivo
                packageName.equals("com.iqoo.secure", true) ||
                packageName.equals("com.vivo.permissionmanager", true) ||

                // Huawei
                packageName.equals("com.huawei.systemmanager", true) ||

                // Motorola
                packageName.equals("com.motorola.ccc.ota", true) ||

                // Nothing
                packageName.equals("com.nothing.smartcenter", true)||

                packageName.equals("com.bosandroidapp.aopayfinance", true)

    }




}

