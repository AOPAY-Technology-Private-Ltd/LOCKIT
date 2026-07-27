package com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit

import android.content.Intent
import android.os.Bundle
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
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPlanListDataItem
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPlanRequest
import com.bosandroidapp.aopaykit.data.notification.NotificationSendTokenRequest
import com.bosandroidapp.aopaykit.data.pg.PGRequestCall
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.repository.PanRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.data.viewModelFactory.PanViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityLockKitPackageTopUpPageBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.slideshow.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.view.activity.customer.PGWebViewActivity
import com.bosandroidapp.aopaykit.ui.view.activity.customer.PGWebViewActivity.Companion.kitPlanListDataItem
import com.bosandroidapp.aopaykit.ui.view.adapter.KitPlanAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.ui.viewmodel.PanViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson

class LockKitPackageTopUpPage : AppCompatActivity() {
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

        hitApiForGeetingKitPackage()

        setOnClickListner()

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
                    payCartAmount = /*amount*/"1",
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
                        it.data.let { users ->
                            users!!.body().let { response ->
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

                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
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
            companyCode = ConstantClass.ClientCode,
            retailerCode = /*preference.getStringValue(ConstantClass.RetailerCode,"")*/  "RTL000028"
        )
        Log.d("requestKit",Gson().toJson(request))

        viewModel.kitPlanTopUpRequest(request).observe(this) { it ->
            when (it.apiStatus) {
                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }

                    ApiStatus.SUCCESS -> {
                        ConstantClass.dialog.dismiss()
                        val response = it.data?.body()
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

    private fun setupAdapter(packagedataLits: List<KitPlanListDataItem?>?) {

        if (!packagedataLits.isNullOrEmpty()) {
            packagedataLits.forEach { it?.isSelected = false }
            packagedataLits[0]?.isSelected = true
            updateSummary(packagedataLits[0]!!)
        }

        adapter = KitPlanAdapter(packagedataLits) { plan ->
            updateSummary(plan)
        }

        binding.rvPlans.adapter = adapter

    }

    private fun updateSummary(plan: KitPlanListDataItem) {
        binding.tvSummaryPlanName.text = "${plan.noOfKits} Kits"
        binding.tvSummaryPlanPrice.text = "₹${String.format("%,.0f", plan.planAmount)}"
        binding.tvSummaryGst.text = "₹${String.format("%,.0f", plan.gstAmount)}"
        binding.tvSummaryTotal.text = "₹${String.format("%,.0f", plan.totalAmount)}"
        kitPlanListDataItem= plan
    }


}
