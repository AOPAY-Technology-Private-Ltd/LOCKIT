package com.bosandroidapp.aopaykit.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.getPublicIpAddress
import com.bosandroidapp.aopaykit.data.model.CustomerlocationUploadReq
import com.bosandroidapp.aopaykit.localdb.SharedPreference
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


}
