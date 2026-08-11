package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.chaos.view.PinView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityForgetPasswordPageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.ForgotPasswordType
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.data.model.loginsignup.ForgotPasswordReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.SendOtpReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.VerifyOTPReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.activity.LoginPage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson

class ForgetPasswordPage : BaseActivity() {
    lateinit var binding : ActivityForgetPasswordPageBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference
    lateinit var dialog : Dialog
    var EmailId :String = ""
    lateinit var countDownTimer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgetPasswordPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)

        setOnClickListner()


    }


    fun setOnClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.sendlayout.setOnClickListener {
            var emailOfMobile = binding.mailId.text.toString().trim()

            if(validateLoginInput(emailOfMobile,this)){
                if(isInternetAvailable(this@ForgetPasswordPage)) {
                    hitApiForSendOTP(emailOfMobile)
                }
                else{
                    Toast.makeText(this,"Please check your internet connection!!",Toast.LENGTH_SHORT).show()
                }
            }


        }

        binding.changepasswordback.setOnClickListener {
            binding.forgetpagelayout.visibility= View.VISIBLE
            binding.changepasswordmainlayout.visibility= View.GONE
        }

        binding.submitLayout.setOnClickListener {
            var password = binding.newPassword.text.toString().trim()
            var confirmpass = binding.confirmPassword.text.toString().trim()

            if(validateForm(password,confirmpass,this)){
                if(isInternetAvailable(this@ForgetPasswordPage)) {
                    hitApiForChangePassword(EmailId, password)
                }
                else{
                    Toast.makeText(this,"Please check your internet connection!!",Toast.LENGTH_SHORT).show()
                }
            }

        }

    }


    fun hitApiForSendOTP(mailidormobile:String){
        var sendOtpReq = SendOtpReq(
            mobileoremailId = mailidormobile,
            otpType =  ForgotPasswordType
        )

        Log.d("SendOTPREQ", Gson().toJson(sendOtpReq))
        viewModel.sendOTPReq(sendOtpReq).observe(this){
                resources->resources.let {
            when(it.apiStatus){
                ApiStatus.SUCCESS -> {
                    it.data?.let { users ->
                        users.body()?.let { response ->
                            ConstantClass.dialog.dismiss()
                            Log.d("SendRes",Gson().toJson(response) )

                            if(response.statuss.equals("True")){
                                OpenLoader(mailidormobile)
                                Toast.makeText(this,response.message,Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Toast.makeText(this,response.message,Toast.LENGTH_SHORT).show()
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


    fun hitApiForReSendOTP(mailidormobile:String){
        var sendOtpReq = SendOtpReq(
            mobileoremailId = mailidormobile,
            otpType = "Retailer forgot password"
        )
        Log.d("SendOTPREQ", Gson().toJson(sendOtpReq))
        viewModel.sendOTPReq(sendOtpReq).observe(this){
                resources->resources.let {
            when(it.apiStatus){
                ApiStatus.SUCCESS -> {
                    it.data?.let { users ->
                        users.body()?.let { response ->
                            ConstantClass.dialog.dismiss()
                            Log.d("SendRes", response.message)
                            Toast.makeText(this,response.message,Toast.LENGTH_SHORT).show()

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


    fun OpenLoader(mobileOrEmailID: String){
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

        // Access views inside the custom layout
        val pinView=dialog.findViewById<PinView>(R.id.pinview)

        val verifyButton = dialog.findViewById<LinearLayout>(R.id.verifylayout)
        val cancel = dialog.findViewById<ImageView>(R.id.cancel)
        val resendlayout = dialog.findViewById<RelativeLayout>(R.id.resendlayout)
        val resendtxt = dialog.findViewById<TextView>(R.id.resendtxt)
        val timer = dialog.findViewById<TextView>(R.id.timer)

        startOtpTimer(resendtxt,timer)


        cancel.setOnClickListener {
            dialog.dismiss()
        }

        resendlayout.setOnClickListener {
            var emailOfMobile = binding.mailId.text.toString().trim()
            if(validateLoginInput(emailOfMobile,this)){
                if(isInternetAvailable(this@ForgetPasswordPage)) {
                    hitApiForReSendOTP(emailOfMobile)
                    startOtpTimer(resendtxt,timer)
                }
                else{
                    Toast.makeText(this,"Please check your internet connection!!",Toast.LENGTH_SHORT).show()
                }

            }
        }

        verifyButton.setOnClickListener {
            val enteredOTP = pinView.getText().toString()
            if (enteredOTP.length == 4) {
                hitApiForOTPVerify(mobileOrEmailID,enteredOTP)
            } else {
                Toast.makeText(this, "Please enter complete OTP", Toast.LENGTH_SHORT).show()
            }

        }

        dialog.show()

    }


    fun hitApiForOTPVerify(mobileOrEmailID: String,otp:String){
        var verifyotpreq = VerifyOTPReq(
            mobileormailid = mobileOrEmailID,
            otp = otp,
            logintype = "Retailer Forget password"
        )
        Log.d("VerifyOTPReq", Gson().toJson(verifyotpreq))
        viewModel.verifyOTPReq(verifyotpreq).observe(this){
                resources->resources.let {
            when(it.apiStatus){
                ApiStatus.SUCCESS -> {
                    it.data?.let { users ->
                        users.body()?.let { response ->
                            ConstantClass.dialog.dismiss()
                            Log.d("VerifyOTPRes", response.message)
                            if(response.statuss.equals("True")){
                                EmailId=mobileOrEmailID
                                binding.forgetpagelayout.visibility=View.GONE
                                binding.changepasswordmainlayout.visibility=View.VISIBLE
                                if(dialog!=null && dialog.isShowing){
                                    dialog.dismiss()
                                }
                            }
                            Toast.makeText(this,response.message,Toast.LENGTH_SHORT).show()

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


    fun hitApiForChangePassword(mailidormobile: String,password:String){
        var changePass = ForgotPasswordReq(
            mobileormailid =mailidormobile ,
            password = password
        )
        Log.d("ChangePassReq", Gson().toJson(changePass))
        viewModel.forgotPasswordReq(changePass).observe(this){
                resources->resources.let {
            when(it.apiStatus){

                ApiStatus.SUCCESS -> {
                    it.data?.let { users ->
                        users.body()?.let { response ->
                            ConstantClass.dialog.dismiss()
                            Log.d("ChangePassRes", response.message)
                            Toast.makeText(this,response.message,Toast.LENGTH_SHORT).show()

                            if(response.statuss.equals("True")){
                                val intent = Intent(this@ForgetPasswordPage, LoginPage::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
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


    fun validateLoginInput(mobileOrEmailID: String, context: Context): Boolean {
        val isMobile = mobileOrEmailID.all { it.isDigit() } && mobileOrEmailID.length == 10
        val isEmail = Patterns.EMAIL_ADDRESS.matcher(mobileOrEmailID).matches()

        if (!isMobile && !isEmail) {
            Toast.makeText(context, "Enter a valid 10-digit mobile number or valid email address", Toast.LENGTH_SHORT).show()
            return false
        }


        return true // Input is valid
    }


    fun validateForm(password: String, confirmPassword: String, context: Context): Boolean {

        if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        if (confirmPassword != password) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true // All fields are valid
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



}