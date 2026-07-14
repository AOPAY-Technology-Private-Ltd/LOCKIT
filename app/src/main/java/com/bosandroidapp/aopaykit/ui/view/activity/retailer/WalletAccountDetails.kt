package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityWalletAccountDetailsBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.DataItem
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.view.activity.fragment.PayoutPage.Companion.CheckActiveStatus
import com.bosandroidapp.aopaykit.ui.view.activity.fragment.PayoutReports.Companion.reportType
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.reports.AllTransactionHistory
import com.bosandroidapp.aopaykit.ui.view.adapter.WalletPagerAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class WalletAccountDetails : AppCompatActivity() {
    lateinit var binding : ActivityWalletAccountDetailsBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference
    var reportDataList: MutableList<DataItem> = arrayListOf()
    lateinit var dialog: Dialog

    val statusArray = listOf("Payout", "Reports")
    lateinit var  viewPager: ViewPager2
    lateinit var  tabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityWalletAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }
        preference = SharedPreference(this)
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        reportType= "Payout"
        setView()
        setclickListner()

    }


    private fun setView(){

        viewPager = binding.viewPager
        tabLayout = binding.tablayout

        val adapter = WalletPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.isUserInputEnabled = true
        viewPager.adapter = adapter


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val tabView =  LayoutInflater.from(tabLayout.context).inflate(R.layout.tab_title, null)
            val text=tabView.findViewById<TextView>(R.id.tabText)
            text.text = statusArray[position]
            tab.customView= tabView
        }.attach()


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val textView = tab.customView as? TextView
                textView?.isSelected = true // triggers ColorStateList
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val textView = tab.customView as? TextView
                textView?.isSelected = false
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        // Also mark the initially selected tab (0)
        (tabLayout.getTabAt(tabLayout.selectedTabPosition)?.customView as? TextView)?.isSelected = true

    }


    fun setclickListner(){

        binding.back.setOnClickListener {
            CheckActiveStatus=false
            finish()
        }


        binding.history.setOnClickListener {
            startActivity(Intent(this@WalletAccountDetails, AllTransactionHistory::class.java))
        }


        binding.walletamounttxt.setOnClickListener{
            binding.walletamountcard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.purple))
            binding.holdamountcard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.walletamounttxt.setTextColor(resources.getColor(R.color.white))
            binding.holdamounttxt.setTextColor(resources.getColor(R.color.darkpurple))
            CheckActiveStatus = false
            reportType= "Payout"
            setView()

        }


        binding.holdamounttxt.setOnClickListener {
            binding.walletamountcard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
            binding.holdamountcard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.purple))
            binding.walletamounttxt.setTextColor(resources.getColor(R.color.darkpurple))
            binding.holdamounttxt.setTextColor(resources.getColor(R.color.white))
            CheckActiveStatus = true
            reportType= "Holding"
            setView()
        }


    }


    override fun onResume() {
        super.onResume()
        hitApiForLogin()
    }



    override fun onBackPressed() {
        CheckActiveStatus=false
        super.onBackPressed()
    }



    fun hitApiForLogin() {

        var sessionOutReq = SessionOutReq(
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
        )

        Log.d("SessionOutReq", Gson().toJson(sessionOutReq))

        viewModel.getSessionReq(sessionOutReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("SessionOutResponse", Gson().toJson(response))
                                if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                                    ConstantClass.dialog.dismiss()
                                }
                                ConstantClass.checkActiveStatusAndLogout(this@WalletAccountDetails, response.status, preference)
                            }
                        }
                    }

                    ApiStatus.ERROR -> {

                    }

                    ApiStatus.LOADING -> {

                    }
                }
            }
        }

        var request = ValidateSessionRequest(
            preference.getStringValue(ConstantClass.RetailerCode, ""),
            preference.getStringValue(ConstantClass.DEVICEID, ""),
            preference.getStringValue(ConstantClass.FCMTOKEN, "")
        )

        Log.d("validaterequest", Gson().toJson(request))
        viewModel.getSessionExpiredReq(request).observe(this){resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("validateresp", Gson().toJson(response))
                                if(response.status==0){
                                    hitApiForRetailerLogout()
                                }
                            }
                        }
                    }

                    ApiStatus.ERROR -> {

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
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("LogoutResponse", Gson().toJson(response))
                                preference.setBooleanValue(ConstantClass.LoggedIn, false)
                                preference.setStringValue(ConstantClass.LoginType, "")
                                ConstantClass.ClickOnCardDashboard = ""
                                val intent = Intent(this@WalletAccountDetails, ChooseYourRolePage::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                        }
                    }

                    ApiStatus.ERROR -> {

                    }

                    ApiStatus.LOADING -> {

                    }
                }
            }
        }

    }


}