package com.bosandroidapp.aopaykit.phonestatereceiver

import android.annotation.SuppressLint
import android.app.admin.DevicePolicyManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.AppVersion
import com.bosandroidapp.aopaykit.constant.ConstantClass.Customer
import com.bosandroidapp.aopaykit.constant.ConstantClass.DeviceBrand
import com.bosandroidapp.aopaykit.constant.ConstantClass.DeviceName
import com.bosandroidapp.aopaykit.constant.ConstantClass.DeviceOSVersion
import com.bosandroidapp.aopaykit.constant.ConstantClass.SerialNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.deviceManufacturer
import com.bosandroidapp.aopaykit.constant.ConstantClass.deviceModel
import com.bosandroidapp.aopaykit.data.model.UploadDeviceInfoReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

class SimStateReceiver : BroadcastReceiver() {

    private val authRepository by lazy { AuthRepository(RetrofitClient.apiInterface) }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        if (intent?.action == "android.intent.action.SIM_STATE_CHANGED") {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simState = telephonyManager.simState

            when (simState) {
                TelephonyManager.SIM_STATE_ABSENT -> {
                    Log.d("SimCardState", "SIM Removed")
                    val preference = SharedPreference(context)
                    val loginType = preference.getStringValue(ConstantClass.LoginType, "").orEmpty()
                    if(loginType.equals(ConstantClass.Retailer)){
                        performLogout(context)
                    }
                }
                TelephonyManager.SIM_STATE_READY -> {
                    Log.d("SimCardState", "SIM Ready")
                    val preference = SharedPreference(context)
                    val loginType = preference.getStringValue(ConstantClass.LoginType, "").orEmpty()
                    if(loginType.equals(ConstantClass.Customer)){
                        uploadDeviceInfo(context)
                    }

                }
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.S)
    private fun performLogout(context: Context) {
        val preference = SharedPreference(context)
        preference.setBooleanValue(ConstantClass.LoggedIn, false)
        preference.setStringValue(ConstantClass.LoginType, "")
        ConstantClass.ClickOnCardDashboard = ""

        val intent = Intent(context, ChooseYourRolePage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun uploadDeviceInfo(context: Context) {
        try {
            val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

            if (dpm.isDeviceOwnerApp(context.packageName)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        SerialNumber = Build.getSerial()
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                } else {
                    @Suppress("DEPRECATION")
                    SerialNumber = Build.SERIAL
                }

                val simData = getSimIdentifiers(context)
                val firstSim = simData.firstOrNull()

                deviceManufacturer = Build.MANUFACTURER
                deviceModel = Build.MODEL
                DeviceBrand = Build.BRAND
                DeviceOSVersion = Build.VERSION.RELEASE
                AppVersion = Build.VERSION.SDK_INT.toString()

                val request = UploadDeviceInfoReq(
                    appVersion = AppVersion,
                    imeiNumber = "",
                    osVersion = DeviceOSVersion,
                    model = deviceModel,
                    sdkVersion = AppVersion,
                    deviceID = "",
                    brand = DeviceBrand,
                    deviceName = "",
                    manufacturer = deviceManufacturer,
                    serialNumber = SerialNumber,
                    iccid = firstSim?.iccId,
                    subscriptionId = firstSim?.subscriptionId,
                    carrierName = firstSim?.carrierName,
                    mcc = firstSim?.mcc,
                    mnc = firstSim?.mnc,
                    slotIndex = firstSim?.slotIndex,
                )

                Log.d("DeviceInfoReq", Gson().toJson(request))

                runBlocking {
                    try {
                        val response = authRepository.uploadDeviceInfo(request)
                        if (response?.isSuccessful == true) {
                            Log.d("DeviceInfoResponse", Gson().toJson(response.body()))

                        } else {
                            Log.e("DeviceInfoError", "Failed to upload device info")

                        }
                    } catch (e: Exception) {
                        Log.e("DeviceInfoError", "Error: ${e.message}")

                    }
                }
            } else {

            }
        } catch (e: Exception) {
            Log.e("DeviceInfoException", "Error: ${e.message}")

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
                    slotIndex = sim.simSlotIndex
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
        val slotIndex: Int
    )


}
