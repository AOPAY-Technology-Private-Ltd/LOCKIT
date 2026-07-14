package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.Patterns
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.view.travel.airfragment.PersonalInfo.Companion.DOB
import com.bos.payment.appName.ui.view.travel.airfragment.PersonalInfo.Companion.EMAILID
import com.bos.payment.appName.ui.view.travel.airfragment.PersonalInfo.Companion.firstname
import com.bos.payment.appName.ui.view.travel.airfragment.PersonalInfo.Companion.mobNumber
import com.bos.payment.appName.ui.view.travel.airfragment.PersonalInfo.Companion.panNumber
import com.bosandroidapp.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore.CibilScoreReq
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.BureauScore.Companion.userScore
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CAIS.Companion.ActiveAccount
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CAIS.Companion.ClosedAccount
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CAIS.Companion.DefaultAccount
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CAIS.Companion.outstandingBalance
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CAIS.Companion.totalCreditAccount
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CAPSSummery.Companion.Last180Days
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CAPSSummery.Companion.Last30Days
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CAPSSummery.Companion.Last7Days
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CAPSSummery.Companion.Last90Days
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CreditAccounts.Companion.AccountList
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.isCibilAggrementVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.SendOtpReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.repository.CibilRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CibilViewModelFactory
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityCivilReportFormBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.ui.viewmodel.CibilViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CivilReportForm : AppCompatActivity() {

    lateinit var binding : ActivityCivilReportFormBinding
    lateinit var dialog: Dialog
    lateinit var preference : SharedPreference
    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var viewCibilModel: CibilViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCivilReportFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        preference = SharedPreference(this)
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        viewCibilModel = ViewModelProvider(this, CibilViewModelFactory(CibilRepository(RetrofitClient.apiInterfacePAN)))[CibilViewModel::class.java]

        setclickListner()

    }

    override fun onResume() {
        super.onResume()
        hitApiForLogin()
    }

    fun setclickListner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.acceptTermConditionCheck.setOnClickListener{
            OpenPopUpForTermCondition()
        }

        binding.calendarlayout.setOnClickListener{
            showDatePicker()
        }

        binding.createaccount.setOnClickListener {
            var firstName = binding.firstName.text.toString().trim()
            var lastName = binding.lastName.text.toString().trim()
            var mobilenumber = binding.mobileNumber.text.toString().trim()
            var emailId = binding.emailId.text.toString().trim()
            var pannumber = binding.panEditText.text.toString().trim()
            var dob = binding.dob.text.toString().trim()
            var isAccepted = binding.acceptTermConditionCheck.isChecked


            if(validateForm(firstName,lastName,pannumber,mobilenumber,emailId,dob,isAccepted,this@CivilReportForm)){
                if(isInternetAvailable(this@CivilReportForm))  {
                    hitApiForSendOTP(mobilenumber,"Mobile")
                }
                else{

                }
            }

        }

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Month is 0-indexed, so we add 1
                calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = sdf.format(calendar.time)

                binding.dob.text = formattedDate

            }, year, month, day
        )

        datePickerDialog.show()
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

                                if (response.statuss.equals("True")) {
                                    var firstName = "Customer"
                                    var customerName = firstName
                                    hitApiForCibilScore(otp)
                                }
                                else{
                                    Toast.makeText(this@CivilReportForm,response.message,Toast.LENGTH_SHORT).show()
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

    fun hitApiForCibilScore(otp:String){
        var firstName = binding.firstName.text.toString().trim()
        var lastName = binding.lastName.text.toString().trim()
        var mobilenumber = binding.mobileNumber.text.toString().trim()
        var emailId = binding.emailId.text.toString().trim()
        var pannumber = binding.panEditText.text.toString().trim()
        var dob = binding.dob.text.toString().trim()

        var cibilReq = CibilScoreReq(
            firstName= firstName,
            lastName = lastName,
            mobilenumber = mobilenumber,
            dob= dob,
            mailid = emailId,
            pannumber = pannumber,
            otp = otp,
            consentmessage = "I agree to share my data for verification purposes",
            consentacceptence = "yes",
            registrationID = "AOP-5039"
        )

        Log.d("CibilReq",Gson().toJson(cibilReq))
        viewCibilModel.getCibilReq(cibilReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                ConstantClass.dialog.dismiss()
                                var otp = response.value
                                Log.d("cibilresp", response.message)
                                if (!response.httpResponseCode.isNullOrBlank() && response.httpResponseCode.equals("200")) {
                                    var data = response.result.resultJson.inProfileResponse
                                    // for personal details
                                    var applicantDetails = data.currentApplication.currentApplicationDetails.currentApplicantDetails
                                    firstname = applicantDetails.firstName
                                    panNumber = applicantDetails.incomeTaxPan
                                    mobNumber = applicantDetails.mobilePhoneNumber

                                    //for dob and mailid
                                    val emailList = mutableListOf<String>()
                                    var caisHolderDetails = data.caisAccount.caisAccountDETAILS

                                    var creditAccount =  data.caisAccount.caisSummary.creditAccount
                                    var totaloutstandingbalance =  data.caisAccount.caisSummary.totalOutstandingBalance


                                    caisHolderDetails.forEach { account ->
                                        account.caisHolderPhoneDetails.forEach { phoneDetails ->
                                            val email = phoneDetails.eMailId
                                            if (!email.isNullOrBlank()) {
                                                emailList.add(email)
                                            }
                                        }
                                    }

                                    val dobList = mutableListOf<String>()

                                    caisHolderDetails.forEach { account ->
                                        account.caisHolderDetails.forEach { holderdetails ->
                                            holderdetails.dateOfBirth.let { dob ->
                                                if (dob.isNotBlank()) {
                                                    dobList.add(dob)
                                                }
                                            }
                                        }
                                    }

                                    val emailsInSingleLine = emailList
                                    val dobInSingleLine = dobList

                                    // Example: show in Log or TextView
                                    Log.d("Emails", emailsInSingleLine[0])
                                    Log.d("dob",dobInSingleLine[0])

                                    DOB = dobInSingleLine[0]
                                    EMAILID = emailsInSingleLine[0]

                                    Last7Days = data.caps.capsSummary.capsLast7Days
                                    Last30Days = data.caps.capsSummary.capsLast30Days
                                    Last90Days = data.caps.capsSummary.capsLast90Days
                                    Last180Days = data.caps.capsSummary.capsLast180Days

                                    totalCreditAccount =creditAccount.creditAccountTotal
                                    ActiveAccount = creditAccount.creditAccountActive
                                    DefaultAccount = creditAccount.creditAccountDefault
                                    ClosedAccount = creditAccount.creditAccountClosed
                                    outstandingBalance = totaloutstandingbalance.outstandingBalanceAll

                                    userScore = data.score.bureauScore.toFloat()

                                    AccountList = caisHolderDetails

                                   startActivity(Intent(this@CivilReportForm,CibilReportsDetailsPage::class.java))

                                }
                                else{
                                    Toast.makeText(this@CivilReportForm,response.message,Toast.LENGTH_LONG).show()
                                }

                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                       // ConstantClass.OpenPopUpForVeryfyOTP(this)
                    }


                }
            }
        }

    }


    fun validateForm(
        firstname: String,
        lastname: String,
        pannumber: String,
        mobile: String,
        email: String,
        dob: String,
        isAccepted: Boolean,
        context: Context
    ): Boolean {

        if (mobile.isBlank() || mobile.length != 10 || !mobile.all { it.isDigit() }) {
            Toast.makeText(context, "Enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Enter a valid email address", Toast.LENGTH_SHORT).show()
            return false
        }


        if (firstname.isBlank() ) {
            Toast.makeText(context, "Enter first name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (lastname.isBlank() ) {
            Toast.makeText(context, "Enter last name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (dob.isBlank() ) {
            Toast.makeText(context, "Enter dob name", Toast.LENGTH_SHORT).show()
            return false
        }

        // PAN validation (Regex: 5 letters, 4 digits, 1 letter)
        val panRegex = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        if (pannumber.isBlank() || !panRegex.matches(pannumber.uppercase())) {
            Toast.makeText(context, "Enter a valid PAN number (e.g., ABCDE1234F)", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!isAccepted) {
            Toast.makeText(context, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
            return  false
        }

        return true // All fields are valid
    }


    fun OpenPopUpForTermCondition() {

        dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.term_condition_layout)

        dialog.setCanceledOnTouchOutside(false)

        val verifyButton = dialog.findViewById<LinearLayout>(R.id.btnAccept)
        val termConditiontxt = dialog.findViewById<TextView>(R.id.tvTermsContent)

        termConditiontxt.text = Html.fromHtml(getString(R.string.cibiltermcondition), Html.FROM_HTML_MODE_LEGACY)

        verifyButton.setOnClickListener {
            isCibilAggrementVerified = true
            binding.acceptTermConditionCheck.isChecked = true
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            // Called when dialog is dismissed by back press or programmatically
            if (isCibilAggrementVerified) {

            } else {
                binding.acceptTermConditionCheck.isChecked = false
                isCibilAggrementVerified = false
            }
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
                                ConstantClass.checkActiveStatusAndLogout(this@CivilReportForm, response.status, preference)
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
                                val intent = Intent(this@CivilReportForm, ChooseYourRolePage::class.java)
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