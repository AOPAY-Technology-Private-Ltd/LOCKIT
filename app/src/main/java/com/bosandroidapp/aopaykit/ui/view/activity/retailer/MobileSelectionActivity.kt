package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityMobileselectionBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.DataItem
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.slideshow.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.slideshow.adapter.MobileListAdapter
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.bosandroidapp.aopaykit.utils.GridSpacingItemDecoration
import com.google.gson.Gson

class MobileSelectionActivity : BaseActivity() {

    lateinit var binding : ActivityMobileselectionBinding
    lateinit var adapter : MobileListAdapter

    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference
    lateinit var dialog: Dialog


    companion object{
        var MobileList : MutableList<com.bosandroidapp.aopaykit.ui.view.model.MobileListModel> = mutableListOf()
        var MobileDataList : MutableList<DataItem> = mutableListOf()
        var FilterDataList : MutableList<DataItem> = mutableListOf()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMobileselectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)

        val spacingInPixels = 5   // or just use `5` in dp
        binding.showingMobileList.layoutManager = GridLayoutManager(this, 2)
        binding.showingMobileList.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true))

        setClickListner()
        hitApiForGetMobileDataList()

    }


    override fun onResume() {
        super.onResume()

        hitApiForLogin()

    }


    fun setClickListner(){


        binding.synchicon.setOnClickListener {
            hitApiForGetMobileDataList()
        }

        binding.home.setOnClickListener {
            val intent = Intent(this, DashBoard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            onBackPressed()
        }

        binding.back.setOnClickListener {
            OpenPopUpForVAlert()
        }

        binding.searchIcon.setOnClickListener {
            binding.searcMobile.requestFocus()
            binding.searcMobile.text.clear()
            // Show the keyboard
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searcMobile, InputMethodManager.SHOW_IMPLICIT)

        }


        binding.searcMobile.addTextChangedListener(
            object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val search = s.toString().lowercase().trim()
                    val result = MobileDataList.filter {
                        it.brandName.lowercase().contains(search) || it.modelName.lowercase().contains(search)
                    }
                    FilterDataList.clear()
                    FilterDataList.addAll(result)
                    setViewData(FilterDataList)
                    adapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            }
        )

    }


    fun setViewData(MobileDataList : MutableList<DataItem>){
        adapter = MobileListAdapter(MobileDataList,this@MobileSelectionActivity)
        binding.showingMobileList.adapter = adapter

    }


    fun hitApiForGetMobileDataList(){
        viewModel.getMobileList().observe(this){ resources->resources.let {
            when(it.apiStatus){
                ApiStatus.SUCCESS -> {
                    it.data?.let { users ->
                        users.body()?.let { response ->
                            ConstantClass.dialog.dismiss()
                            Log.d("MobileRes", response.message)
                            if(response.status.equals("True")){
                                MobileDataList = response.data!!
                                Log.d("List",Gson().toJson(MobileDataList))
                                setViewData(MobileDataList)
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


    fun addMobileList() {
        MobileList.clear()

        val colorList1 = arrayListOf(
            ContextCompat.getColor(this, R.color.black),
            ContextCompat.getColor(this, R.color.lightblue),
            ContextCompat.getColor(this, R.color.lightpink)
        )
        MobileList.add(
            com.bosandroidapp.aopaykit.ui.view.model.MobileListModel(
                R.drawable.iphone,
                "Apple iPhone 16",
                "₹ 79,000 (128GB)",
                colorList1
            )
        )

        val colorList2 = arrayListOf(
            ContextCompat.getColor(this, R.color.black),
            ContextCompat.getColor(this, R.color.skyblue),
            ContextCompat.getColor(this, R.color.darkred)
        )

        MobileList.add(
            com.bosandroidapp.aopaykit.ui.view.model.MobileListModel(
                R.drawable.samsung,
                "Samsung",
                "₹ 30,000 (64GB)",
                colorList2
            )
        )


        val colorList3 = arrayListOf(
            ContextCompat.getColor(this, R.color.black),
            ContextCompat.getColor(this, R.color.lightgrey),
            ContextCompat.getColor(this, R.color.lightgreen)
        )

        MobileList.add(
            com.bosandroidapp.aopaykit.ui.view.model.MobileListModel(
                R.drawable.oneplus,
                "OnePlus",
                "₹ 59,000 (64GB)",
                colorList3
            )
        )


        val colorList4 = arrayListOf(
            ContextCompat.getColor(this, R.color.black),
            ContextCompat.getColor(this, R.color.blue),
            ContextCompat.getColor(this, R.color.teal700)
        )

        
        MobileList.add(
                com.bosandroidapp.aopaykit.ui.view.model.MobileListModel(
                R.drawable.realme,
                "Realme",
                "₹ 20,000 (64GB)",
                colorList4
            )
        )

    }


    @SuppressLint("SetTextI18n")
    fun OpenPopUpForVAlert() {
        dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.signoutalert)


        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }


        dialog.setCanceledOnTouchOutside(false)

        val cancel = dialog.findViewById<Button>(R.id.btnCancel)
        val done = dialog.findViewById<Button>(R.id.btnLogout)
        val txt = dialog.findViewById<TextView>(R.id.dialog_message)
        val image = dialog.findViewById<ImageView>(R.id.imageview)

        image.visibility = View.VISIBLE

        done.text = "OK"

        txt.text = "Are you sure you want to go back?"

        done.setOnClickListener {
            finish()


        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

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
                                ConstantClass.checkActiveStatusAndLogout(this@MobileSelectionActivity, response.status, preference)
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
                                val intent = Intent(this@MobileSelectionActivity, ChooseYourRolePage::class.java)
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