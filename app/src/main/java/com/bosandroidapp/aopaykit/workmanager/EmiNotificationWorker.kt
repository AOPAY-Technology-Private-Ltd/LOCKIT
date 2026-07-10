package com.bosandroidapp.aopaykit.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.customer.CustomerEMIPage
import com.bosandroidapp.aopaykit.utils.MonthsAndPayables
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit


class EmiNotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private val preference: SharedPreference by lazy { SharedPreference.getInstance(applicationContext)!! }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        Log.d("EMI_WORKER", "Worker Invoked")

        val emiList = getStoredEmiList()
        val today = System.currentTimeMillis()

        emiList.forEach { dueItem ->
            val dueMillis = convertDateToMillis(dueItem.dueDateWithGross)
            val diffDays = TimeUnit.MILLISECONDS.toDays(dueMillis - today)

            if (diffDays in 1..3) {
                // Show notification immediately
                showNotification(
                    "Loan EMI Alert",
                    "Your EMI is due in $diffDays days. Please pay on time.",
                    dueMillis.hashCode() // unique id per EMI
                )

                // Schedule next worker for the exact due date
                setupEmiWorkManager(dueMillis)
            }

        }

        // Optionally schedule this worker to check again later
        scheduleNextWork()

        return Result.success()
    }


    private fun scheduleNextWork() {
        val request = OneTimeWorkRequestBuilder<EmiNotificationWorker>().setInitialDelay(6, TimeUnit.HOURS) // 4 times a day
            .build()

        WorkManager.getInstance(applicationContext).enqueue(request)
    }


    private fun showNotification(title: String, message: String, notificationId: Int) {
        val channelId = "emi_alert_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "EMI Alerts", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, CustomerEMIPage::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.newapplogo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, builder.build())
    }



    private fun getStoredEmiList(): List<MonthsAndPayables> {
        val jsonString = preference.getStringValue(ConstantClass.EMILIST, "")
        if (jsonString.isNullOrBlank()) return emptyList()
        return try {
            val type = object : TypeToken<List<MonthsAndPayables>>() {}.type
            Gson().fromJson(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            Log.e("EMI_WORKER", "Failed to parse EMI list", e)
            emptyList()
        }
    }


    private fun convertDateToMillis(date: String): Long {
        return try {
            val format = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            format.parse(date)?.time ?: 0L
        } catch (e: Exception) {
            Log.e("EMI_WORKER", "Invalid date format: $date", e)
            0L
        }
    }


    private fun setupEmiWorkManager(dueMillis: Long) {
        val delay = dueMillis - System.currentTimeMillis()
        if (delay <= 0) return

        val data = androidx.work.Data.Builder().putLong("due_date", dueMillis).build()

        val request = OneTimeWorkRequestBuilder<EmiNotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(request)
    }


}

