package com.bosandroidapp.aopaykit.kioskmode

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.admin.DevicePolicyManager
import android.app.admin.FactoryResetProtectionPolicy
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.UserManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass.CheckCompleteEmiStatus
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class KioskPolicyService : Service() {

    private lateinit var fusedClient: FusedLocationProviderClient


    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(1, createNotification()) // required

        val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager

        val admin = ComponentName(this, KioskDeviceAdminReceiver::class.java)

         val action = intent?.getStringExtra("action")

        if (dpm.isDeviceOwnerApp(packageName) && dpm.isAdminActive(admin)) {
           // dpm.setCameraDisabled(admin, true)
            Toast.makeText(this, "Admin active", Toast.LENGTH_LONG).show()

            when (action) {

                // camera feature
                "DISABLE_CAMERA" -> {
                    dpm.setCameraDisabled(admin, true)
                }

                "ENABLE_CAMERA" -> {
                    dpm.setCameraDisabled(admin, false)
                }

                "ENABLE_WIFI" -> {
                    dpm.clearUserRestriction(admin, UserManager.DISALLOW_CONFIG_WIFI)
                }

                "DISABLE_WIFI" -> {
                    dpm.addUserRestriction(admin, UserManager.DISALLOW_CONFIG_WIFI)
                }

                "REBOOT" -> {
                    dpm.reboot(admin)
                }

                "ENABLE_AIRPLANE_MODE" -> {
                    dpm.clearUserRestriction(admin, UserManager.DISALLOW_AIRPLANE_MODE)
                }

                "DISABLE_AIRPLANE_MODE" -> {
                    dpm.addUserRestriction(admin, UserManager.DISALLOW_AIRPLANE_MODE)
                }

                "DISABLE_KIOSK" -> {
                    dpm.setLockTaskPackages(admin, arrayOf())
                    stopLockSituation()
                }

                "ENABLE_KIOSK" -> {
                    dpm.setLockTaskPackages(admin, arrayOf(packageName))
                    startLockSituation()
                }

                "APP_HIDE"->{
                    val packageNameToHide = "com.whatsapp"
                    dpm.setApplicationHidden(admin, packageNameToHide, true)

                }
                "APP_SHOW"->{
                    val packageNameToShow = "com.whatsapp"
                    dpm.setApplicationHidden(admin, packageNameToShow, false)
                }

                "DISABLE_USB" ->{
                    dpm.addUserRestriction(admin, UserManager.DISALLOW_USB_FILE_TRANSFER)
                }

                "ENABLE_USB" ->{
                    dpm.clearUserRestriction(admin, UserManager.DISALLOW_USB_FILE_TRANSFER)
                }

                "ENABLE_BLUETOOTH" ->{
                    dpm.clearUserRestriction(admin, UserManager.DISALLOW_BLUETOOTH)
                }

                "DISABLE_BLUETOOTH" ->{
                    dpm.addUserRestriction(admin, UserManager.DISALLOW_BLUETOOTH)
                }

             /*   "SIM_INFO" -> {
                         dpm.setPermissionGrantState(
                admin,
                packageName,
                Manifest.permission.READ_PHONE_STATE,
                DevicePolicyManager.PERMISSION_GRANT_STATE_GRANTED
            )

            dpm.setPermissionGrantState(
                admin,
                packageName,
                Manifest.permission.READ_PHONE_NUMBERS,
                DevicePolicyManager.PERMISSION_GRANT_STATE_GRANTED
            )

            val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val subscriptionManager = getSystemService(TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

            val simOperatorName = telephonyManager.simOperatorName
            val networkOperatorName = telephonyManager.networkOperatorName
            val countryIso = telephonyManager.simCountryIso
            val simList = subscriptionManager.activeSubscriptionInfoList

                }*/

                "FETCH_LOCATION" ->{

                    dpm.setPermissionGrantState(
                        admin,
                        packageName,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        DevicePolicyManager.PERMISSION_GRANT_STATE_GRANTED
                    )

                    dpm.setPermissionGrantState(
                        admin,
                        packageName,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                        DevicePolicyManager.PERMISSION_GRANT_STATE_GRANTED
                    )

                    getCurrentLocation(this)
                }

                "APP_UNINSTALL" ->{
                    if (dpm.isDeviceOwnerApp(packageName)) {
                        CheckCompleteEmiStatus = false

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            val policy = FactoryResetProtectionPolicy.Builder()
                                .setFactoryResetProtectionAccounts(emptyList())
                                .build()

                            dpm.setFactoryResetProtectionPolicy(admin, policy)
                        }
                        dpm.clearUserRestriction(admin, UserManager.DISALLOW_FACTORY_RESET)
                        dpm.clearDeviceOwnerApp(getPackageName())
                        dpm.removeActiveAdmin(admin)
                    }
                }

            }


        }

        stopSelf()
        return START_NOT_STICKY
    }


    override fun onBind(intent: Intent?) = null


    private fun createNotification(): Notification {

        val channelId = "kiosk"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Kiosk Service", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Kiosk Active")
            .setSmallIcon(R.drawable.newapplogo)
            .build()
    }


    fun getCurrentLocation(context: Context) {

        fusedClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedClient.lastLocation.addOnSuccessListener { location ->

            if (location != null) {

                val lat = location.latitude
                val lng = location.longitude

                Log.d("LOCATION", "$lat , $lng")

            } else {

                val request = CurrentLocationRequest.Builder()
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .build()

                fusedClient.getCurrentLocation(request, CancellationTokenSource().token)
                    .addOnSuccessListener {

                        it?.let { loc ->
                            Log.d("LOCATION", "${loc.latitude}, ${loc.longitude}")
                        }
                    }
            }
        }
    }

    fun hitApiForUpdateAction(){
        //updateActionFromCustomerDevice
    }

}