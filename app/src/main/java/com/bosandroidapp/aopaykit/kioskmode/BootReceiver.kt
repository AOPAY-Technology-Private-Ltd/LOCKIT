package com.bosandroidapp.aopaykit.kioskmode

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.localdb.SharedPreference


class   BootReceiver : BroadcastReceiver() {

     lateinit var preference: SharedPreference

    public override fun onReceive(context: Context, intent: Intent?) {
        preference = SharedPreference.getInstance(context)!!

        if (intent!!.action == Intent.ACTION_BOOT_COMPLETED ||intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED ||
            intent.action==Intent.ACTION_MY_PACKAGE_REPLACED) {
            if(preference.getBoolanValue(ConstantClass.IS_KIOSK_ENABLED,false))
            {
                val launchIntent = Intent(context, KioskLockPage::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                context.startActivity(launchIntent)
            }

        }

    }

}