package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bos.payment.appName.network.ApiInterface
import com.bos.payment.appName.network.RetrofitClient
import com.chaos.view.PinView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityRetailerProfilePageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.Customer
import com.bosandroidapp.aopaykit.constant.ConstantClass.Retailer
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.constant.ConstantClass.validateLoginInput
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.RetailerProfileReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.VerifyOTPReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.SendOtpReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.launch

class RetailerProfilePage : AppCompatActivity() {
    lateinit var binding: ActivityRetailerProfilePageBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var api: ApiInterface
    lateinit var preference: SharedPreference
    var retailerCode: String = ""
    var Address: String = ""
    var MobileNumber: String = ""
    var EmailId: String = ""
    var FName: String = ""
    var LName: String = ""
    var customerType: String = ""
    var customerCode: String = ""
    var clicMobile: Boolean = false
    var clickEmailId: Boolean = false
    var isMobileVerify: Boolean = false
    var isEmailVerify: Boolean = false
    lateinit var dialog: Dialog
    lateinit var countDownTimer: CountDownTimer

    var checkFirstname: Boolean = false
    var checkLastname: Boolean = false
    var checkaddressname: Boolean = false
    var checkEmailId: Boolean = false
    lateinit var logintype: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRetailerProfilePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        preference = SharedPreference(this)
        retailerCode = preference.getStringValue(ConstantClass.RetailerCode, "")
        api = RetrofitClient.apiInterfaceSMS
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface))
        )[AuthenticationViewModel::class.java]


        logintype = preference.getStringValue(ConstantClass.LoginType, "").orEmpty()


        if (logintype.equals(Retailer)) {
            binding.edittext.visibility = View.VISIBLE
            binding.codetype.text = "Retailer Code"
            customerType = Retailer
            binding.customerCode.setTextColor(resources.getColor(R.color.grey))
            binding.aadharNumber.setTextColor(resources.getColor(R.color.grey))
            binding.pannumber.setTextColor(resources.getColor(R.color.grey))
            customerCode = preference.getStringValue(ConstantClass.RetailerCode, "")
            hitapiforGetUpdateProfile()
        } else {
            binding.codetype.text = "Customer Code"
            binding.edittext.visibility = View.INVISIBLE
            binding.verifybuttonlayout.visibility = View.GONE
            binding.customerCode.setTextColor(resources.getColor(R.color.black))
            binding.aadharNumber.setTextColor(resources.getColor(R.color.black))
            binding.pannumber.setTextColor(resources.getColor(R.color.black))
            customerType = Customer
            customerCode = preference.getStringValue(ConstantClass.CustomerCode, "")
            hitapiforGetUpdateProfile()
        }
        setViewValidation()
        setClickListner()

        binding.back.setOnClickListener {
            finish()
        }


    }


    override fun onResume() {
        super.onResume()
        if (logintype.equals(Retailer)) {
            hitApiForLogin()
        }
    }


    fun setViewValidation() {
        binding.retailerFName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.retailerFName.error = "Emoji not allowed"
                        checkFirstname = false
                    } else {
                        checkFirstname = true
                    }
                }
            }


        })

        binding.retailerLName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.retailerFName.error = "Emoji not allowed"
                        checkLastname = false
                    } else {
                        checkLastname = true
                    }
                }
            }


        })

        binding.address.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.address.error = "Emoji not allowed"
                        checkaddressname = false
                    } else {
                        checkaddressname = true
                    }
                }
            }


        })

        binding.emailId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.emailId.error = "Emoji not allowed"
                        checkEmailId = false
                    } else {
                        checkEmailId = true
                    }
                }
            }


        })

    }

    fun setClickListner() {

        binding.back.setOnClickListener {
            finish()
        }

        binding.mobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length == 10 && binding.edittext.text.equals(resources.getString(R.string.cancel))) {
                    binding.verifymobilenumber.visibility = View.VISIBLE
                } else {
                    binding.verifymobilenumber.visibility = View.GONE
                }
            }

        })

        binding.emailId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length > 0) {
                    if (clicMobile) {
                        binding.verifymobilenumber.visibility = View.GONE
                    } else {
                        if (binding.edittext.text.equals(resources.getString(R.string.cancel))) {
                            binding.verifymobilenumber.visibility = View.VISIBLE
                        } else {
                            binding.verifymobilenumber.visibility = View.GONE
                        }

                    }
                    if (binding.edittext.text.equals(resources.getString(R.string.cancel))) {
                        binding.verifyEmailId.visibility = View.VISIBLE
                    } else {
                        binding.verifyEmailId.visibility = View.GONE
                    }

                } else {
                    binding.verifyEmailId.visibility = View.GONE
                }
            }

        })

        binding.verifyEmailId.setOnClickListener {
            clickEmailId = true
            clicMobile = false
            var mailId = binding.emailId.text.toString()
            EmailId = mailId
            if (validateLoginInput(mailId, this)) {
                if (isInternetAvailable(this@RetailerProfilePage)) {
                    hitApiForSendOTP(mailId, "Email")
                } else {
                    Toast.makeText(
                        this,
                        "Please check your internet connection!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.verifymobilenumber.setOnClickListener {
            clickEmailId = false
            clicMobile = true
            var mobnumber = binding.mobileNumber.text.toString()

            if (validateLoginInput(mobnumber, this) && !binding.retailerFName.text.toString()
                    .isNullOrBlank() && !binding.retailerLName.text.toString().isNullOrBlank()
            ) {
                if (isInternetAvailable(this@RetailerProfilePage)) {
                    hitApiForSendOTP(mobnumber, "Mobile")
                } else {
                    Toast.makeText(
                        this,
                        "Please check your internet connection!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                if (binding.retailerFName.text.toString()
                        .isNullOrBlank() || binding.retailerLName.text.toString().isNullOrBlank()
                ) {
                    Toast.makeText(this, "Please enter  first or last name", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }

        binding.edittext.setOnClickListener {
            if (binding.edittext.text.equals(resources.getString(R.string.edit))) {
                binding.edittext.text = resources.getString(R.string.cancel)
                binding.verifybuttonlayout.visibility = View.VISIBLE
                setEnableField()
            } else {
                binding.edittext.text = resources.getString(R.string.edit)
                binding.verifybuttonlayout.visibility = View.GONE
                setDisableField()
            }
        }


        binding.verifybuttonlayout.setOnClickListener {
            var mob = binding.mobileNumber.text.toString().trim()
            var emailId = binding.emailId.text.toString().trim()

            val (isValid, errorMessage) = isValidForm(
                checkFirstname = checkFirstname,
                checkLastname = checkLastname,
                checkaddress = checkaddressname,
                checkMailId = checkEmailId
            )


            if (isInternetAvailable(this)){
                if (!isValid) {
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
                else{
                    if (!mob.equals(MobileNumber) || !emailId.equals(EmailId)) {
                        if (isEmailVerify || isMobileVerify) {
                            OpenPopUpForVAlert()
                        }
                        else {
                            Toast.makeText(this, "Please verify mobile or email id first!!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else {

                        hitapiForUpdateProfile()
                    }
                }

            }

        }
    }


    fun hitapiforGetUpdateProfile() {

        var req = RetailerProfileReq(
            mode = "GET",
            customerType = customerType,
            customerCode = customerCode,
            firstName = "",
            lastName = "",
            mobileNo = "",
            emailid = "",
            address = "",
            aadharNumber = "",
            panNumber = "",
            activeStatus = ""
        )

        Log.d("retailergetprofileReq", Gson().toJson(req))

        viewModel.getRetailerProfileReq(req).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->

                                if(response!=null){
                                if (response!!.statuss.equals("True")) {
                                    Log.d("RetailerDetailsResponse", Gson().toJson(response))
                                    ConstantClass.dialog.dismiss()
                                    Address = response.address.toString()
                                    EmailId = response.emailid.toString()
                                    MobileNumber = response.mobileNo.toString()
                                    FName = response.firstName.toString()
                                    LName = response.lastName.toString()
                                    binding.retailerFName.setText(response.firstName)
                                    binding.retailerLName.setText(response.lastName)
                                    binding.customerCode.setText(response.customerCode)
                                    binding.aadharNumber.setText(response.aadharNumber)
                                    binding.address.setText(response.address)
                                    binding.mobileNumber.setText(response.mobileNo)
                                    binding.emailId.setText(response.emailid)
                                    binding.pannumber.setText(response.panNumber)
                                    binding.verifybuttonlayout.visibility = View.GONE
                                    binding.edittext.text = ConstantClass.editprofile
                                    preference.setStringValue(ConstantClass.FirstName, response.firstName.toString())
                                    preference.setStringValue(ConstantClass.LastName, response.lastName.toString())
                                    preference.setStringValue(ConstantClass.CustomerMobileNumber, response.mobileNo.toString())
                                    preference.setStringValue(ConstantClass.CustomerEmailID, response.emailid.toString())
                                    preference.setStringValue(ConstantClass.CustomerCode, response.customerCode.toString())
                                    setDisableField()
                                }
                                else {
                                    ConstantClass.dialog.dismiss()
                                    finish()
                                }
                                 }

                                else{
                                    ConstantClass.dialog.dismiss()
                                    hitapiforGetUpdateProfile()
                                }
                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                        hitapiforGetUpdateProfile()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(this)
                    }
                }
            }
        }
    }


    fun hitapiForUpdateProfile() {

        var req = RetailerProfileReq(
            mode = "UPDATE",
            customerType = "Retailer",
            customerCode = retailerCode,
            firstName = binding.retailerFName.text.toString(),
            lastName = binding.retailerLName.text.toString(),
            mobileNo = binding.mobileNumber.text.toString(),
            emailid = binding.emailId.text.toString(),
            address = binding.address.text.toString(),
            aadharNumber = binding.aadharNumber.text.toString(),
            panNumber = binding.pannumber.text.toString(),
            activeStatus = "True"
        )

        Log.d("retailerUpdateprofileReq", Gson().toJson(req))

        viewModel.getRetailerProfileReq(req).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                if (response!!.statuss.equals("True")) {
                                    Log.d("ProfileUpdateResp", Gson().toJson(response))
                                    ConstantClass.dialog.dismiss()

                                    var mob = binding.mobileNumber.text.toString().trim()
                                    var emailId = binding.emailId.text.toString().trim()

                                    if (!mob.equals(MobileNumber) || !emailId.equals(EmailId)) {
                                        preference.setBooleanValue(ConstantClass.LoggedIn, false)
                                        preference.setStringValue(ConstantClass.LoginType, "")
                                        ConstantClass.ClickOnCardDashboard = ""
                                        val intent = Intent(this@RetailerProfilePage, ChooseYourRolePage::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        binding.edittext.text = this.resources.getString(R.string.edit)
                                        binding.verifybuttonlayout.visibility = View.GONE
                                        setDisableField()
                                        hitapiforGetUpdateProfile()
                                        Toast.makeText(this, "Profile update successfully !!", Toast.LENGTH_SHORT).show()
                                    }

                                } else {
                                    ConstantClass.dialog.dismiss()
                                    finish()
                                }

                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(this)
                    }
                }
            }
        }

    }


    fun setDisableField() {
        binding.retailerFName.isEnabled = false
        binding.retailerLName.isEnabled = false
        binding.customerCode.isEnabled = false
        binding.aadharNumber.isEnabled = false
        binding.pannumber.isEnabled = false
        binding.address.isEnabled = false
        binding.mobileNumber.isEnabled = false
        binding.emailId.isEnabled = false
    }


    fun setEnableField() {
        binding.retailerFName.isEnabled = true
        binding.retailerLName.isEnabled = true
        binding.customerCode.isEnabled = false
        binding.aadharNumber.isEnabled = false
        binding.pannumber.isEnabled = false
        binding.address.isEnabled = true
        binding.mobileNumber.isEnabled = true
        binding.emailId.isEnabled = true
        binding.firstName.requestFocus()
    }


    fun hitApiForSendOTP(mailidormobile: String, type: String) {
        var sendOtpReq = SendOtpReq(
            mobileoremailId = mailidormobile,
            otpType = type
        )
        Log.d("SendOTPREQ", Gson().toJson(sendOtpReq))

        viewModel.sendOTPReq(sendOtpReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("SendRes", response.message)
                                var otp = response.value
                                Log.d("OTP", otp)

                                if (response.statuss.equals("True")) {
                                    if (clicMobile) {
                                        var firstName = binding.retailerFName.text.toString()
                                        var lastName = binding.retailerLName.text.toString()
                                        var customerName = firstName.plus(" ").plus(lastName)
                                        hitApiForMobVerify(mailidormobile, customerName, otp)
                                    }

                                    if (clickEmailId) {
                                        ConstantClass.dialog.dismiss()
                                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT)
                                            .show()
                                        OpenPopUpForVeryfyOTP(mailidormobile, "")
                                    }
                                }
                                else{
                                    Toast.makeText(this@RetailerProfilePage,response.message,Toast.LENGTH_SHORT).show()
                                    ConstantClass.dialog.dismiss()
                                }
                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(this)
                    }


                }
            }
        }
    }


    fun hitApiForMobVerify(mobnumber: String, customerName: String, OTP: String) {
        // hint: Dear  Annu Satija , Your OTP for Verification is 1234. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER
        var message =
            "Dear $customerName, Your OTP for Verification is $OTP. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER"

        lifecycleScope.launch {
            try {
                val response = api.sendSMSForVerifyMob(
                    apikey = ConstantClass.SMS_API_KEY,
                    senderid = ConstantClass.SMS_SENDER_ID,
                    templateid = ConstantClass.SMS_TEMPLATE_ID,
                    mobnumber = mobnumber,
                    message = message,
                )

                if (response!!.isSuccessful) {
                    Toast.makeText(
                        this@RetailerProfilePage,
                        "Otp sent on your mobile number!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val loanData = response.body()

                    if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                        ConstantClass.dialog.dismiss()
                        OpenPopUpForVeryfyOTP(mobnumber, OTP)
                    }

                    Log.d("API_SUCCESS", loanData.toString())
                } else {
                    Log.e("API_ERROR", response.errorBody()?.string() ?: "Unknown error")
                }

            } catch (e: Exception) {
                Log.e("API_EXCEPTION", e.toString())
            }
        }
    }


    fun OpenPopUpForVeryfyOTP(EmailID: String, otp: String) {
        dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.verifyforgetpasswordotplayour)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }


        dialog.setCanceledOnTouchOutside(false)

        val pinView = dialog.findViewById<PinView>(R.id.pinview)

        val verifyButton = dialog.findViewById<LinearLayout>(R.id.verifylayout)
        val cancel = dialog.findViewById<ImageView>(R.id.cancel)
        val resendlayout = dialog.findViewById<RelativeLayout>(R.id.resendlayout)
        val resendtxt = dialog.findViewById<TextView>(R.id.resendtxt)
        val timer = dialog.findViewById<TextView>(R.id.timer)
        val title = dialog.findViewById<TextView>(R.id.text_subtitle)

        startOtpTimer(resendtxt, timer)

        if (clickEmailId) {
            title.text = "Enter four digit OTP send on your registered email id"
        } else {
            title.text = "Enter four digit OTP send on your registered mobile number"
        }



        cancel.setOnClickListener {
            dialog.dismiss()
        }

        resendlayout.setOnClickListener {

            if (validateLoginInput(EmailID, this)) {
                if (isInternetAvailable(this@RetailerProfilePage)) {
                    if (clickEmailId) {
                        hitApiForReSendOTP(EmailID, "Mail")
                    }
                    if (clicMobile) {
                        hitApiForReSendOTP(EmailID, "Mobile")
                    }
                    startOtpTimer(resendtxt, timer)
                } else {
                    Toast.makeText(
                        this,
                        "Please check your internet connection!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }

        verifyButton.setOnClickListener {
            val enteredOTP = pinView.getText().toString()

            if (enteredOTP.length == 4) {
                if (clickEmailId) {
                    hitApiForOTPVerify(EmailID, enteredOTP, "Mail id verify")
                }
                if (clicMobile) {
                    hitApiForOTPVerify(EmailID, enteredOTP, "Mobile verify")
                }


            } else {
                Toast.makeText(this, "Please enter complete OTP", Toast.LENGTH_SHORT).show()
            }

        }

        dialog.show()

    }


    fun startOtpTimer(resendtxt: TextView, timer: TextView) {
        resendtxt.visibility = View.INVISIBLE
        timer.visibility = View.VISIBLE

        countDownTimer = object : CountDownTimer(5 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timer.text = "00:00"
                timer.visibility = View.INVISIBLE
                resendtxt.visibility = View.VISIBLE
            }
        }
        countDownTimer.start()
    }


    private fun setupOTPFocus(current: EditText, next: EditText?, previous: EditText? = null) {
        current.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) {
                    next?.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        current.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && current.text.isEmpty()) {
                previous?.requestFocus()
                previous?.setSelection(previous.text.length) // move cursor to end
                return@setOnKeyListener true
            }
            false
        }


    }


    fun hitApiForOTPVerify(mobileOrEmailID: String, otp: String, message: String) {
        var verifyotpreq = VerifyOTPReq(
            mobileormailid = mobileOrEmailID,
            otp = otp,
            logintype = message
        )
        Log.d("VerifyOTPReq", Gson().toJson(verifyotpreq))
        viewModel.verifyOTPReq(verifyotpreq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                ConstantClass.dialog.dismiss()
                                Log.d("VerifyOTPRes", response.message)
                                if (response.statuss.equals("True")) {
                                    if (clickEmailId) {
                                        EmailId = mobileOrEmailID
                                        isEmailVerify = true
                                        binding.emailId.isEnabled = false
                                        binding.verifyiconemailId.visibility = View.VISIBLE
                                        binding.verifyEmailId.visibility = View.GONE
                                    }

                                    if (clicMobile) {
                                        isMobileVerify = true
                                        binding.mobileNumber.isEnabled = false
                                        binding.verifyiconphonenumber.visibility = View.VISIBLE
                                        binding.verifymobilenumber.visibility = View.GONE
                                    }

                                    if (dialog != null && dialog.isShowing) {
                                        dialog.dismiss()
                                    }
                                } else {
                                    binding.emailId.isEnabled = true
                                    binding.verifyiconemailId.visibility = View.GONE
                                }

                                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                            }
                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(this)
                    }

                }
            }

        }

    }


    fun hitApiForReSendOTP(mailidormobile: String, type: String) {
        var sendOtpReq = SendOtpReq(
            mobileoremailId = mailidormobile,
            otpType = type
        )
        Log.d("SendOTPREQ", Gson().toJson(sendOtpReq))
        viewModel.sendOTPReq(sendOtpReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                ConstantClass.dialog.dismiss()
                                Log.d("SendRes", response.message)
                                if(response.statuss.equals("True")){
                                    if (clickEmailId) {
                                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        if (clicMobile) {
                                            var otp = response.value
                                            var firstName = binding.retailerFName.text.toString()
                                            var lastName = binding.retailerLName.text.toString()
                                            var customerName = firstName.plus(" ").plus(lastName)
                                            hitApiForResendMobVerify(mailidormobile, customerName, otp)
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(this@RetailerProfilePage,response.message,Toast.LENGTH_SHORT).show()
                                    ConstantClass.dialog.dismiss()
                                }
                                
                            }
                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(this)
                    }


                }
            }
        }
    }


    fun hitApiForResendMobVerify(mobnumber: String, customerName: String, OTP: String) {
        // hint: Dear  Naim Khan, Your OTP for Verification is 1234. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER
        var message = "Dear $customerName, Your OTP for Verification is $OTP. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER "

        lifecycleScope.launch {
            try {
                val response = api.sendSMSForVerifyMob(
                    apikey = ConstantClass.SMS_API_KEY,
                    senderid = ConstantClass.SMS_SENDER_ID,
                    templateid = ConstantClass.SMS_TEMPLATE_ID,
                    mobnumber = mobnumber,
                    message = message
                )

                if (response!!.isSuccessful) {
                    Toast.makeText(
                        this@RetailerProfilePage,
                        "Otp sent on your mobile number!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val loanData = response.body()

                    if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                        ConstantClass.dialog.dismiss()
                    }
                    Log.d("API_SUCCESS", loanData.toString())
                } else {
                    Log.e("API_ERROR", response.errorBody()?.string() ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("API_EXCEPTION", e.toString())
            }
        }

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

        txt.text = "If you update your email ID or mobile number, you will be logged out of the app"

        done.setOnClickListener {
            hitapiForUpdateProfile()

        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


    fun containsEmoji(text: String): Boolean {
        for (char in text) {
            val type = Character.getType(char)
            if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                return true
            }
        }
        return false
    }



    fun isValidForm(
        checkFirstname: Boolean,
        checkLastname:Boolean,
        checkaddress: Boolean,
        checkMailId: Boolean): Pair<Boolean, String?> {
        // Bank details
        if (!checkFirstname) return Pair(false, "Enter valid first name")

        if (!checkLastname) return Pair(false, "Enter valid last name")

        if (!checkaddress) return Pair(false, "Enter valid address")

        if (!checkMailId) return Pair(false, "Enter valid mail id")

        return Pair(true, null)
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
                                ConstantClass.checkActiveStatusAndLogout(this@RetailerProfilePage, response.status, preference)
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
                                val intent = Intent(this@RetailerProfilePage, ChooseYourRolePage::class.java)
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