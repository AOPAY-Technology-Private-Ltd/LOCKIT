package com.bosandroidapp.aopaykit.phonestatereceiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.Customer
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage

class SimStateReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == "android.intent.action.SIM_STATE_CHANGED") {

            val telephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simState = telephonyManager.simState

            when (simState) {
                TelephonyManager.SIM_STATE_ABSENT -> {
                    Log.d("SimCardState", "SIM Removed")
                    logoutUser(context)
                }
                TelephonyManager.SIM_STATE_READY -> {
                    Log.d("SimCardState", "SIM Ready")
                }
            }
        }



    }


    private fun logoutUser(context: Context) {
        val preference = SharedPreference(context)
        var logintype = preference.getStringValue(ConstantClass.LoginType, "").orEmpty()

        if(logintype.equals(Customer)){

        }
        else {
            preference.setBooleanValue(ConstantClass.LoggedIn, false)
            preference.setStringValue(ConstantClass.LoginType, "")
            ConstantClass.ClickOnCardDashboard = ""

            val intent = Intent(context, ChooseYourRolePage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

    }

}
