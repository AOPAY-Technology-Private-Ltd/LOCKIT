package com.bosandroidapp.aopaykit.utils

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel

class ApplicationClass : Application() {

    lateinit var preference: SharedPreference
    lateinit var viewModel: AuthenticationViewModel
    lateinit var FcmToken: String
    lateinit var deviceId: String
    lateinit var retailerCode : String

    override fun onCreate() {
        super.onCreate()

        Log.d("ApplicationClass", "Application started")

        // Create default notification channel once
        createNotificationChannel()


        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }




    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("CHANNEL_ID", "General Notifications", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Channel for app notifications"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

}