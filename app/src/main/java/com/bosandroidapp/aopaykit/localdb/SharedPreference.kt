package com.bosandroidapp.aopaykit.localdb

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

open class SharedPreference(mContext: Context) {
    val prefFile = mContext.packageName
    private val preferences: SharedPreferences = mContext.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor? = preferences.edit()


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mMStash: SharedPreference? = null

        @Synchronized
        fun getInstance(context: Context): SharedPreference? {
            if (mMStash == null) {
                mMStash = SharedPreference(context.applicationContext)
            }
            return mMStash
        }

    }


    fun setBooleanValue(keyFlag: String?, value: Boolean) {
        preferences.edit().putBoolean(keyFlag, value).apply()
    }


    fun getBoolanValue(keyFlag: String?, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(keyFlag, defaultValue)
    }


    fun setStringValue(keyFlag: String?, value: String) {
        preferences.edit().putString(keyFlag, value).apply()
    }


    fun getStringValue(keyFlag: String?, defaultValue: String): String {
        return preferences.getString(keyFlag, "")!!
    }



}