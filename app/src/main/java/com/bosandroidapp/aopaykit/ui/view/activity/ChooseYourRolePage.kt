package com.bosandroidapp.aopaykit.ui.view.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.AppVersion
import com.bosandroidapp.aopaykit.databinding.ActivityChooseYourRolePageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass.Customer
import com.bosandroidapp.aopaykit.constant.ConstantClass.DeviceBrand
import com.bosandroidapp.aopaykit.constant.ConstantClass.DeviceName
import com.bosandroidapp.aopaykit.constant.ConstantClass.DeviceOSVersion
import com.bosandroidapp.aopaykit.constant.ConstantClass.Retailer
import com.bosandroidapp.aopaykit.constant.ConstantClass.deviceManufacturer
import com.bosandroidapp.aopaykit.constant.ConstantClass.deviceModel
import com.bosandroidapp.aopaykit.constant.ConstantClass.loginType
import com.bosandroidapp.aopaykit.data.model.UploadDeviceInfoReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.network.google_auth.GoogleAuth
import com.bosandroidapp.aopaykit.ui.slideshow.activity.LoginPage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson

class ChooseYourRolePage : AppCompatActivity() {
   lateinit var binding : ActivityChooseYourRolePageBinding
    lateinit var viewModel: AuthenticationViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseYourRolePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }
        GoogleAuth.initialize(this)
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

        setOnClickListner()

    }



    fun setOnClickListner(){

        binding.retailerid.setOnClickListener{
           // this.startActivityForAuth()
            loginType = Retailer
            binding.retailerid.strokeColor = resources.getColor(R.color.darkpurple)
            binding.customerid.strokeColor = resources.getColor(R.color.white)
            val mainIntent = Intent(this@ChooseYourRolePage, LoginPage::class.java)
            startActivity(mainIntent)
        }

        binding.customerid.setOnClickListener{
            if(!checkPermissionsrRetailer()){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 101)
            }
            else{
                hitApiForUploadCustomerDeviceInfo()
            }

        }

    }


    private fun checkPermissionsrRetailer(): Boolean {
        val phoneStatePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        return phoneStatePermission == PackageManager.PERMISSION_GRANTED
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Permissions Granted", Toast.LENGTH_SHORT).show()
                hitApiForUploadCustomerDeviceInfo()
            }
            else {
                Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show()
            }
        }

    }


    fun intentNextPage(){
        loginType = Customer
        binding.customerid.strokeColor = resources.getColor(R.color.darkpurple)
        binding.retailerid.strokeColor = resources.getColor(R.color.white)
        val mainIntent = Intent(this@ChooseYourRolePage, LoginPage::class.java)
        startActivity(mainIntent)

    }


    fun hitApiForUploadCustomerDeviceInfo(){
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        try {
            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            ConstantClass.IMEI = telephonyManager.imei
            // Toast.makeText(this, "IMEI: ${telephonyManager.imei}", Toast.LENGTH_LONG).show()
        }
        catch (e: Exception) {
           // Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
        }

        ConstantClass.deviceManufacturer = android.os.Build.MANUFACTURER
        deviceModel= android.os.Build.MODEL
        DeviceBrand = android.os.Build.BRAND
        DeviceOSVersion = android.os.Build.VERSION.RELEASE
        AppVersion = android.os.Build.VERSION.SDK_INT.toString()
        DeviceName = android.provider.Settings.Global.getString(contentResolver, android.provider.Settings.Global.DEVICE_NAME)

        Log.d("DeviceName", "$DeviceName")
        Log.d("DeviceInfo", "$deviceManufacturer $deviceModel")
        Log.d("DeviceInfo", "Android Version: $DeviceOSVersion (SDK $AppVersion) (brand $DeviceBrand)")


        var request = UploadDeviceInfoReq(
            appVersion = AppVersion,
            imeiNumber = ConstantClass.IMEI,
            osVersion = DeviceOSVersion,
            model = deviceModel,
            sdkVersion = AppVersion,
            deviceID = deviceId,
            brand = DeviceBrand,
            deviceName = DeviceName,
            manufacturer = deviceManufacturer,
        )
        Log.d("DeviceInfoReq", Gson().toJson(request))

        viewModel.uploadDeviceInfo(request).observe(this) { it ->

            when (it.apiStatus) {

                ApiStatus.LOADING -> {
                    ConstantClass.OpenPopUpForVeryfyOTP(this)
                }

                ApiStatus.SUCCESS -> {
                    ConstantClass.dialog.dismiss()
                    val response = it.data?.body()
                    Log.d("DeviceInfoResponse", Gson().toJson(response))

                    if (response != null && response.status.equals("200")) {
                        intentNextPage()
                    }
                    else {
                        Toast.makeText(this,"Kindly transfer the ownership to AO Pay.",Toast.LENGTH_SHORT).show()
                       // intentNextPage() // for testing purpose
                    }
                }

                ApiStatus.ERROR -> {
                    ConstantClass.dialog.dismiss()
                    // 👇 Show proper error from ViewModel (404, 500 etc.)
                    val errorMessage = it.message ?: "Something went wrong"
                    Log.e("LoginError", errorMessage)
                }

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        GoogleAuth.onActivityResult(requestCode, data)?.let { details ->
            Toast.makeText(this, details, Toast.LENGTH_LONG).show()
        }
    }



}