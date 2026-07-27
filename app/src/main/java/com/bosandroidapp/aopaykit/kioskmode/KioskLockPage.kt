package com.bosandroidapp.aopaykit.kioskmode

import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.loginsignup.RetailerProfileReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityKioskLockPageBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus

class KioskLockPage : AppCompatActivity() {

    private lateinit var binding: ActivityKioskLockPageBinding
    private lateinit var preference: SharedPreference
    private lateinit var viewModel: AuthenticationViewModel

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent?.action == "EXIT_KIOSK") {
            stopLockTask()      // Exit kiosk mode
            finish()            // Close this activity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKioskLockPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = SharedPreference.getInstance(this)!!
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

        setupKioskMode()
        fetchRetailerDetails()

    }

    private fun setupKioskMode() {
        val dpm = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val admin = ComponentName(this, KioskDeviceAdminReceiver::class.java)
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        if (dpm.isDeviceOwnerApp(packageName)) {
            dpm.setLockTaskPackages(admin, arrayOf(packageName))
            if (activityManager.lockTaskModeState == ActivityManager.LOCK_TASK_MODE_NONE) {
                startLockTask()
            }
        }
    }

    private fun fetchRetailerDetails() {
        val retailerCode = preference.getStringValue(ConstantClass.RetailerCode, "")
        if (retailerCode.isEmpty()) return

        val req = RetailerProfileReq(
            mode = "GET",
            customerType = "Retailer",
            customerCode = retailerCode,
            firstName = "",
            lastName = "",
            mobileNo = "",
            emailid = "",
            address = "",
            aadharNumber = "",
            panNumber = "",
            activeStatus = ""
        )

        viewModel.getRetailerProfileReq(req).observe(this) { resources ->
            when (resources.apiStatus) {
                ApiStatus.SUCCESS -> {
                    resources.data?.body()?.let { response ->
                        if (response.statuss == "True") {
                            binding.tvRetailerName.text = "${response.firstName} ${response.lastName}"
                            binding.tvRetailerPhone.text = response.mobileNo
                        }
                    }
                }
                else -> {}
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Disable back button in kiosk mode
        // super.onBackPressed() // Do not call super to disable back button
    }
}
