package com.bosandroidapp.aopaykit.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.UiAutomation
import android.app.admin.DevicePolicyManager
import android.app.admin.FactoryResetProtectionPolicy
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import android.os.OutcomeReceiver
import android.os.UserManager
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.CheckCompleteEmiStatus
import com.bosandroidapp.aopaykit.data.customeraction.CustomerSideUpdateUnInstallAppRequest
import com.bosandroidapp.aopaykit.data.customeraction.GetPendingDeviceActionReq
import com.bosandroidapp.aopaykit.data.customeraction.UpdateCustomerDeviceActionRequest
import com.bosandroidapp.aopaykit.data.customeraction.UploadCustomerLocationRequest
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.kioskmode.KioskDeviceAdminReceiver
import com.bosandroidapp.aopaykit.kioskmode.KioskLockPage
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.util.concurrent.Executors


class MyFirebaseMessagingService : FirebaseMessagingService() {
    var rid : Int = -1
    private lateinit var fusedClient: FusedLocationProviderClient
    private val preference: SharedPreference by lazy { SharedPreference.getInstance(applicationContext)!! }
    private val authRepository by lazy { AuthRepository(RetrofitClient.apiInterface) }



    @RequiresApi(Build.VERSION_CODES.R)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val payload = remoteMessage.data["payload"]

        // 1. Show notification in the status bar if title/body exists
        if (!title.isNullOrEmpty() || !body.isNullOrEmpty()) {
            sendNotification(title ?: "LockKit Notification", body ?: "")
        }

        if (!payload.isNullOrEmpty()) {

            Log.d("payload", Gson().toJson(payload))

            try {
                val dpm = applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

                val admin = ComponentName(applicationContext, KioskDeviceAdminReceiver::class.java)

                val json = JSONObject(payload)
                var NotificationCode = json.optString(ConstantClass.NotificationCode)

                hitApiForGetActionStatus(NotificationCode)

                when(NotificationCode){

                    ConstantClass.SocialApps, ConstantClass.GamingApps, ConstantClass.UPIApps -> {
                        handleSocialApps(dpm, admin, json)
                    }

                    ConstantClass.Lock -> {

                        val action = json.optString(ConstantClass.JSONAction).toLowerCase().equals("enable", ignoreCase = true)

                        var pin =  json.optString(ConstantClass.JSONACTIONPIN)
                        val tokenString = preference.getStringValue(ConstantClass.SecretKey,"")

                        val resetToken = Base64.decode(tokenString, Base64.DEFAULT)

                        if(action){
                            val changed =  dpm.resetPasswordWithToken(admin, pin,       // obtain securely; never hard-code
                                resetToken,
                                0
                            )
                            if (changed) {
                                dpm.lockNow()
                                if(rid>-1){
                                    hitApiForUpdateActionStatus("")
                                }
                            }
                        }

                        else{
                            dpm.resetPasswordWithToken(
                                admin,
                                "",       // obtain securely; never hard-code
                                resetToken,
                                0
                            )
                            if(rid>-1){
                                hitApiForUpdateActionStatus("")
                            }
                        }

                    }

                    ConstantClass.UnLock -> {
                        val tokenString = preference.getStringValue(ConstantClass.SecretKey,"")

                        val resetToken = Base64.decode(tokenString, Base64.DEFAULT)

                        dpm.resetPasswordWithToken(
                            admin,
                            "",       // obtain securely; never hard-code
                            resetToken,
                            0
                        )

                        if(rid>-1){
                            hitApiForUpdateActionStatus("")
                        }

                    }

                    ConstantClass.CameraDisable -> {
                        val action = json.optString(ConstantClass.JSONAction).toLowerCase().equals("enable", ignoreCase = true)
                        if(action){
                            dpm.setCameraDisabled(admin, true)
                        }
                        else{
                            dpm.setCameraDisabled(admin, false)
                        }

                        if(rid>-1){
                            hitApiForUpdateActionStatus("")
                        }

                    }

                    ConstantClass.DisableSetting-> {
                        handleSettingApps(dpm, admin, json)
                    }

                    ConstantClass.Reboot -> {
                        dpm.reboot(admin)

                        if(rid>-1){
                            hitApiForUpdateActionStatus("")
                        }
                    }

                    ConstantClass.DevicePin -> {
                        val tokenString = preference.getStringValue(ConstantClass.SecretKey,"")
                        var pin =  json.optString(ConstantClass.JSONACTIONPIN)

                        val resetToken = Base64.decode(
                            tokenString,
                            Base64.DEFAULT
                        )

                        val changed =  dpm.resetPasswordWithToken(
                            admin,
                            pin,       // obtain securely; never hard-code
                            resetToken,
                            0
                        )

                        if (changed) {
                            Log.d("devicePin","${pin} Pin Set Success")

                            if(rid>-1){
                                hitApiForUpdateActionStatus(pin)
                            }
                        }

                    }

                    ConstantClass.CALL_DISABLE ->{
                        val action = json.optString(ConstantClass.JSONAction).toLowerCase().equals("enable", ignoreCase = true)
                        if(action){
                            dpm.addUserRestriction(admin, UserManager.DISALLOW_OUTGOING_CALLS)
                            dpm.addUserRestriction(admin, UserManager.DISALLOW_SMS)
                        }
                        else{
                            dpm.clearUserRestriction(admin, UserManager.DISALLOW_OUTGOING_CALLS)
                            dpm.clearUserRestriction(admin, UserManager.DISALLOW_SMS)
                        }

                        if(rid>-1){
                            hitApiForUpdateActionStatus("")
                        }
                    }

                    ConstantClass.Airplane -> {
                        val action = json.optString(ConstantClass.JSONAction).toLowerCase().equals("enable", ignoreCase = true)

                        if(action){
                            dpm.addUserRestriction(admin, UserManager.DISALLOW_AIRPLANE_MODE)
                        }
                        else{
                            dpm.clearUserRestriction(admin, UserManager.DISALLOW_AIRPLANE_MODE)
                        }

                        if(rid>-1){
                            hitApiForUpdateActionStatus("")
                        }

                    }

                    ConstantClass.Kisok -> {
                        val actionStatus = json.optString(ConstantClass.JSONAction).toLowerCase().equals("enable", ignoreCase = true)

                        if (actionStatus) {
                            // Launch KioskLockPage when locked

                            preference.setBooleanValue(ConstantClass.IS_KIOSK_ENABLED,true)

                            val intent = Intent(applicationContext, KioskLockPage::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            }
                            val filter = IntentFilter(Intent.ACTION_MAIN).apply {
                                addCategory(Intent.CATEGORY_HOME)
                                addCategory(Intent.CATEGORY_DEFAULT)
                            }
                            val activity = ComponentName(this, KioskLockPage::class.java)
                            dpm.addPersistentPreferredActivity(
                                admin,
                                filter,
                                activity
                            )
                            startActivity(intent)
                        }
                        else {
                            preference.setBooleanValue(ConstantClass.IS_KIOSK_ENABLED,false)
                            val intent = Intent(applicationContext, KioskLockPage::class.java).apply {
                                action = "EXIT_KIOSK"
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            startActivity(intent)
                        }
                        if(rid>-1){
                            hitApiForUpdateActionStatus("")
                        }
                    }

                    ConstantClass.AppHide ->{
                        handleHideApps(dpm, admin, json)
                    }

                    ConstantClass.UNINSTALL ->{
                        val tokenString = preference.getStringValue(ConstantClass.SecretKey,"")
                        val resetToken = Base64.decode(tokenString, Base64.DEFAULT)

                        dpm.resetPasswordWithToken(
                            admin,
                            "",       // obtain securely; never hard-code
                            resetToken,
                            0
                        )

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
                            if(rid>-1){
                                hitApiForUpdateActionStatus("")
                            }
                            hitApiForUploadUninstallAppStatus()

                        }

                    }

                    ConstantClass.GETLOCATION ->{

                        dpm.setLocationEnabled(admin, true)

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

                    ConstantClass.SIM_REMOVE_LOCK -> {
                        if(rid>-1){
                            hitApiForUpdateActionSimRemoveLockStatus(dpm,admin)
                        }
                    }

                }


            }
            catch (e: Exception){

            }
        }


    }


    private fun sendNotification(title: String, messageBody: String) {
        val channelId = "LockKit_Notification_Channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Device Policy Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for device lock and restriction updates"
                enableLights(true)
                lightColor = android.graphics.Color.BLUE
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.newapplogo) 
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }


    @SuppressLint("MissingPermission")
    fun getSimIdentifiers(context: Context): List<SimInfo> {

        val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

        val simList = mutableListOf<SimInfo>()
        subscriptionManager.activeSubscriptionInfoList?.forEach { sim ->
            simList.add(
                SimInfo(
                    iccId = sim.iccId,
                    subscriptionId = sim.subscriptionId,
                    carrierName = sim.carrierName.toString(),
                    mcc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) sim.mccString else sim.mcc.toString(),
                    mnc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) sim.mncString else sim.mnc.toString(),
                    slotIndex = sim.simSlotIndex,
                    simNumber =   sim.number.toString()
                )
            )
        }

        return simList
    }


    data class SimInfo(
        val iccId: String?,
        val subscriptionId: Int,
        val carrierName: String,
        val mcc: String?,
        val mnc: String?,
        val slotIndex: Int,
        val simNumber: String,
    )


    fun getCurrentLocation(context: Context) {

        fusedClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        fusedClient.lastLocation.addOnSuccessListener { location ->

            if (location != null) {

                val lat = location.latitude
                val lng = location.longitude

                Log.d("LOCATION", "$lat , $lng")
                if(rid>-1){
                    hitApiForUpdateActionStatus("")
                }
                hitApiForUploadLocation(lat,lng)

            }
            else {

                val request = CurrentLocationRequest.Builder()
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .build()

                fusedClient.getCurrentLocation(request, CancellationTokenSource().token)
                    .addOnSuccessListener {

                        it?.let { loc ->
                            Log.d("LOCATION", "${loc.latitude}, ${loc.longitude}")
                            if(rid>-1){
                                hitApiForUpdateActionStatus("")
                            }
                            hitApiForUploadLocation(loc.latitude,loc.longitude)
                        }
                    }
            }
        }
    }


    fun hitApiForGetActionStatus(notificationCode: String) {
        val request = GetPendingDeviceActionReq(customerCode = preference.getStringValue(ConstantClass.CustomerCode, ""
        ))
        Log.d("request", Gson().toJson(request))

        runBlocking {
            try {
                val response = authRepository.getPendingDeviceActionRequest(request)

                if (response?.isSuccessful == true) {
                    val body = response.body()
                    if (body?.status == true && !body.data.isNullOrEmpty()) {
                        Log.d("response", Gson().toJson(body))
                        body.data.forEach { item->
                            if(item?.notificationCode==notificationCode){
                                rid = item.rid!!
                                return@forEach
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("hitApiForGetStatus", "Error: ${e.message}")
            }
            Unit
        }

    }


    fun hitApiForUpdateActionSimRemoveLockStatus(dpm:DevicePolicyManager,admin:ComponentName) {

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


        var simData = getSimIdentifiers(applicationContext)
        Log.d("simData", Gson().toJson(simData))

        if(simData.firstOrNull()?.iccId.isNullOrEmpty() || simData.firstOrNull()?.carrierName.isNullOrEmpty()){
            dpm.clearUserRestriction(admin, UserManager.DISALLOW_OUTGOING_CALLS)
            dpm.clearUserRestriction(admin, UserManager.DISALLOW_SMS)
        }
        else{
            val updaterequest = UpdateCustomerDeviceActionRequest(
                rid = rid,
                updatedBy = preference.getStringValue(ConstantClass.CustomerCode, ""),
                executionStatus = "Success",
                failureReason = "",
                devicePin= "",
                iccid = simData.firstOrNull()?.iccId,
                subscriptionId = simData.firstOrNull()?.subscriptionId,
                carrierName = simData.firstOrNull()?.carrierName,
                mcc = simData.firstOrNull()?.mcc,
                mnc = simData.firstOrNull()?.mnc,
                slotIndex = simData.firstOrNull()?.slotIndex,
            )
            runBlocking {
                try {
                    val response = authRepository.updateActionFromCustomerDevice(updaterequest)
                    if (response?.isSuccessful == true) {
                        if(response.body()?.status==true){
                            // sim same
                            dpm.clearUserRestriction(admin, UserManager.DISALLOW_OUTGOING_CALLS)
                            dpm.clearUserRestriction(admin, UserManager.DISALLOW_SMS)
                        }
                        else {
                            // sim change..........................
                            val uri = Settings.System.DEFAULT_ALARM_ALERT_URI
                            val ringtone = RingtoneManager.getRingtone(applicationContext, uri)
                            ringtone.play()
                            dpm.addUserRestriction(admin, UserManager.DISALLOW_OUTGOING_CALLS)
                            dpm.addUserRestriction(admin, UserManager.DISALLOW_SMS)
                        }
                        Log.d("responseUpdate", Gson().toJson(response.body()))
                    }

                } catch (e: Exception) {
                    Log.e("hitApiForUpdateStatus", "Error: ${e.message}")
                }
                Unit
            }
        }

    }


    fun hitApiForUpdateActionStatus( devicePin: String?) {

        val updaterequest = UpdateCustomerDeviceActionRequest(
            rid = rid,
            updatedBy = preference.getStringValue(ConstantClass.CustomerCode, ""),
            executionStatus = "Success",
            failureReason = "",
            devicePin= devicePin,
            )
        runBlocking {
            try {
                val response = authRepository.updateActionFromCustomerDevice(updaterequest)
                if (response?.isSuccessful == true) {
                    Log.d("responseUpdate", Gson().toJson(response.body()))
                }
            } catch (e: Exception) {
                Log.e("hitApiForUpdateStatus", "Error: ${e.message}")
            }
            Unit
        }

    }


    fun hitApiForUploadUninstallAppStatus() {


        val updaterequest = CustomerSideUpdateUnInstallAppRequest(
            clientCode = preference.getStringValue(ConstantClass.ClientCode,""),
            appName = "LockKitApp",
            eventTime = ConstantClass.getCurrentStartDate(),
            customerCode = preference.getStringValue(ConstantClass.CustomerCode, ""),
            retailerCode =  preference.getStringValue(ConstantClass.RetailerCode, ""),
            packageName= packageName
        )
        runBlocking {
            try {
                val response = authRepository.updateAppUninstallStatusReq(updaterequest)
                if (response?.isSuccessful == true) {
                    Log.d("responseUpdate", Gson().toJson(response.body()))
                }
            } catch (e: Exception) {
                Log.e("hitApiForUpdateStatus", "Error: ${e.message}")
            }
            Unit
        }

    }


    fun hitApiForUploadLocation(lat: Double, lng: Double) {
        val request = UploadCustomerLocationRequest(
            clientCode = preference.getStringValue(ConstantClass.ClientCode,""),
            customerCode = preference.getStringValue(ConstantClass.CustomerCode, ""),
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
            latitude = lat,
            longitude = lng,
            locationTime = ConstantClass.getCurrentStartDate())

        Log.d("locationReq", Gson().toJson(request))

        runBlocking {
            try {
                val response = authRepository.uploadKitCustomerLocationRequest(request)
                if (response?.isSuccessful == true) {
                    Log.d("locationresp", Gson().toJson(response.body()))
                }
            } catch (e: Exception) {
                Log.e("hitApiForGetStatus", "Error: ${e.message}")
            }
            Unit
        }

    }


    private fun handleSocialApps(dpm: DevicePolicyManager, admin: ComponentName, json: JSONObject) {
        val appActions = json.optJSONArray("AppActions") ?: return

        for (index in 0 until appActions.length()) {
            val app = appActions.getJSONObject(index)
            val packageName = app.optString("PackageName")
            val action = app.optString("Action")

            updateAppStatus(dpm, admin, packageName, action)
        }

    }


    private fun handleHideApps(dpm: DevicePolicyManager, admin: ComponentName, json: JSONObject) {
        val appActions = json.optJSONArray("AppActions") ?: return

        for (index in 0 until appActions.length()) {
            val app = appActions.getJSONObject(index)
            val packageName = app.optString("PackageName")
            val action = app.optString("Action")

            hideUnhideAppStatus(dpm, admin, packageName, action)
        }

    }


    private fun handleSettingApps(dpm: DevicePolicyManager, admin: ComponentName, json: JSONObject) {
        val appActions = json.optJSONArray("AppActions") ?: return

        for (index in 0 until appActions.length()) {
            val app = appActions.getJSONObject(index)
            val appName = app.optString("PackageName")
            val action = app.optString("Action")

            updateSettingStatus(dpm, admin, appName, action)
        }

    }


    private fun updateSettingStatus(dpm: DevicePolicyManager, admin: ComponentName, appName: String, action: String) {
        val action = action.toLowerCase().equals("enable", ignoreCase = true)

       when(appName){

           ConstantClass.Bluetooth ->  {
               var isapplied =false
               if(action){
                   dpm.addUserRestriction(admin, UserManager.DISALLOW_BLUETOOTH)
                   isapplied =true
               }else{
                   dpm.clearUserRestriction(admin, UserManager.DISALLOW_BLUETOOTH)
                   isapplied =true
               }
               if(isapplied&& rid>-1){
                   hitApiForUpdateActionStatus("")
               }
           }

           ConstantClass.Wifi ->  {
               var isapplied =false
               if(action){
                   dpm.addUserRestriction(admin, UserManager.DISALLOW_CONFIG_WIFI)
                   isapplied =true
               }
               else{
                   dpm.clearUserRestriction(admin, UserManager.DISALLOW_CONFIG_WIFI)
                   isapplied =true
               }
               if(isapplied&& rid>-1){
                   hitApiForUpdateActionStatus("")
               }
           }

           ConstantClass.Hotspot -> {
               var isapplied =false
               if(action) {
                   dpm.addUserRestriction(admin, UserManager.DISALLOW_CONFIG_TETHERING)
                   isapplied =true
               }
               else{
                   dpm.clearUserRestriction(admin, UserManager.DISALLOW_CONFIG_TETHERING)
                   isapplied =true
               }

               if(isapplied&& rid>-1){
                   hitApiForUpdateActionStatus("")
               }


           }

           ConstantClass.USB -> {
               var isapplied = false
               if(action){
                   dpm.addUserRestriction(admin, UserManager.DISALLOW_USB_FILE_TRANSFER)
                   isapplied = true
               }else {
                   dpm.clearUserRestriction(admin, UserManager.DISALLOW_USB_FILE_TRANSFER)
                   isapplied = true
               }

               if(isapplied&& rid>-1){
                   hitApiForUpdateActionStatus("")
               }

           }


       }

    }



  /*  private fun updateAppStatus(dpm: DevicePolicyManager, admin: ComponentName, appName: String, action: String) {
        val packages = appPackages[appName].orEmpty()

        if (packages.isEmpty()) {
            Log.e("DPC", "Package mapping not found for: $appName")
            return
        }

        // true = suspend/disable, false = resume/enable
        val suspendApp = action.toLowerCase().equals("enable", ignoreCase = true)

        val failedPackages = dpm.setPackagesSuspended(admin, packages.toTypedArray(), suspendApp)

        if (failedPackages.isEmpty()) {
            if(rid>-1){
                hitApiForUpdateActionStatus("")
            }
            Log.d("DPC", "$appName $action successfully")
        } else {
            Log.e("DPC", "Failed packages: ${failedPackages.joinToString()}")
        }
    }*/


    private fun updateAppStatus(dpm: DevicePolicyManager, admin: ComponentName, packageName: String, action: String){

        if (packageName.isEmpty()) {
            Log.e("DPC", "Package mapping not found for: $packageName")
            return
        }

        // true = suspend, false = unsuspend
        val suspendApp = action.equals("enable", ignoreCase = true)

            try {
                packageManager.getPackageInfo(packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                Log.d("DPC", "$packageName is not installed")
            }

            try {

                val failed = dpm.setPackagesSuspended(admin, arrayOf(packageName), suspendApp)
                dpm.setUninstallBlocked(admin, packageName, suspendApp)

                if (failed.isEmpty()) {
                    Log.d("DPC", "($packageName) $action successfully")
                } else {
                    Log.e("DPC", "Failed package: ${failed.joinToString()}")
                }

                if (failed.isEmpty()) {
                    if (rid > -1) {
                        hitApiForUpdateActionStatus("")
                    }
                }

            } catch (e: Exception) {

                Log.e("DPC", "Error processing $packageName: ${e.message}")
            }

    }


   /* private fun hideUnhideAppStatus(dpm: DevicePolicyManager, admin: ComponentName, appName: String, action: String) {

        val packageName = hideunhideAppPackages[appName]?.firstOrNull()


        if (packageName!!.isEmpty()) {
            Log.e("DPC", "Package mapping not found for: $appName")
            return
        }

        // true = suspend/disable, false = resume/enable
        val hideapp = action.toLowerCase().equals("enable", ignoreCase = true)

        val changedDone = dpm.setApplicationHidden(admin, packageName, hideapp)

        if (changedDone) {
            if(rid>-1){
                hitApiForUpdateActionStatus("")
            }
            Log.d("DPCHideAPP", "$appName $action successfully")
        }
        else {
            Log.e("DPCHideAPP", "Failed packages: ${packageName}")
        }

    }*/


    private fun hideUnhideAppStatus(dpm: DevicePolicyManager, admin: ComponentName, packageName: String, action: String) {

        if (packageName.isNullOrEmpty()) {
            Log.e("DPC", "Package mapping not found for: $packageName")
            return
        }

        // true = suspend/disable, false = resume/enable
            val hideapp = action.toLowerCase().equals("enable", ignoreCase = true)
            try {
                preference.setBooleanValue(ConstantClass.IS_HIDE_ENABLED,hideapp)
                val changedDone = dpm.setApplicationHidden(admin, packageName, hideapp)
                dpm.setUninstallBlocked(admin, packageName, hideapp)
                if (changedDone) {
                    Log.d("DPCHideAPP", "($packageName) $action successfully")
                }
                else {
                    Log.e("DPCHideAPP", "Failed for package: $packageName")
                }
                if (changedDone) {
                    if (rid > -1) {
                        hitApiForUpdateActionStatus("")
                    }
                }

            }
            catch (e: Exception) {
                Log.e("DPCHideAPP", "Error while processing $packageName : ${e.message}")
            }


    }


   /* private val appPackages = mapOf(
        // Social Apps
        ConstantClass.FaceBook to listOf("com.facebook.katana"),
        ConstantClass.WhatsApp to listOf("com.whatsapp"),
        ConstantClass.Instagram to listOf("com.instagram.android"),
        ConstantClass.Telegram to listOf("org.telegram.messenger"),
        ConstantClass.Snapchat   to listOf("com.snapchat.android"),
        ConstantClass.YouTube to listOf("com.google.android.youtube"),

        // Gaming Apps
        ConstantClass.CandyCrush to listOf("com.king.candycrushsaga"),

        ConstantClass.BattleGroundMobile to listOf(
            "com.pubg.imobile",  // BGMI
            "com.tencent.ig"     // PUBG Mobile
        ),
        ConstantClass.Chess to listOf(
            "com.chess",                 // Chess.com
            "org.lichess.mobileapp"      // Lichess
        ),
        ConstantClass.FreeFire to listOf("com.dts.freefireth", "com.dts.freefiremax"),
        ConstantClass.CallOfDuty to listOf("com.activision.callofduty.shooter"),
        ConstantClass.BallPool to listOf("com.miniclip.eightballpool"),

        // UPI Apps
        ConstantClass.Phonepe to listOf("com.phonepe.app"),
        ConstantClass.Googlepay to listOf("com.google.android.apps.nbu.paisa.user"),
        ConstantClass.Paytm to listOf("net.one97.paytm"),
        ConstantClass.Cred to listOf("com.dreamplug.androidapp"),
        ConstantClass.BHIM to listOf("in.org.npci.upiapp")
    )*/

    private val appPackages = mapOf(

        // Social Apps
        ConstantClass.FaceBook to listOf(
            "com.facebook.katana",          // Facebook
            "com.facebook.lite"             // Facebook Lite
        ),

        ConstantClass.WhatsApp to listOf(
            "com.whatsapp",                 // WhatsApp Messenger
            "com.whatsapp.w4b"              // WhatsApp Business
        ),

        ConstantClass.Instagram to listOf(
            "com.instagram.android"
        ),

        ConstantClass.Telegram to listOf(
            "org.telegram.messenger",       // Official Telegram
            "org.thunderdog.challegram"     // Telegram X
        ),

        ConstantClass.Snapchat to listOf(
            "com.snapchat.android"
        ),

        ConstantClass.YouTube to listOf(
            "com.google.android.youtube",
            "com.google.android.youtube.tv" // YouTube TV
        ),


        // Gaming Apps
        ConstantClass.CandyCrush to listOf(
            "com.king.candycrushsaga",
            "com.king.candycrush4"
        ),

        ConstantClass.BattleGroundMobile to listOf(
            "com.pubg.imobile",             // BGMI India
            "com.tencent.ig",               // PUBG Mobile Global
            "com.pubg.krmobile",            // PUBG Korea
            "com.rekoo.pubgm",              // PUBG Taiwan
            "com.vng.pubgmobile"            // PUBG Vietnam
        ),

        ConstantClass.Chess to listOf(
            "com.chess",                    // Chess.com
            "org.lichess.mobileapp",        // Lichess
            "com.playchess",                // Play Magnus/Chess variants
            "com.chess.chesstempo"          // Chess Tempo
        ),

        ConstantClass.FreeFire to listOf(
            "com.dts.freefireth",           // Free Fire
            "com.dts.freefiremax"           // Free Fire MAX
        ),

        ConstantClass.CallOfDuty to listOf(
            "com.activision.callofduty.shooter",
            "com.activision.callofduty.warzone"
        ),

        ConstantClass.BallPool to listOf(
            "com.miniclip.eightballpool"
        ),


        // UPI / Payment Apps
        ConstantClass.Phonepe to listOf(
            "com.phonepe.app"
        ),

        ConstantClass.Googlepay to listOf(
            "com.google.android.apps.nbu.paisa.user",
            "com.google.android.apps.walletnfcrel"
        ),

        ConstantClass.Paytm to listOf(
            "net.one97.paytm"
        ),

        ConstantClass.Cred to listOf(
            "com.dreamplug.androidapp"
        ),

        ConstantClass.BHIM to listOf(
            "in.org.npci.upiapp"
        )
    )


   /* private val hideunhideAppPackages = mapOf(
        ConstantClass.Gallery to listOf("com.google.android.apps.photos"),
        ConstantClass.Chrome  to listOf("com.android.chrome"),
        ConstantClass.Gmail  to listOf("com.google.android.gm"),
        ConstantClass.GooglePhotos to listOf("com.google.android.apps.photos"),
        ConstantClass.GoogleDrive to listOf("com.google.android.apps.docs"),
        ConstantClass.PlayStore to listOf("com.android.vending"),
        ConstantClass.GoogleMaps to listOf("com.google.android.apps.maps"),
        ConstantClass.Files to listOf("com.google.android.documentsui"),
        ConstantClass.Calculator to listOf("com.google.android.calculator"),
        ConstantClass.Calendar to listOf("com.google.android.calendar"),
        ConstantClass.Contacts to listOf("com.google.android.contacts"),
        ConstantClass.Messages to listOf("com.google.android.apps.messaging"),
        ConstantClass.Phone to listOf("com.google.android.dialer"),
        ConstantClass.XTwitter to listOf("com.twitter.android"),
        ConstantClass.Amazon to listOf("in.amazon.mShop.android.shopping"),
        ConstantClass.Flipkart to listOf("com.flipkart.android"),
        ConstantClass.Netflix to listOf("com.netflix.mediaclient"),
        ConstantClass.Spotify to listOf("com.spotify.music")

    )*/

    private val hideunhideAppPackages = mapOf(
        ConstantClass.Gallery to listOf(
            // Google
            "com.google.android.apps.photos",

            // AOSP
            "com.android.gallery3d",

            // Samsung
            "com.sec.android.gallery3d",
            "com.samsung.android.gallery3d",

            // Xiaomi / Redmi / POCO
            "com.miui.gallery",

            // OPPO
            "com.coloros.gallery3d",
            "com.oppo.gallery3d",

            // OnePlus
            "com.oneplus.gallery",

            // vivo
            "com.vivo.gallery",

            // realme
            "com.coloros.gallery3d",

            // Huawei
            "com.huawei.photos",

            // Honor
            "com.hihonor.photos",

            // Motorola
            "com.motorola.camera3",
            "com.motorola.gallery",

            // Nokia (AOSP/Google Photos)
            "com.google.android.apps.photos",
            "com.android.gallery3d",

            // Sony
            "com.sonyericsson.album",

            // LG
            "com.lge.gallery",

            // ASUS
            "com.asus.gallery",

            // Lenovo
            "com.lenovo.gallery",

            // ZTE
            "com.zte.gallery3d",

            // Meizu
            "com.meizu.media.gallery",

            // Tecno / Infinix / itel
            "com.transsion.gallery",
            "com.transsion.phoenix.photos",

            // Nothing
            "com.google.android.apps.photos"
        ),
        ConstantClass.Chrome to listOf(
            // Stable
            "com.android.chrome",

            // Chrome Beta
            "com.chrome.beta",

            // Chrome Dev
            "com.chrome.dev",

            // Chrome Canary
            "com.chrome.canary",

            // Chromium (AOSP/Open Source)
            "org.chromium.chrome",
            "org.chromium.chrome.stable",

            // Bromite (Chromium-based)
            "org.bromite.bromite"
        ),
        ConstantClass.Gmail to listOf(
            // Gmail Stable
            "com.google.android.gm",

            // Gmail Go (Android Go devices)
            "com.google.android.gm.lite"
        ),
        ConstantClass.GooglePhotos to listOf(
            // Google Photos (Stable)
            "com.google.android.apps.photos",

            // Google Photos Go (Android Go devices)
            "com.google.android.apps.photosgo"
        ),
        ConstantClass.GoogleDrive to listOf(
            // Google Drive (Stable)
            "com.google.android.apps.docs",

            // Google Drive (legacy/older package variant)
            "com.google.android.apps.docs.editors.docs"
        ),
        ConstantClass.PlayStore to listOf("com.android.vending"),
        ConstantClass.GoogleMaps to listOf("com.google.android.apps.maps"),
        ConstantClass.Files to listOf(
            // Android AOSP / System Files picker
            "com.google.android.documentsui",

            // AOSP older versions
            "com.android.documentsui",

            // Google Files by Google
            "com.google.android.apps.nbu.files",

            // Samsung My Files
            "com.sec.android.app.myfiles",

            // Xiaomi / Redmi / POCO File Manager
            "com.mi.android.globalFileexplorer",
            "com.android.fileexplorer",

            // OPPO / Realme / OnePlus File Manager
            "com.coloros.filemanager",
            "com.oppo.filemanager",

            // Vivo File Manager
            "com.vivo.filemanager",

            // Huawei File Manager
            "com.huawei.hidisk",

            // Motorola File Manager
            "com.motorola.filemanager",

            // ASUS File Manager
            "com.asus.filemanager",

            // LG File Manager
            "com.lge.filemanager",

            // Lenovo File Manager
            "com.lenovo.FileBrowser",

            // ZTE File Manager
            "zte.com.market.filemanager"
        ),
        ConstantClass.Calculator to listOf(
            // Google Calculator
            "com.google.android.calculator",

            // AOSP Calculator
            "com.android.calculator2",

            // Samsung Calculator
            "com.sec.android.app.popupcalculator",

            // Xiaomi / Redmi / POCO Calculator
            "com.miui.calculator",

            // OPPO / Realme Calculator
            "com.coloros.calculator",

            // OnePlus Calculator
            "com.oneplus.calculator",

            // Vivo Calculator
            "com.vivo.calculator",

            // Huawei Calculator
            "com.android.calculator2",
            "com.huawei.calculator",

            // Motorola Calculator
            "com.motorola.calculator",

            // ASUS Calculator
            "com.asus.calculator",

            // LG Calculator
            "com.lge.calculator",

            // Lenovo Calculator
            "com.lenovo.calculator",

            // ZTE Calculator
            "zte.com.cn.calculator"
        ),
        ConstantClass.Calendar to listOf(
            // Google Calendar
            "com.google.android.calendar",

            // AOSP Calendar
            "com.android.calendar",

            // Samsung Calendar
            "com.samsung.android.calendar",

            // Xiaomi / Redmi / POCO Calendar
            "com.android.calendar",
            "com.miui.calendar",

            // OPPO / Realme Calendar
            "com.coloros.calendar",

            // OnePlus Calendar
            "com.oneplus.calendar",

            // Vivo Calendar
            "com.vivo.calendar",

            // Huawei Calendar
            "com.android.calendar",
            "com.huawei.calendar",

            // Motorola Calendar (usually Google Calendar)
            "com.google.android.calendar",

            // ASUS Calendar
            "com.asus.calendar",

            // Lenovo Calendar
            "com.lenovo.calendar"
        ),
        ConstantClass.Contacts to listOf(
            // Google Contacts
            "com.google.android.contacts",

            // AOSP Contacts
            "com.android.contacts",

            // Samsung Contacts
            "com.samsung.android.contacts",

            // Xiaomi / Redmi / POCO Contacts
            "com.android.contacts",
            "com.miui.contacts",

            // OPPO / Realme Contacts
            "com.coloros.contacts",

            // OnePlus Contacts
            "com.oneplus.contacts",

            // Vivo Contacts
            "com.android.contacts",
            "com.vivo.contacts",

            // Huawei Contacts
            "com.android.contacts",
            "com.huawei.contacts",

            // Motorola Contacts (usually Google)
            "com.google.android.contacts",

            // ASUS Contacts
            "com.asus.contacts",

            // Lenovo Contacts
            "com.lenovo.contacts",

            // ZTE Contacts
            "com.zte.contacts"
        ),
        ConstantClass.Messages to listOf(
                // Google Messages
            "com.google.android.apps.messaging",

        // AOSP Messaging
        "com.android.messaging",

        // Samsung Messages
        "com.samsung.android.messaging",

        // Xiaomi / Redmi / POCO Messages
        "com.android.mms",

        // OPPO / Realme Messages
        "com.android.mms",
        "com.coloros.mms",

        // OnePlus Messages
        "com.oneplus.mms",

        // Vivo Messages
        "com.android.mms",

        // Huawei Messages
        "com.android.mms",
        "com.huawei.message",

        // Motorola Messages (usually Google)
        "com.google.android.apps.messaging",

        // ASUS Messages
        "com.asus.message",

        // Lenovo Messages
        "com.lenovo.messaging",

        // ZTE Messages
        "com.zte.mms"
    ),
        ConstantClass.Phone to listOf(
            // Google Phone (Pixel and many GMS devices)
            "com.google.android.dialer",

            // AOSP Dialer
            "com.android.dialer",

            // Samsung Phone
            "com.samsung.android.dialer",

            // Xiaomi / Redmi / POCO Phone
            "com.android.dialer",
            "com.miui.phone",

            // OPPO / Realme Phone
            "com.android.dialer",
            "com.coloros.dialer",

            // OnePlus Phone
            "com.google.android.dialer",
            "com.oneplus.dialer",

            // Vivo Phone
            "com.android.dialer",
            "com.vivo.dialer",

            // Huawei Phone
            "com.android.dialer",
            "com.huawei.contacts",

            // Motorola Phone
            "com.google.android.dialer",

            // ASUS Phone
            "com.asus.dialer",

            // Lenovo Phone
            "com.lenovo.dialer",

            // ZTE Phone
            "com.zte.dialer"
        ),
        ConstantClass.XTwitter to listOf("com.twitter.android"),
        ConstantClass.Amazon to listOf(
            // Amazon Shopping (India)
            "in.amazon.mShop.android.shopping",

            // Amazon Shopping (Global/other regions)
            "com.amazon.mShop.android.shopping"
        ),
        ConstantClass.Flipkart to listOf("com.flipkart.android"),
        ConstantClass.Netflix to listOf("com.netflix.mediaclient"),
        ConstantClass.Spotify to listOf("com.spotify.music")

    )


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }




}