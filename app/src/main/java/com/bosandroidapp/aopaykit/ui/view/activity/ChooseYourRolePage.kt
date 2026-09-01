package com.bosandroidapp.aopaykit.ui.view.activity

import android.Manifest
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.Retailer
import com.bosandroidapp.aopaykit.constant.ConstantClass.loginType
import com.bosandroidapp.aopaykit.data.model.UploadDeviceInfoReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityChooseYourRolePageBinding
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.kioskmode.KioskDeviceAdminReceiver
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.network.google_auth.GoogleAuth
import com.bosandroidapp.aopaykit.ui.activity.LoginPage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import java.util.Locale

class ChooseYourRolePage : BaseActivity() {
    lateinit var binding : ActivityChooseYourRolePageBinding
    lateinit var viewModel: AuthenticationViewModel
    var SerialNumber: String = ""

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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hitApiForUploadCustomerDeviceInfo()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun intentNextPage(){
        loginType = ConstantClass.Customer
        binding.customerid.strokeColor = resources.getColor(R.color.darkpurple)
        binding.retailerid.strokeColor = resources.getColor(R.color.white)
        val mainIntent = Intent(this@ChooseYourRolePage, LoginPage::class.java)
        startActivity(mainIntent)
    }
    

    fun hitApiForUploadCustomerDeviceInfo(){
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        try {
            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ConstantClass.IMEI = try { telephonyManager.imei ?: "" } catch (e: Exception) { "" }
                }
            }

            val dpm = applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val admin = ComponentName(applicationContext, KioskDeviceAdminReceiver::class.java)

            if(dpm.isDeviceOwnerApp(packageName)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        SerialNumber = Build.getSerial()
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                } else {
                    @Suppress("DEPRECATION")
                    SerialNumber = Build.SERIAL
                }

                val simData = getSimIdentifiers(this)
                val iccid = simData.firstOrNull()?.iccId
                val subscriptionId = simData.firstOrNull()?.subscriptionId
                val carrierName = simData.firstOrNull()?.carrierName
                val mcc = simData.firstOrNull()?.mcc
                val mnc = simData.firstOrNull()?.mnc
                val slotIndex = simData.firstOrNull()?.slotIndex

                val request = UploadDeviceInfoReq(
                    manufacturer = Build.MANUFACTURER,
                    model = Build.MODEL,
                    brand = Build.BRAND,
                    serialNumber = SerialNumber,
                    osVersion = Build.VERSION.RELEASE,
                    sdkVersion = Build.VERSION.SDK_INT.toString(),
                    appVersion = packageManager.getPackageInfo(packageName, 0).versionName,
                    imeiNumber = ConstantClass.IMEI,
                    deviceName = Settings.Global.getString(contentResolver, Settings.Global.DEVICE_NAME) ?: Build.MODEL,
                    deviceID = androidId,
                    iccid = iccid,
                    subscriptionId = subscriptionId,
                    carrierName = carrierName,
                    mcc = mcc,
                    mnc = mnc,
                    slotIndex = slotIndex
                )

                Log.d("UploadDeviceInfoReq", Gson().toJson(request))

                viewModel.uploadDeviceInfo(request).observe(this) { resources ->
                    when (resources.apiStatus) {
                        ApiStatus.LOADING -> ConstantClass.OpenLoader(this)
                        ApiStatus.SUCCESS -> {
                            ConstantClass.dialog.dismiss()
                            resources.data?.body()?.let { response ->
                                if (response.status == "200" || response.status?.lowercase() == "true") {
                                    intentNextPage()
                                } else {
                                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        ApiStatus.ERROR -> {
                            ConstantClass.dialog.dismiss()
                            Toast.makeText(this, resources.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Kindly transfer the ownership to LockKit.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getSimIdentifiers(context: Context): List<SimInfo> {
        val subscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        val simList = mutableListOf<SimInfo>()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            subscriptionManager.activeSubscriptionInfoList?.forEach { sim ->
                simList.add(
                    SimInfo(
                        iccId = sim.iccId,
                        subscriptionId = sim.subscriptionId,
                        carrierName = sim.carrierName.toString(),
                        mcc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) sim.mccString else sim.mcc.toString(),
                        mnc = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) sim.mncString else sim.mnc.toString(),
                        slotIndex = sim.simSlotIndex
                    )
                )
            }
        }
        return simList
    }

    data class SimInfo(
        val iccId: String?,
        val subscriptionId: Int,
        val carrierName: String,
        val mcc: String?,
        val mnc: String?,
        val slotIndex: Int
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = GoogleAuth.onActivityResult(requestCode, data)
        if (result != null) {
            Log.d("GoogleAuth", result)
        }
    }
    
}
