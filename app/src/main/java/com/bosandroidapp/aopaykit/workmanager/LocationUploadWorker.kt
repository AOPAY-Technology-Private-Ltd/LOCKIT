package com.bosandroidapp.aopaykit.workmanager

import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.getPublicIpAddress
import com.bosandroidapp.aopaykit.data.customeraction.AppsItem
import com.bosandroidapp.aopaykit.data.customeraction.CategoriesItem
import com.bosandroidapp.aopaykit.data.customeraction.SendInstalledAppOnServerRequest
import com.bosandroidapp.aopaykit.data.model.CustomerlocationUploadReq
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.google.android.gms.common.wrappers.Wrappers.packageManager
import com.google.gson.Gson


class LocationUploadWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    private val preference: SharedPreference by lazy { SharedPreference.getInstance(applicationContext)!! }


    override suspend  fun doWork(): Result {
        return try {
            Log.d("dowork", "DoWork")
            val deviceIp = getPublicIpAddress()

            val lat = inputData.getDouble("LAT", 0.0)
            val long = inputData.getDouble("LONG", 0.0)

            val request = CustomerlocationUploadReq(
                taskType = "INS",
                locationAuditID = 0,
                latitude = lat,
                longitude = long,
                ipAddress = deviceIp,
                customerCode = preference.getStringValue(ConstantClass.CustomerCode, ""),
                retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
                loanCode = "",
                userName = preference.getStringValue(ConstantClass.CustomerMobileNumber, "")
            )

            Log.d("LocationWorkerReq", Gson().toJson(request))
            // Call API (suspend function preferred)
            RetrofitClient.apiInterface.uploadcustomerlocation(request)


            //  Upload Dynamic Installed Apps as well
            val sendInstalledAppRequest = SendInstalledAppOnServerRequest(
                clientCode = preference.getStringValue(ConstantClass.ClientCode, ""),
                createdBy = preference.getStringValue(ConstantClass.CustomerCode, ""),
                categories = ConstantClass.getInstalledApps(applicationContext.packageManager, applicationContext)
            )

            Log.d("InstallAppsWorkerReq", Gson().toJson(sendInstalledAppRequest))
            RetrofitClient.apiInterface.uploadCustomerDeviceInsatlledAppsOnServerRequest(sendInstalledAppRequest)

            // ✅ SAVE after success
            preference.setStringValue(ConstantClass.CUREENTLAT, lat.toString())
            preference.setStringValue(ConstantClass.CUREENTLONGG, long.toString())

            Result.success()
        }
        catch (e: Exception) {
            Log.e("LocationWorker", e.message ?: "")
            Result.retry()
        }
    }


   /* fun getCustomerAction():List <CategoriesItem> {

        val categoryList = mutableListOf<CategoriesItem>()
        val socialApps = mutableListOf<AppsItem>()
        val gamingApps = mutableListOf<AppsItem>()
        val videoApps = mutableListOf<AppsItem>()
        val audioApps = mutableListOf<AppsItem>()
        val imageApps = mutableListOf<AppsItem>()
        val mapApps = mutableListOf<AppsItem>()
        val newsApps = mutableListOf<AppsItem>()
        val productivityApps = mutableListOf<AppsItem>()
        val undefinedApps = mutableListOf<AppsItem>()

        val apps = applicationContext.packageManager.getInstalledApplications(0)
        val pm = applicationContext.packageManager

        apps.forEach { AppsItem ->

            val appName = applicationContext.packageManager.getApplicationLabel(AppsItem).toString()
            val packageName = AppsItem.packageName

            when (AppsItem.category) {

                ApplicationInfo.CATEGORY_SOCIAL -> {
                    socialApps.add(
                        AppsItem(
                            appName = appName,
                            packageName = packageName
                        )
                    )
                }

                ApplicationInfo.CATEGORY_GAME -> {
                    gamingApps.add(
                        AppsItem(
                            appName = appName,
                            packageName = packageName
                        )
                    )
                }

                ApplicationInfo.CATEGORY_AUDIO -> {
                    audioApps.add(
                        AppsItem(
                            appName = appName,
                            packageName = packageName
                        )
                    )
                }

                ApplicationInfo.CATEGORY_IMAGE -> {
                    imageApps.add(
                        AppsItem(
                            appName = appName,
                            packageName = packageName
                        )
                    )
                }

                ApplicationInfo.CATEGORY_MAPS -> {
                    mapApps.add(
                        AppsItem(
                            appName = appName,
                            packageName = packageName
                        )
                    )
                }

                ApplicationInfo.CATEGORY_NEWS -> {
                    newsApps.add(
                        AppsItem(
                            appName = appName,
                            packageName = packageName
                        )
                    )
                }

                ApplicationInfo.CATEGORY_PRODUCTIVITY -> {
                    productivityApps.add(
                        AppsItem(
                            appName = appName,
                            packageName = packageName
                        )
                    )
                }

                ApplicationInfo.CATEGORY_VIDEO -> {
                    videoApps.add(
                        AppsItem(
                            appName = appName,
                            packageName = packageName
                        )
                    )
                }

                ApplicationInfo.CATEGORY_UNDEFINED -> {
                    // Skip system apps
                    if ((AppsItem.flags and ApplicationInfo.FLAG_SYSTEM) != 0) {
                        return@forEach
                    }
                    if ((AppsItem.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                        return@forEach
                    }
                    // Skip apps without launcher icon
                    val launchIntent = pm.getLaunchIntentForPackage(packageName)
                    if (launchIntent == null) {
                        return@forEach
                    }

                    undefinedApps.add(
                        AppsItem(
                            appName = appName,
                            packageName = packageName
                        )
                    )

                }


            }

        }

        // Add categories

        categoryList.add(
            CategoriesItem(

                category = "Social",
                apps = socialApps
            )
        )

        categoryList.add(
            CategoriesItem(
                category = "Gaming",
                apps = gamingApps
            )
        )

        categoryList.add(
            CategoriesItem(
                category = "Audio",
                apps = audioApps
            )
        )

        categoryList.add(
            CategoriesItem(
                category = "Image",
                apps = imageApps
            )
        )

        categoryList.add(
            CategoriesItem(

                category = "Map",
                apps = mapApps
            )
        )

        categoryList.add(
            CategoriesItem(

                category = "News",
                apps = newsApps
            )
        )

        categoryList.add(
            CategoriesItem(

                category = "Video",
                apps = videoApps
            )
        )

        categoryList.add(
            CategoriesItem(

                category = "Productivity",
                apps = productivityApps
            )
        )

        categoryList.add(
            CategoriesItem(
                category = "Undefined",
                apps = undefinedApps
            )
        )

        Log.d("CategoryList", Gson().toJson(categoryList.toString()))

        // 3. Add System/Special Actions
        categoryList.add(CategoriesItem(category = "Disable Call", apps = emptyList()))
        val disabledSetting = mutableListOf(
            AppsItem(appName = ConstantClass.Bluetooth, packageName = ConstantClass.Bluetooth),
            AppsItem(appName = ConstantClass.Wifi, packageName = ConstantClass.Wifi),
            AppsItem(appName = ConstantClass.Hotspot, packageName = ConstantClass.Hotspot),
            AppsItem(appName = ConstantClass.USB, packageName = ConstantClass.USB),
        )
        categoryList.add(CategoriesItem(category = "Disable Settings", apps = disabledSetting))
        categoryList.add(CategoriesItem(category = "Kiosk Mode", apps = emptyList()))
        categoryList.add(CategoriesItem(category = "Disable Camera", apps = emptyList()))
        categoryList.add(CategoriesItem(category = "Reboot", apps = emptyList()))
        categoryList.add(CategoriesItem(category = "Airplane Mode", apps = emptyList()))
        return categoryList
    }*/

}
