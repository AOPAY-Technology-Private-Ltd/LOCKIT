package com.bosandroidapp.aopaykit.notification

import android.content.Intent
import androidx.core.content.ContextCompat
import com.bosandroidapp.aopaykit.kioskmode.KioskPolicyService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {



    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        val action = remoteMessage.data["action"]

        val intent = Intent(this, KioskPolicyService::class.java).apply {
            putExtra("action", action)
        }
        ContextCompat.startForegroundService(this, intent)

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

}