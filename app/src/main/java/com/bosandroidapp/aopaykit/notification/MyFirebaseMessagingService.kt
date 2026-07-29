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

      /*var title = remoteMessage.notification?.title
        var body = remoteMessage.notification?.body*/

        var payload = remoteMessage.data["payload"]
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
                     }
                     else{
                         dpm.clearUserRestriction(admin, UserManager.DISALLOW_OUTGOING_CALLS)
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

                        val policy = FactoryResetProtectionPolicy.Builder()
                            .setFactoryResetProtectionAccounts(emptyList())
                            .build()

                        dpm.setFactoryResetProtectionPolicy(admin, policy)
                        dpm.clearUserRestriction(admin, UserManager.DISALLOW_FACTORY_RESET)
                        dpm.clearDeviceOwnerApp(getPackageName())
                        dpm.removeActiveAdmin(admin)

                        if(rid>-1){
                            hitApiForUpdateActionStatus("")
                        }

                    }

                }

                ConstantClass.GETLOCATION ->{
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


    @RequiresApi(Build.VERSION_CODES.Q)
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
                    mcc = sim.mccString,
                    mnc = sim.mncString,
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


    @RequiresApi(Build.VERSION_CODES.Q)
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

        if(simData.firstOrNull()?.iccId!!.isEmpty() || simData.firstOrNull()?.carrierName!!.isEmpty()){
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


    fun hitApiForUploadLocation(lat: Double, lng: Double) {
        val request = UploadCustomerLocationRequest(
            clientCode = ConstantClass.ClientCode,
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
            val appName = app.optString("PackageName")
            val action = app.optString("Action")

            updateAppStatus(dpm, admin, appName, action)
        }

    }


    private fun handleHideApps(dpm: DevicePolicyManager, admin: ComponentName, json: JSONObject) {
        val appActions = json.optJSONArray("AppActions") ?: return

        for (index in 0 until appActions.length()) {
            val app = appActions.getJSONObject(index)
            val appName = app.optString("PackageName")
            val action = app.optString("Action")

            hideUnhideAppStatus(dpm, admin, appName, action)
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


    private fun updateAppStatus(dpm: DevicePolicyManager, admin: ComponentName, appName: String, action: String) {
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
    }


    private fun hideUnhideAppStatus(dpm: DevicePolicyManager, admin: ComponentName, appName: String, action: String) {

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

    }


    private val appPackages = mapOf(
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
    )

    private val hideunhideAppPackages = mapOf(
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

    )


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


}