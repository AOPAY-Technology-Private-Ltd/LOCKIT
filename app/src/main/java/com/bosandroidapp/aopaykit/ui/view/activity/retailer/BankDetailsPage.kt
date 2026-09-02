package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.databinding.ActivityBankDetailsPageBinding
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.view.adapter.BankPagerAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class BankDetailsPage : BaseActivity() {
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference
    lateinit var binding : ActivityBankDetailsPageBinding
    val statusArray = listOf("Accounts", "Add Bank")
    lateinit var  viewPager: ViewPager2
    lateinit var  tabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBankDetailsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        preference = SharedPreference(this)
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

        setView()
        setclickListner()

    }

    override fun onResume() {
        super.onResume()
        hitApiForLogin()
    }

    private fun setView(){

        viewPager = binding.viewPager
        tabLayout = binding.tablayout

        val adapter = BankPagerAdapter(supportFragmentManager, lifecycle)
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
            finish()
        }


    }


    fun hitApiForLogin() {

        var sessionOutReq = SessionOutReq(
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
            clientCode = preference.getStringValue(ConstantClass.ClientCode, "")
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
                                ConstantClass.checkActiveStatusAndLogout(this@BankDetailsPage, response.status, preference)
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
                                val intent = Intent(this@BankDetailsPage, ChooseYourRolePage::class.java)
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