package com.bosandroidapp.aopaykit.utils

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.phonestatereceiver.PackageReceiver
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ApplicationClass : Application() {

    lateinit var preference: SharedPreference
    lateinit var viewModel: AuthenticationViewModel
    lateinit var FcmToken: String
    lateinit var deviceId: String
    lateinit var retailerCode : String
    
    companion object {
        val isNetworkAvailable = MutableStateFlow(false)
    }

    override fun onCreate() {
        super.onCreate()
        
        isNetworkAvailable.value = ConstantClass.isInternetAvailable(this)

        Log.d("ApplicationClass", "Application started")

        // Create default notification channel once
        createNotificationChannel()
        
        // Register PackageReceiver
        registerPackageReceiver() // for check which app is uninstall from device


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

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isNetworkAvailable.value = true
            }

            override fun onLost(network: Network) {
               isNetworkAvailable.value = false
            }
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

    private fun registerPackageReceiver() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
        }
        registerReceiver(PackageReceiver(), filter)
    }



}