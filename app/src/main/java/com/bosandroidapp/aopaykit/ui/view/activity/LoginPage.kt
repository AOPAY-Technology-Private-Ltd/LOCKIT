package com.bosandroidapp.aopaykit.ui.slideshow.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bos.payment.appName.network.ApiInterface
import com.bos.payment.appName.network.RetrofitClient
import com.chaos.view.PinView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.Retailer
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.constant.ConstantClass.loginType
import com.bosandroidapp.aopaykit.data.model.loginsignup.LoginReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.VerifyOTPReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.SendOtpReq
import com.bosandroidapp.aopaykit.data.notification.NotificationSendTokenRequest
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityLoginPageBinding
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.ForgetPasswordPage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.SignupPage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginPage : BaseActivity() {
    lateinit var binding: ActivityLoginPageBinding
    private lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference
    lateinit var dialog : Dialog
    lateinit var countDownTimer: CountDownTimer
    lateinit var api : ApiInterface
    var  FireBaseToken: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)
        api =  RetrofitClient.apiInterfaceSMS

        setonclicklistner()
        getFirebaseToken()
    }


    private fun isSimPresent(): Boolean {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.simState != TelephonyManager.SIM_STATE_ABSENT
    }


    override fun onResume() {
        super.onResume()
        setLayoutForLogin()
    }


    fun setLayoutForLogin(){
        if(loginType.equals(Retailer)){
            binding.retailerLogin.visibility= View.VISIBLE
            binding.customerLogin.visibility=View.GONE
            binding.emailormobilenumber.requestFocus()

            binding.emailormobilenumber.postDelayed({
                binding.emailormobilenumber.requestFocus()
                val imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.emailormobilenumber, InputMethodManager.SHOW_FORCED)

                binding.main.viewTreeObserver.addOnGlobalLayoutListener {
                    val rect = Rect()
                    binding.main.getWindowVisibleDisplayFrame(rect)

                    val screenHeight = binding.main.rootView.height
                    val keypadHeight = screenHeight - rect.bottom

                    Log.d("KeyboardCheck", "screenHeight=$screenHeight, rect.bottom=${rect.bottom}, keypadHeight=$keypadHeight")

                    if (keypadHeight > screenHeight * 0.20) {
                        // ✅ Keyboard is open

                        binding.scrollview.post {
                            binding.scrollview.smoothScrollTo(0, binding.scrollview.bottom)
                        }

                    }
                }

            }, 300)

        }

        else{
            binding.retailerLogin.visibility= View.GONE
            binding.customerLogin.visibility=View.VISIBLE
            binding.customerLoginLayout.visibility = View.VISIBLE
        }
    }



    fun setonclicklistner(){

       /* binding.customerGenerateKeyLayout.setOnClickListener{
            hitApiForGetAndCheckAccessToken()
        }


        binding.clicktologin.setOnClickListener {
            preference.setBooleanValue(ConstantClass.CustomerAccessKey,true)
            val intent = Intent(this@LoginPage, DashBoard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }*/


        binding.signupText.setOnClickListener{
            val mainIntent = Intent(this@LoginPage, SignupPage::class.java)
            startActivity(mainIntent)
        }

        binding.emailormobilenumber.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.emailormobilenumber.error = "Emoji not allowed"
                        binding.loginlayout.isEnabled = false
                    }else{
                        binding.loginlayout.isEnabled = true
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.loginlayout.setOnClickListener {
            var emailOfMobile = binding.emailormobilenumber.text.toString().trim()
            var password = binding.password.text.toString().trim()

           if(validateLoginInput(emailOfMobile,password,this)){
               if(isInternetAvailable(this@LoginPage)) {
                   if (!isSimPresent()) {
                       Toast.makeText(this, "No SIM detected. Insert SIM to continue.", Toast.LENGTH_LONG).show()
                       return@setOnClickListener
                   }
                   hitApiForLogin(emailOfMobile,password)
               }
               else{
                   Toast.makeText(this,"Please check your internet connection!!",Toast.LENGTH_SHORT).show()
               }
           }

        }

        binding.forgetpageLayout.setOnClickListener {
            val mainIntent = Intent(this@LoginPage, ForgetPasswordPage::class.java)
            startActivity(mainIntent)
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.customerloginlayout.setOnClickListener {
            var mobnumber = binding.mobilenumber.text.toString()
            if (ConstantClass.validateLoginInput(mobnumber, this) ) {
                if (isInternetAvailable(this@LoginPage)) {
                    /*if (!isSimPresent()) {
                        Toast.makeText(this, "No SIM detected. Insert SIM to continue.", Toast.LENGTH_LONG).show()
                        return@setOnClickListener
                    }*/
                    hitApiForSendOTP(mobnumber,"Mobile")
                }
                else {
                    Toast.makeText(this, "Please check your internet connection!!", Toast.LENGTH_SHORT).show()
                }
            }
            else{

            }

        }


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



    fun hitApiForLogin(emailOfMobile:String,password:String){

        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        preference.setStringValue("deviceid",deviceId)

        var loginRequest = LoginReq(
            mobileormailid = emailOfMobile,
            password = password,
            logintype = loginType,
            deviceId = deviceId,
            token = FireBaseToken,
            imeiNumber = ConstantClass.IMEI
        )

        Log.d("LoginReq", Gson().toJson(loginRequest))

        viewModel.getLogin(loginRequest).observe(this) { it ->

            when (it.apiStatus) {

                ApiStatus.LOADING -> {
                    ConstantClass.OpenLoader(this)
                }

                ApiStatus.SUCCESS -> {
                    ConstantClass.dialog.dismiss()

                    val response = it.data?.body()
                    Log.d("LoginResponse", Gson().toJson(response))

                    if (response != null && response.statuss) {

                        if(loginType.equals(ConstantClass.Customer)){

                            val req = NotificationSendTokenRequest(
                                deviceType= ConstantClass.DeviceType,
                                clientCode = ConstantClass.ClientCode,
                                customerCode = response.customerCode.toString(),
                                retailerCode = response.retailerCode.toString(),
                                fcmToken = FireBaseToken
                            )
                            sendDataOnServerForUploadToken(req)
                        }

                        preference.setStringValue(ConstantClass.CustomerCode, response.customerCode.toString())
                        preference.setStringValue(ConstantClass.RetailerCode, response.retailerCode.toString())
                        preference.setStringValue(ConstantClass.FirstName, response.firstName.toString())
                        preference.setStringValue(ConstantClass.LastName, response.lastName.toString())
                        preference.setStringValue(ConstantClass.CustomerMobileNumber, response.mobileno.toString())
                        preference.setStringValue(ConstantClass.CustomerEmailID, response.emailID.toString())
                        preference.setBooleanValue(ConstantClass.LoggedIn, true)
                        preference.setStringValue(ConstantClass.LoginType, loginType)
                        preference.setStringValue(ConstantClass.LoginMobileorMailid, emailOfMobile)
                        preference.setStringValue(ConstantClass.Loginpassword, password)

                        val intent = Intent(this@LoginPage, DashBoard::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()

                    } else {
                        OpenPopUpForVAlert(response?.message ?: "Login failed. Please connect with your administrator.")
                    }
                }

                ApiStatus.ERROR -> {
                    ConstantClass.dialog.dismiss()
                    // 👇 Show proper error from ViewModel (404, 500 etc.)
                    val errorMessage = it.message ?: "Something went wrong"
                    OpenPopUpForVAlert(errorMessage)

                    Log.e("LoginError", errorMessage)
                }

            }

        }

    }


    fun validateLoginInput(mobileOrEmailID: String, password: String, context: Context): Boolean {
        val isMobile = mobileOrEmailID.all { it.isDigit() } && mobileOrEmailID.length == 10
        val isEmail = Patterns.EMAIL_ADDRESS.matcher(mobileOrEmailID).matches()

        if (!isMobile && !isEmail) {
            Toast.makeText(context, "Enter a valid 10-digit mobile number or valid email address", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isBlank()) {
            Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show()
            return false
        }

        return true // Input is valid
    }


    // for customer login flow
    fun OpenLoader(MobileNumber: String, otp:String){
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

        val verifyButton = dialog.findViewById<LinearLayout>(R.id.verifylayout)
        val cancel = dialog.findViewById<ImageView>(R.id.cancel)
        val resendlayout = dialog.findViewById<RelativeLayout>(R.id.resendlayout)
        val resendtxt = dialog.findViewById<TextView>(R.id.resendtxt)
        val timer = dialog.findViewById<TextView>(R.id.timer)
        val subtitle = dialog.findViewById<TextView>(R.id.text_subtitle)
        val pinView=dialog.findViewById<PinView>(R.id.pinview)

        subtitle.text = "Enter four digit OTP send on your registered mobile number"

        startOtpTimer(resendtxt,timer)

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        resendlayout.setOnClickListener {
                  if(isInternetAvailable(this@LoginPage)) {
                      hitApiForReSendOTP(MobileNumber,"Mobile")
                      startOtpTimer(resendtxt,timer)
                  }
                  else{
                      Toast.makeText(this,"Please check your internet connection!!", Toast.LENGTH_SHORT).show()
                  }


        }

        verifyButton.setOnClickListener {
            val enteredOTP = pinView.getText().toString()
            if (enteredOTP.length == 4) {
                 hitApiForOTPVerify(MobileNumber, enteredOTP,"Mobile verify")
                 var customerMobile = binding.mobilenumber.text.toString().trim()

                // Toast.makeText(this, "Thanks for your input! The next flow is under development and will be available soon.", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Please enter complete OTP", Toast.LENGTH_SHORT).show()
            }

        }

        dialog.show()

    }


    fun startOtpTimer( resendtxt:TextView, timer:TextView) {
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


    fun hitApiForSendOTP(mailidormobile: String,type : String) {
        var sendOtpReq = SendOtpReq(
            mobileoremailId = mailidormobile,
            otpType = type)
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
                                //Toast.makeText(this,otp, Toast.LENGTH_LONG).show() // for testing
                                if (response.statuss.equals("True")) {
                                    var firstName = "Customer"
                                    var customerName = firstName
                                    hitApiForMobVerify(mailidormobile,customerName,otp)
                                }
                                else{
                                    Toast.makeText(this@LoginPage,response.message,Toast.LENGTH_SHORT).show()
                                    if( ConstantClass.dialog.isShowing){
                                        ConstantClass.dialog.dismiss()
                                    }
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


    fun hitApiForMobVerify(mobnumber:String,customerName:String,OTP:String){
        // hint: Dear  Naim Khan, Your OTP for Verification is 1234. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER
        var message = "Dear $customerName, Your OTP for Verification is $OTP. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER"

        lifecycleScope.launch{
            try {
                val response =api.sendSMSForVerifyMob(
                    apikey = ConstantClass.SMS_API_KEY,
                    senderid = ConstantClass.SMS_SENDER_ID,
                    templateid = ConstantClass.SMS_TEMPLATE_ID,
                    mobnumber = mobnumber,
                    message = message,
                )

                if (response!!.isSuccessful) {
                    Toast.makeText(this@LoginPage,"Otp sent on your mobile number!!" , Toast.LENGTH_SHORT).show()
                    val loanData = response.body()

                    if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                        ConstantClass.dialog.dismiss()
                        OpenLoader(mobnumber,OTP)
                    }

                    Log.d("API_SUCCESS", loanData.toString())
                }
                else {
                    Log.e("API_ERROR", response.errorBody()?.string() ?: "Unknown error")
                }

            } catch (e: Exception) {
                Log.e("API_EXCEPTION", e.toString())
            }
        }
    }


    fun hitApiForOTPVerify(mobileOrEmailID: String, otp: String,message: String) {
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

                                    hitApiForLogin(mobileOrEmailID,"")

                                    if (dialog != null && dialog.isShowing) {
                                        dialog.dismiss()
                                    }
                                }

                                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
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


    fun hitApiForReSendOTP(mailidormobile: String,type : String) {
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
                                        var otp = response.value
                                        var customerName = "Customer"
                                        hitApiForResendMobVerify(mailidormobile,customerName,otp)
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


    fun hitApiForResendMobVerify(mobnumber:String,customerName:String,OTP:String){
    // hint: Dear  Naim Khan, Your OTP for Verification is 1234. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER
    var message = "Dear $customerName, Your OTP for Verification is $OTP. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER "

    lifecycleScope.launch{
        try {
            val response =api.sendSMSForVerifyMob(
                apikey = ConstantClass.SMS_API_KEY,
                senderid = ConstantClass.SMS_SENDER_ID,
                templateid = ConstantClass.SMS_TEMPLATE_ID,
                mobnumber = mobnumber,
                message = message)

            if (response!!.isSuccessful) {
                Toast.makeText(this@LoginPage,"Otp sent on your mobile number!!" , Toast.LENGTH_SHORT).show()
                val loanData = response.body()

                if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                    ConstantClass.dialog.dismiss()
                }
                Log.d("API_SUCCESS", loanData.toString())
            }
            else {
                Log.e("API_ERROR", response.errorBody()?.string() ?: "Unknown error")
            }
        }
        catch (e: Exception) {
            Log.e("API_EXCEPTION", e.toString())
        }
    }

}



    @SuppressLint("SetTextI18n")
    fun OpenPopUpForVAlert(message : String ?){
        dialog = Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
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

        image.visibility=View.VISIBLE
        done.visibility= View.GONE

        txt.text=/*"Your loan application was created, but you must validate your PAN  again since 90 days have passed"*/ message

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


    fun getFirebaseToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("FCM", "Fetching token failed", task.exception)
                    return@addOnCompleteListener
                }
                val fcmToken = task.result
                FireBaseToken= fcmToken
                preference.setStringValue(ConstantClass.FCMTOKEN,FireBaseToken)
               // sendDataToEmail("FCM Token: $fcmToken") // for testing
                Log.d("FCM_TOKEN", fcmToken)
            }
       }



    // for tsting ........................
    fun sendDataToEmail(data: String) {

        val intent = Intent(Intent.ACTION_SEND).apply {

            type = "message/rfc822"

            putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf("developerbos11@gmail.com")
            )

            putExtra(
                Intent.EXTRA_SUBJECT,
                "App Data"
            )

            putExtra(
                Intent.EXTRA_TEXT,
                data
            )
        }

        startActivity(Intent.createChooser(intent, "Send email"))
    }



    fun sendDataOnServerForUploadToken(request : NotificationSendTokenRequest){
        viewModel.NotificationSendTokenRequest(request).observe(this) { it ->

            when (it.apiStatus) {
                ApiStatus.LOADING -> {

                }

                ApiStatus.SUCCESS ->{
                    val response = it.data?.body()
                    Log.d("LoginResponse", Gson().toJson(response))

                }

                ApiStatus.ERROR -> {
                    ConstantClass.dialog.dismiss()
                    // 👇 Show proper error from ViewModel (404, 500 etc.)
                    val errorMessage = it.message ?: "Something went wrong"
                    OpenPopUpForVAlert(errorMessage)

                    Log.e("LoginError", errorMessage)
                }
            }

        }

    }

}