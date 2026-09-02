package com.bosandroidapp.aopaykit.phonestatereceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.customeraction.CustomerSideUpdateUnInstallAppRequest
import com.bosandroidapp.aopaykit.data.customeraction.SendInstalledAppOnServerRequest
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking

class PackageReceiver : BroadcastReceiver() {

    lateinit var preference: SharedPreference

    private val authRepository by lazy { AuthRepository(RetrofitClient.apiInterface) }



    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        preference = SharedPreference(context)

        val action = intent.action
        val packageName = intent.data?.schemeSpecificPart

        Log.d("PackageReceiver", "Action: $action, Package: $packageName")

        if (action == Intent.ACTION_PACKAGE_REMOVED) {
            val isReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)
            val isDataRemoved = intent.getBooleanExtra(Intent.EXTRA_DATA_REMOVED, false)
            
            // Only proceed if it's an actual uninstall (data removed) and not part of an update (replacing)
            if (!isReplacing && isDataRemoved) {
                Log.d("PackageReceiverUninstall", "App Uninstalled: $packageName")
                hitApiForUploadUninstallAppStatus(context, packageName ?: "")
                hitApiForUploadDynamicInstallApps(context)
            }

        }
        else if (action == Intent.ACTION_PACKAGE_ADDED) {
            Log.d("PackageReceiverAdd", "App Installed: $packageName")
            //hitApiForUploadUninstallAppStatus(context, packageName ?: "")
            hitApiForUploadDynamicInstallApps(context)
        }

    }


    private fun hitApiForUploadUninstallAppStatus(context: Context, uninstalledPackageName: String) {
        val preference = SharedPreference.getInstance(context) ?: return
        val loginType = preference.getStringValue(ConstantClass.LoginType, "")

        if (loginType != ConstantClass.Customer) return

        val updaterequest = CustomerSideUpdateUnInstallAppRequest(
            clientCode = preference.getStringValue(ConstantClass.ClientCode,""),
            appName = "", 
            eventTime = ConstantClass.getCurrentStartDate(),
            customerCode = preference.getStringValue(ConstantClass.CustomerCode, ""),
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
            packageName = uninstalledPackageName
        )

        runBlocking {
            try {
                val response = authRepository.updateAppUninstallStatusReq(updaterequest)
                if (response?.isSuccessful == true) {
                    Log.d("PackageReceiver", "Uninstall status uploaded: ${Gson().toJson(response.body())}")
                }else{

                }
            } catch (e: Exception) {
                Log.e("PackageReceiver", "Error uploading uninstall status: ${e.message}")
            }
        }
    }


    private fun hitApiForUploadDynamicInstallApps(context: Context) {
        val preference = SharedPreference.getInstance(context) ?: return
        val loginType = preference.getStringValue(ConstantClass.LoginType, "")

        if (loginType != ConstantClass.Customer) return

        val sendInstalledAppOnServerRequest = SendInstalledAppOnServerRequest(
            clientCode = preference.getStringValue(ConstantClass.ClientCode, ""),
            createdBy = preference.getStringValue(ConstantClass.CustomerCode, ""),
            categories = ConstantClass.getInstalledApps(context.packageManager, context)
        )

        runBlocking {
            try {
                val response = authRepository.uploadCustomerDeviceInsatlledAppsOnServerRequest(sendInstalledAppOnServerRequest)
                if (response?.isSuccessful == true) {
                    Log.d("PackageReceiver", "App Inventory Uploaded: ${Gson().toJson(response.body())}")
                }else {

                }
            } catch (e: Exception) {
                Log.e("PackageReceiver", "Error uploading inventory: ${e.message}")
            }
        }

    }

}
