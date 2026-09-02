package com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.loginType
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPlanListDataItem
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPlanRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.notification.NotificationSendTokenRequest
import com.bosandroidapp.aopaykit.data.pg.PGRequestCall
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.repository.PanRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.data.viewModelFactory.PanViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityLockKitPackageTopUpPageBinding
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.view.activity.customer.PGWebViewActivity
import com.bosandroidapp.aopaykit.ui.view.activity.customer.PGWebViewActivity.Companion.kitPlanListDataItem
import com.bosandroidapp.aopaykit.ui.view.adapter.KitPlanAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.ui.viewmodel.PanViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson

class LockKitPackageTopUpPage : BaseActivity() {
    private lateinit var binding: ActivityLockKitPackageTopUpPageBinding
    private lateinit var adapter: KitPlanAdapter

    lateinit var preference : SharedPreference
    private lateinit var viewModel: AuthenticationViewModel
    var packagedataLits: List<KitPlanListDataItem?>? = listOf()
    lateinit var panViewModel: PanViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLockKitPackageTopUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        panViewModel = ViewModelProvider(this, PanViewModelFactory(PanRepository(RetrofitClient.apiInterfacePAN)))[PanViewModel::class.java]

        preference = SharedPreference(this)

        binding.notfoundimage.visibility= View.VISIBLE
        binding.packageDataList.visibility = View.GONE
        binding.btnPayNow.visibility=View.GONE
        hitApiForGeetingKitPackage()

        setOnClickListner()

    }

    override fun onResume() {
        super.onResume()
        hitApiForLogin()
    }


    fun setOnClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.history.setOnClickListener {
            startActivity(Intent(this,KitPurchaseHistoryList::class.java))
        }

        binding.btnPayNow.setOnClickListener {
            if(ConstantClass.isInternetAvailable(this)){
                val amount = binding.tvSummaryTotal.text.toString().replace(Regex("[^0-9.]"), "")
                var req = PGRequestCall(
                    payCustomerPhoneNo = preference.getStringValue(ConstantClass.CustomerMobileNumber, ""),
                    customerEmailID = "bos.centerpvtltd@gmail.com",
                    registrationID = ConstantClass.PAN_VERIFICATION_REGISTRATION_ID,
                    payCartAmount = amount,
                    payCustomerName = "${preference.getStringValue(ConstantClass.FirstName, "")} ${preference.getStringValue(ConstantClass.LastName, "")}",
                    retailerCode = preference.getStringValue(ConstantClass.RetailerCode,"")
                )

                hitApiForRequestPG(req)
            }
            else{

                Toast.makeText(this,"Please connect with internet", Toast.LENGTH_SHORT).show()
            }

        }


    }

    fun hitApiForRequestPG(req : PGRequestCall){

        Log.d("PGRequest", Gson().toJson(req))

        panViewModel.getKitPGRequestCall(req).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        val response = it.data?.body()
                        if (it.data?.isSuccessful == true && response != null) {
                            Log.d("PanVerificationResp", Gson().toJson(response))

                            if (response!!.status?.toLowerCase().equals("true",ignoreCase = true) && !response.preparePOSTForm.isNullOrEmpty()) {
                                // Open WebView with the provided URL
                                ConstantClass.dialog.dismiss()
                                val intent = Intent(this@LockKitPackageTopUpPage, PGWebViewActivity::class.java)
                                intent.putExtra("kittopup", ConstantClass.KitPlan)
                                intent.putExtra("pgurl", response.preparePOSTForm)
                                startActivity(intent)
                            }
                            else {
                                ConstantClass.dialog.dismiss()
                                Toast.makeText(this@LockKitPackageTopUpPage, response.message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            ConstantClass.handleApiError(this@LockKitPackageTopUpPage, it.data?.code() ?: 0)
                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.handleApiFailure(this@LockKitPackageTopUpPage, it.message)
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }

                }

            }

        }

    }

    fun hitApiForGeetingKitPackage(){
        var request  = KitPlanRequest(
            companyCode = preference.getStringValue(ConstantClass.ClientCode,""),
            retailerCode = /*"AFD0035"*/ preference.getStringValue(ConstantClass.RetailerCode,"")
        )

        Log.d("requestKit",Gson().toJson(request))

        viewModel.kitPlanTopUpRequest(request).observe(this) { it ->
            when (it.apiStatus) {
                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }

                    ApiStatus.SUCCESS -> {
                        val response = it.data?.body()
                        if (it.data?.isSuccessful == true && response != null) {
                            ConstantClass.dialog.dismiss()
                            Log.d("ResponseKit", Gson().toJson(response))

                            if (response != null && response!!.status == true) {
                                packagedataLits = response.data!!
                                if(packagedataLits!!.size>0){
                                    binding.notfoundimage.visibility= View.GONE
                                    binding.btnPayNow.visibility=View.VISIBLE
                                    binding.packageDataList.visibility = View.VISIBLE
                                    setupAdapter(packagedataLits)

                                }else{
                                    binding.notfoundimage.visibility= View.VISIBLE
                                    binding.packageDataList.visibility = View.GONE
                                    binding.btnPayNow.visibility=View.GONE
                                }

                            } else {
                                binding.notfoundimage.visibility= View.VISIBLE
                                binding.packageDataList.visibility = View.GONE
                                binding.btnPayNow.visibility=View.GONE
                            }
                        } else {
                            ConstantClass.handleApiError(this@LockKitPackageTopUpPage, it.data?.code() ?: 0)
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.handleApiFailure(this@LockKitPackageTopUpPage, it.message)
                    }

                }

        }

    }


    private fun setupAdapter(packagedataLits: List<KitPlanListDataItem?>?) {

        val sortedList = packagedataLits?.sortedByDescending { it?.isMostPopular == true }

        if (!sortedList.isNullOrEmpty()) {
            sortedList.forEach { it?.isSelected = false }
            sortedList[0]?.isSelected = true
            updateSummary(sortedList[0]!!)
        }

        adapter = KitPlanAdapter(sortedList) { plan ->
            updateSummary(plan)
        }

        binding.rvPlans.adapter = adapter

    }


    private fun updateSummary(plan: KitPlanListDataItem) {
        val planAmount = plan.planAmount ?: 0.0
        val discountAmount = plan.discountAmount ?: 0.0
        val subtotal = planAmount - discountAmount
        val gstAmount = plan.gstAmount ?: 0.0
        val totalAmount = plan.totalAmount ?: 0.0

        binding.tvSummaryPlanPrice.text = "₹${String.format("%,.2f", planAmount)}"

        binding.tvSummaryDiscountLabel.text = "Discount (${plan.discountPercent?.toInt() ?: 0}%)"
        binding.tvSummaryDiscount.text = "- ₹${String.format("%,.2f", discountAmount)}"

        binding.tvSummarySubtotal.text = "₹${String.format("%,.2f", subtotal)}"

        binding.tvSummaryGstLabel.text = "GST (${plan.gstPercent?.toInt() ?: 0}%)"
        binding.tvSummaryGst.text = "+ ₹${String.format("%,.2f", gstAmount)}"

        binding.tvSummaryTotal.text = "₹${String.format("%,.2f", totalAmount)}"

        kitPlanListDataItem = plan

    }


    fun hitApiForLogin() {

        var deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        preference.setStringValue(ConstantClass.DEVICEID,deviceId)

        var sessionOutReq = SessionOutReq(
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
            clientCode = preference.getStringValue(ConstantClass.ClientCode, "")
        )

        Log.d("SessionOutReq", Gson().toJson(sessionOutReq))

        viewModel.getSessionReq(sessionOutReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        val response = it.data?.body()
                        if (it.data?.isSuccessful == true && response != null) {
                            Log.d("SessionOutResponse", Gson().toJson(response))
                            if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                                ConstantClass.dialog.dismiss()
                            }
                            ConstantClass.checkActiveStatusAndLogout(this@LockKitPackageTopUpPage, response.status, preference)
                        } else {
                            ConstantClass.handleApiError(this@LockKitPackageTopUpPage, it.data?.code() ?: 0)
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.handleApiFailure(this@LockKitPackageTopUpPage, it.message)
                    }

                    ApiStatus.LOADING -> {

                    }
                }
            }
        }


        var request = ValidateSessionRequest(
            preference.getStringValue(ConstantClass.RetailerCode, ""),
            deviceId,
            preference.getStringValue(ConstantClass.FCMTOKEN, "")
        )

        Log.d("validaterequest", Gson().toJson(request))
        viewModel.getSessionExpiredReq(request).observe(this){resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        val response = it.data?.body()
                        if (it.data?.isSuccessful == true && response != null) {
                            Log.d("validateresp", Gson().toJson(response))
                            if(response.status==0){
                                hitApiForRetailerLogout()
                            }
                        } else {
                            ConstantClass.handleApiError(this@LockKitPackageTopUpPage, it.data?.code() ?: 0)
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.handleApiFailure(this@LockKitPackageTopUpPage, it.message)
                    }

                    ApiStatus.LOADING -> {

                    }
                }
            }
        }

    }


    fun hitApiForRetailerLogout() {
        var loginRequest = LogoutReq(
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
        )

        Log.d("LogoutReq", Gson().toJson(loginRequest))

        viewModel.getLogout(loginRequest).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        val response = it.data?.body()
                        if (it.data?.isSuccessful == true && response != null) {
                            Log.d("LogoutResponse", Gson().toJson(response))
                            preference.setBooleanValue(ConstantClass.LoggedIn, false)
                            preference.setStringValue(ConstantClass.LoginType, "")
                            ConstantClass.ClickOnCardDashboard = ""
                            val intent = Intent(this@LockKitPackageTopUpPage, ChooseYourRolePage::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            ConstantClass.handleApiError(this@LockKitPackageTopUpPage, it.data?.code() ?: 0)
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.handleApiFailure(this@LockKitPackageTopUpPage, it.message)
                    }

                    ApiStatus.LOADING -> {

                    }
                }
            }
        }

    }


}
