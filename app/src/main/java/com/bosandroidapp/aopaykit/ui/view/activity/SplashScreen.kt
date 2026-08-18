package com.bosandroidapp.aopaykit.ui.slideshow.activity

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.databinding.SplashMainBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadharTransactionIdNo
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.data.customeraction.AppsItem
import com.bosandroidapp.aopaykit.data.customeraction.CategoriesItem
import com.bosandroidapp.aopaykit.data.customeraction.SendInstalledAppOnServerRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.AadharVerificationReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.AadharCardWebViewDIGILockerPage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.AadharCardWebViewDIGILockerPage.Companion.digilockerLink
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson


class SplashScreen : AppCompatActivity() {
    lateinit var binding: SplashMainBinding
    lateinit var preference: SharedPreference
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            supportActionBar?.hide()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        binding = SplashMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preference = SharedPreference(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        binding.uattext.visibility= View.GONE

        Handler(Looper.getMainLooper()).postDelayed({

            if (!isInternetAvailable(this)) {
                Snackbar.make(findViewById(android.R.id.content),
                    "No internet connection. Please check your network.",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Retry") {
                    val intent = Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }.show()
            }
            else {

                // Code to run after delay
                if (preference.getBoolanValue(ConstantClass.LoggedIn, false)) {
                    val mainIntent = Intent(this@SplashScreen, DashBoard::class.java)
                    startActivity(mainIntent)
                    finish()
                }
                else {
                    val mainIntent = Intent(this@SplashScreen, ChooseYourRolePage::class.java)
                    startActivity(mainIntent)
                    finish()
                }
            }



        }, 3000)


    }





}