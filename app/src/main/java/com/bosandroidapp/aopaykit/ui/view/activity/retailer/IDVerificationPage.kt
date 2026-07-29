package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.Window.FEATURE_NO_TITLE
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityIdverificationPageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadharTransactionIdNo
import com.bosandroidapp.aopaykit.constant.ConstantClass.CheckOnlineOrOffline
import com.bosandroidapp.aopaykit.constant.ConstantClass.ENTEREDCUSTOMERDOB
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanFrontImageUri
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefAadharTransactionIdNo
import com.bosandroidapp.aopaykit.constant.ConstantClass.ReferenceAadharNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.ReferenceAadharVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.kitoption.KitOptionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.AadharVerificationReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.repository.PanRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.data.viewModelFactory.PanViewModelFactory
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.AadharCardWebViewDIGILockerPage.Companion.digilockerLink
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.ui.viewmodel.PanViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class IDVerificationPage : BaseActivity() {
    lateinit var binding: ActivityIdverificationPageBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference: SharedPreference
    lateinit var dialog: Dialog
    lateinit var panViewModel: PanViewModel


    companion object{
        var isOnline : Boolean = false
        var isOffline : Boolean = false
        var isKit : Boolean = false

        var onlineMaxLoanLimit: Int = 0
        var availableOnlineBalance : Int =0
        var offlineMaxLoanLimit : Int =0

        var availableOfflineBalance : Int =0
        var kitMaxLoanLimit : Int =0
        var availableKitBalance : Int =0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIdverificationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        panViewModel = ViewModelProvider(this, PanViewModelFactory(PanRepository(RetrofitClient.apiInterfacePAN)))[PanViewModel::class.java]
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

        preference = SharedPreference(this)



        setOnClickListner()

    }


    fun setOnClickListner() {

        binding.swiperefresh.setOnRefreshListener {
            if (isInternetAvailable(this@IDVerificationPage)) {
                hitApiForKitOption()
                binding.swiperefresh.isRefreshing = true
            }
        }

        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.aadharcardlayout.setOnClickListener {
            if (CheckOnlineOrOffline.isBlank()) {
                Toast.makeText(this@IDVerificationPage, "Please select mode first!!", Toast.LENGTH_SHORT).show()
            }
            else{
                if (PanNumber.isBlank() && !CheckOnlineOrOffline.equals(ConstantClass.kit)) {
                    Toast.makeText(this@IDVerificationPage, "Please Verify Pan Card first!!", Toast.LENGTH_SHORT).show()
                }
                else {
                    if (ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.online)) {
                        hitApiForAadharVerification()
                    }
                    else {
                        // 3. Offline validation (if Offline is selected)
                        if(CheckOnlineOrOffline== ConstantClass.kit){
                            if (!isKit || availableKitBalance <= 0 || kitMaxLoanLimit <= 0) {
                                PopOpForKitPackageAlert("Kit limit is not available.Please contact your administrator.")
                                return@setOnClickListener
                            }
                        }
                        startActivity(Intent(this@IDVerificationPage, AadharCardVerificationPage::class.java))
                    }
                }
            }

        }

        binding.pancardlayout.setOnClickListener {

            if (CheckOnlineOrOffline.isBlank()) {
                Toast.makeText(this@IDVerificationPage, "Please select mode first!!", Toast.LENGTH_SHORT).show()
            }
            else {
                // 1. Kit validation (Mandatory)
                /*if (!isKit ) {
                    PopOpForKitPackageAlert("Kit is mandatory for loan creation. Please contact your administrator.")
                }
                else{*/
                    // 2. Online validation (if Online is selected)
                    if (CheckOnlineOrOffline == ConstantClass.online) {
                        if (!isOnline || availableOnlineBalance <= 0 || onlineMaxLoanLimit <= 0) {
                            PopOpForKitPackageAlert("Online loan limit is not available.")
                            return@setOnClickListener
                        }
                    }

                    // 3. Offline validation (if Offline is selected)
                    if (CheckOnlineOrOffline == ConstantClass.offline) {
                        if (!isOffline || availableOfflineBalance <= 0 || offlineMaxLoanLimit <= 0) {
                            PopOpForKitPackageAlert("Offline loan limit is not available.")
                            return@setOnClickListener
                        }
                    }

                    // 3. Offline validation (if Offline is selected)
                    if(CheckOnlineOrOffline== ConstantClass.kit){
                        if (!isKit || availableKitBalance <= 0 || kitMaxLoanLimit <= 0) {
                            PopOpForKitPackageAlert("Kit limit is not available.Please contact your administrator.")
                            return@setOnClickListener
                        }
                    }

                   OpenPopUpForValidateDate()

              /*  }*/

            }

        }


        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->

            val radioButton = group.findViewById<RadioButton>(checkedId)

            if (radioButton != null && radioButton.isPressed) {
                when (checkedId) {

                    R.id.radioButton1 -> {
                        CheckOnlineOrOffline = ConstantClass.online
                        PanNumber = ""
                        PanFrontImageUri = null
                        ConstantClass.AadharVerified = ""
                        ConstantClass.ClickOnCardLowCibilScore = ""
                        binding.pancardlayout.isEnabled = true
                        AadharTransactionIdNo = ""
                        RefAadharTransactionIdNo = ""
                        ReferenceAadharVerified=""
                        ReferenceAadharNumber=""
                        onResume()
                    }

                    R.id.radioButton2 -> {
                        CheckOnlineOrOffline = ConstantClass.offline
                        PanNumber = ""
                        PanFrontImageUri = null
                        ConstantClass.AadharVerified = ""
                        ConstantClass.ClickOnCardLowCibilScore = ""
                        binding.pancardlayout.isEnabled = true
                        AadharTransactionIdNo = ""
                        RefAadharTransactionIdNo = ""
                        ReferenceAadharVerified=""
                        ReferenceAadharNumber=""
                        onResume()
                    }

                    R.id.radioButton3 -> {
                        CheckOnlineOrOffline = ConstantClass.kit
                        PanNumber = ""
                        PanFrontImageUri = null
                        ConstantClass.AadharVerified = ""
                        ConstantClass.ClickOnCardLowCibilScore = ""
                        binding.pancardlayout.isEnabled = true
                        AadharTransactionIdNo = ""
                        RefAadharTransactionIdNo = ""
                        ReferenceAadharVerified=""
                        ReferenceAadharNumber=""
                        onResume()
                    }

                }
            }

        }


        binding.cibilcardlayout.setOnClickListener {
            startActivity(Intent(this@IDVerificationPage, CivilReportForm::class.java))
        }


    }


    override fun onResume() {
        super.onResume()

        if (PanNumber.isBlank()) {
            binding.donepancard.visibility = View.GONE
            binding.pancardlayout.isEnabled = true
        } else {
            binding.donepancard.visibility = View.VISIBLE
            binding.pancardlayout.isEnabled = false
        }

        if (ConstantClass.AadharVerified.equals("no")&& ConstantClass.AadharVerified.isNullOrBlank() && ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.online)) {
            binding.doneaadhaar.visibility = View.VISIBLE
        }
        else {
            binding.doneaadhaar.visibility = View.GONE
        }

        hitApiForLogin()
        hitApiForKitOption()

    }

    fun hitApiForAadharVerification() {
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val emailId = preference.getStringValue(ConstantClass.CustomerEmailID, "").orEmpty()
        val mob = preference.getStringValue(ConstantClass.CustomerMobileNumber, "").orEmpty()

        var aadharverificationreq = AadharVerificationReq(
            firstName = firstName,
            lastName = lastName,
            mobileNumber = mob,
            emailId = emailId,
            registrationId = ConstantClass.PAN_VERIFICATION_REGISTRATION_ID,
        )

        Log.d("AadharVerificationreq", Gson().toJson(aadharverificationreq))

        panViewModel.getAadharVerificationReq(aadharverificationreq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                ConstantClass.dialog.dismiss()
                                Log.d("AadharVerificationResp", Gson().toJson(response))

                                if (response!!.code == null) {
                                    Toast.makeText(this@IDVerificationPage, response.message, Toast.LENGTH_SHORT).show()
                                }
                                if (response!!.code.equals("200")) {
                                    digilockerLink = response!!.model.kycUrl
                                    AadharTransactionIdNo = response.model.transactionId
                                    Log.d("Customerdigilockeurl", digilockerLink)

                                   /* val url = digilockerLink

                                    val customTabsIntent = CustomTabsIntent.Builder()
                                        .setShowTitle(true)
                                        .build()

                                    customTabsIntent.launchUrl(this, Uri.parse(url))*/

                                    startActivity(Intent(this@IDVerificationPage, AadharCardWebViewDIGILockerPage::class.java))
                                    finish()
                                }
                                else {
                                    Toast.makeText(this@IDVerificationPage, response.message, Toast.LENGTH_SHORT).show()
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


    override fun onBackPressed() {
        PanNumber = ""
        PanFrontImageUri = null
        ConstantClass.AadharVerified = ""
        CheckOnlineOrOffline = ""
        AadharTransactionIdNo = ""
        super.onBackPressed()
    }

    @SuppressLint("SetTextI18n")
    fun OpenPopUpForValidateDate() {
        dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(FEATURE_NO_TITLE)
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
        val dobfield = dialog.findViewById<TextView>(R.id.dob)
        val calendarlayout = dialog.findViewById<LinearLayout>(R.id.calendarlayout)

        cancel.visibility = View.VISIBLE
        image.visibility = View.VISIBLE
        calendarlayout.visibility = View.VISIBLE
        done.text = "Verify"
        txt.text = "You are eligible if your age is between 18 and 65 years ."


        calendarlayout.setOnClickListener {
            showDatePicker(dobfield, done)
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    private fun showDatePicker(dob: TextView, done: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var exactAge: Int = 0

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Selected date
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth)

                // Format and set DOB
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val formattedDate = sdf.format(selectedCalendar.time)
                dob.text = formattedDate
                ENTEREDCUSTOMERDOB = formattedDate

                // ✅ Age validation
                val today = Calendar.getInstance()
                val age = today.get(Calendar.YEAR) - selectedYear

                // Adjust if birthday hasn't occurred yet this year
                val hasBirthdayPassed = (today.get(Calendar.DAY_OF_YEAR) >= selectedCalendar.get(Calendar.DAY_OF_YEAR))
                exactAge = if (hasBirthdayPassed) age else age - 1



            }, year, month, day
        )


        // Set min age 18
        calendar.add(Calendar.YEAR, -18)
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        // Set max age 65
        val minCalendar = Calendar.getInstance()
        minCalendar.add(Calendar.YEAR, -65)
        datePickerDialog.datePicker.minDate = minCalendar.timeInMillis


        datePickerDialog.show()

        done.setOnClickListener {

            if (exactAge in 18..65) {
                startActivity(Intent(this@IDVerificationPage, PanCardVerificationPage::class.java))
                if(dialog!=null && dialog.isShowing){
                    dialog.dismiss()
                }
            }
            else {
                Toast.makeText(this, "❌ Not Eligible (Age: $exactAge) Loan applicants must be between 18 and 65 years ", Toast.LENGTH_SHORT).show()
            }

        }

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
                                ConstantClass.checkActiveStatusAndLogout(this@IDVerificationPage, response.status, preference)
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
                                val intent = Intent(this@IDVerificationPage, ChooseYourRolePage::class.java)
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



    fun hitApiForKitOption(){

        var request = KitOptionRequest(
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode,"")
        )

        viewModel.getRequestKitOption(request).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("ktResponse", Gson().toJson(response))

                                 if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                                    ConstantClass.dialog.dismiss()
                                 }

                                 var getdata = response.data

                                 getdata.let {
                                     isOnline = it?.get(0)!!.isOnline!!
                                     isOffline = it?.get(0)!!.isOffline!!
                                     isKit = it?.get(0)!!.isKit!!

                                     onlineMaxLoanLimit = it?.get(0)!!.onlineMaxLoanLimit!!
                                     availableOnlineBalance = it?.get(0)!!.availableOnlineBalance!!

                                     offlineMaxLoanLimit = it?.get(0)!!.offlineMaxLoanLimit!!
                                     availableOfflineBalance = it?.get(0)!!.availableOfflineBalance!!

                                     kitMaxLoanLimit = it?.get(0)!!.kitMaxLoanLimit!!
                                     availableKitBalance = it?.get(0)!!.availableKitBalance!!

                                     // view of ui option
                                     binding.radioButton1.visibility = if (isOnline) View.VISIBLE else View.GONE
                                     binding.radioButton2.visibility = if (isOffline) View.VISIBLE else View.GONE
                                     binding.radioButton3.visibility = if (isKit) View.VISIBLE else View.GONE

                                     binding.swiperefresh.isRefreshing = false

                                 }
                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                            ConstantClass.dialog.dismiss()
                        }
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }

                }
            }
        }


    }


    fun PopOpForKitPackageAlert(message : String){
        dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.kitmessagealert)

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        dialog.setCanceledOnTouchOutside(false)


        val done = dialog.findViewById<Button>(R.id.Ok)
        val txt = dialog.findViewById<TextView>(R.id.dialog_message)


        txt.text = message


        done.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


}