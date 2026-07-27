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
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bos.payment.appName.network.ApiInterface
import com.bos.payment.appName.network.RetrofitClient
import com.chaos.view.PinView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityPaymentInformationBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadharNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.AccountHolderName
import com.bosandroidapp.aopaykit.constant.ConstantClass.AccountNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.AccountType
import com.bosandroidapp.aopaykit.constant.ConstantClass.BankID
import com.bosandroidapp.aopaykit.constant.ConstantClass.BankIFSCCode
import com.bosandroidapp.aopaykit.constant.ConstantClass.BankName
import com.bosandroidapp.aopaykit.constant.ConstantClass.BranchAddress
import com.bosandroidapp.aopaykit.constant.ConstantClass.BranchName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustFirstName
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefAadharTransactionIdNo
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefAddress
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefName
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefRelationShip
import com.bosandroidapp.aopaykit.constant.ConstantClass.ReferenceAadharNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefmobileNo
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.constant.ConstantClass.validateLoginInput
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.VerifyOTPReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.AadharVerificationReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.SendOtpReq
import com.bosandroidapp.aopaykit.data.pennydrop.BankListReq
import com.bosandroidapp.aopaykit.data.pennydrop.PennyDropCheckStatusRequest
import com.bosandroidapp.aopaykit.data.pennydrop.PennyDropRequest
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.repository.PanRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.slideshow.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.AadharCardReferenceWebViewDIGILockerPage.Companion.checkAdharForRef
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.ui.viewmodel.PanViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlin.text.clear
import kotlin.text.equals

class PaymentInformation : AppCompatActivity() {
    lateinit var  binding : ActivityPaymentInformationBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var api: ApiInterface
    lateinit var preference: SharedPreference
    lateinit var dialog: Dialog
    lateinit var countDownTimer: CountDownTimer
    var isRefMobVerified : Boolean = false
    var checkIFSCCOde : Boolean = false

    var checkBranchName : Boolean = false
    var checkRefrenceName : Boolean = false
    var checkRefrenceRelation : Boolean = false
    var checkRefrenceaddress : Boolean = false

    lateinit var panViewModel: PanViewModel

    var bankList = mutableListOf<Pair<String, Int>>()
    var bankName = mutableListOf<String>()

    val agreementText = """
    <b>ECS Auto-Debit Agreement</b><br><br>

    By selecting ECS (Electronic Clearing Service), I hereby authorize <b> AO Pay </b> to debit my registered bank account for EMI payments.<br><br>

    <b>I understand and agree to the following:</b><br>
    • I have provided correct bank account details.<br>
    • I authorize debit on scheduled EMI dates.<br>
    • I will maintain sufficient balance.<br>
    • I am responsible for penalties in case of failure.<br>
    • This mandate remains valid until dues are cleared.<br>
    • Cancellation may take a few working days.<br>
    • I agree to the loan terms and conditions.
""".trimIndent()


    var  selectFirst: Boolean = false
    var  selectSecond: Boolean = false
    var  selectThird: Boolean = false
    var isBankVerified = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPaymentInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        panViewModel = ViewModelProvider(this, com.bosandroidapp.aopaykit.data.viewModelFactory.PanViewModelFactory(PanRepository(RetrofitClient.apiInterfacePAN)))[PanViewModel::class.java]
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

        api = RetrofitClient.apiInterfaceSMS
        preference = SharedPreference(this)

        setselectionForFirstCard()
        setView()
        hitApiForBankList()
        setDataInSpinner()
        setOnClickListner()
    }



    companion object{
        var checkKYC : Boolean = false
    }


    override fun onResume() {
        super.onResume()

        hitApiForLogin()
        if(ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.online)){

            if (checkKYC) {

                if (isSameMaskedAadhaar(AadharNumber, ReferenceAadharNumber)) {
                    checkKYC = false
                    binding.referenceKycChecked.isChecked = false
                    binding.banificeryName.isEnabled = true
                    Toast.makeText(this, "Customer and Reference Aadhaar should not be same.", Toast.LENGTH_SHORT).show()
                    return
                }

                binding.refername.text?.clear()
                binding.refername.setText(ConstantClass.ReferenceAadhaarName)

                binding.refername.isEnabled = ConstantClass.ReferenceAadhaarName.isEmpty()

                binding.referenceKycChecked.isChecked = true

                binding.refaddress.setText("${ConstantClass.ReferenceAadharHouse} " +
                        "${ConstantClass.ReferenceAadharStreet} " +
                        "${ConstantClass.ReferenceAadharLoc} " +
                        "${ConstantClass.ReferenceAadhardist}, " +
                        "${ConstantClass.ReferenceAadharState}, " +
                        "${ConstantClass.ReferenceAadharCountry}, " +
                        "${ConstantClass.ReferenceAadharPin}")

                Log.d("ReferenceName","${ConstantClass.ReferenceAadhaarName} ${ConstantClass.ReferenceAadharNumber}" )
            }

            else {
                if(!checkAdharForRef.isNullOrBlank() ){
                    Toast.makeText(this,"Customer and Reference Aadhaar should not be same.", Toast.LENGTH_SHORT).show()
                }
                binding.refername.isEnabled= true
                checkKYC =false
                binding.referenceKycChecked.isChecked = checkKYC
            }

        }

    }


    fun isSameMaskedAadhaar(customerMasked: String, referenceMasked: String): Boolean {
        val customerLast4 = customerMasked.takeLast(4)
        val referenceLast4 = referenceMasked.takeLast(4)
        val isAadhaarMatched = customerLast4 == referenceLast4
        Log.d("validateAadhaarnumber" , "${isAadhaarMatched}")
        return isAadhaarMatched
    }


    fun hitApiForBankList() {
        bankList.clear()

        var req = BankListReq(
            registrationID = ConstantClass.PAN_VERIFICATION_REGISTRATION_ID
        )

        panViewModel.getBankListReq(req).observe(this) { resources ->
          resources.let {
              when (it.apiStatus) {
                  ApiStatus.SUCCESS -> {
                      it.data.let { users ->
                          users!!.body().let { response ->
                              ConstantClass.dialog.dismiss()

                              if(response!!.status!!.toLowerCase().equals("false")){
                                  Toast.makeText(this@PaymentInformation,response.message, Toast.LENGTH_SHORT).show()
                              }

                              response?.data?.banks?.forEach {

                                  bankList.add(Pair(it!!.name!!, it.id) as Pair<String, Int>)

                                  bankName.add(it!!.name!!)
                                  bankName.sort()

                                  val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bankName)
                                  binding.bankname.setAdapter(adapter)

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

    fun setView(){

        binding.aggreement.text = android.text.Html.fromHtml(agreementText, android.text.Html.FROM_HTML_MODE_LEGACY)

        binding.ifsccode.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.ifsccode.error = "Emoji not allowed"
                        checkIFSCCOde = true
                    }else{
                        checkIFSCCOde = false
                    }
                }
            }

        })

        binding.branchname.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.branchname.error = "Emoji not allowed"
                        checkBranchName = true
                    }else{
                        checkBranchName = false
                    }
                }
            }

        })

        binding.refername.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.branchname.error = "Emoji not allowed"
                        checkRefrenceName = true
                    }else{
                        checkRefrenceName = false
                    }
                }
            }

        })

        binding.referrelatinonship.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.referrelatinonship.error = "Emoji not allowed"
                        checkRefrenceRelation = true
                    }
                    else{
                        checkRefrenceRelation = false
                    }
                }
            }

        })

        binding.refaddress.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.refaddress.error = "Emoji not allowed"
                        checkRefrenceaddress = true
                    }
                    else{
                        checkRefrenceaddress = false
                    }
                }
            }

        })

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


    fun setDataInSpinner(){
        val adapter = ArrayAdapter.createFromResource(this,  R.array.accounttype, R.layout.mobilenamelayout)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.acounttype.adapter = adapter
    }


    fun setOnClickListner(){

        binding.home.setOnClickListener {
            val intent = Intent(this, DashBoard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            onBackPressed()
        }

        binding.refmobno.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length == 10) {
                    binding.verifymobilenumber.visibility = View.VISIBLE
                } else {
                    binding.verifymobilenumber.visibility = View.GONE
                }
            }

        })

        binding.verifymobilenumber.setOnClickListener {

            var mobnumber = binding.refmobno.text.toString()

            if (validateLoginInput(mobnumber, this) && !binding.refername.text.toString().isNullOrBlank()) {
                if (isInternetAvailable(this@PaymentInformation)) {
                    hitApiForSendOTP(mobnumber, "Mobile")
                } else {
                    Toast.makeText(this, "Please check your internet connection!!", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (binding.refername.text.toString().isNullOrBlank()) {
                    Toast.makeText(this, "Please enter customer first or last name", Toast.LENGTH_SHORT).show()
                }
            }

        }


        binding.back.setOnClickListener {
            OpenPopUpForVAlert()
        }


        binding.bankname.setOnClickListener {
            binding.bankname.showDropDown()
        }


        binding.referenceKycChecked.setOnClickListener {
            hitApiForAadharVerification()
        }


        binding.nextlayout.setOnClickListener {

            when{
                selectFirst -> {

                    val hasBankDetails =
                        binding.accountnumber.text.toString().trim().isNotEmpty() ||
                                binding.ifsccode.text.toString().trim().isNotEmpty() ||
                                binding.bankname.text.toString().trim().isNotEmpty() ||
                                binding.branchname.text.toString().trim().isNotEmpty() ||
                                binding.banificeryName.text.toString().trim().isNotEmpty() ||
                                binding.branchaddress.text.toString().trim().isNotEmpty()

                    if (ConstantClass.CheckOnlineOrOffline == ConstantClass.online) {

                        // Online case - Penny Drop mandatory
                        val (isValid, errorMessage) = isPaymentValidForm(
                            accountNumber = binding.accountnumber.text.toString().trim(),
                            ifscCode = binding.ifsccode.text.toString().trim(),
                            bankName = binding.bankname.text.toString().trim(),
                            accountType = binding.acounttype.selectedItem.toString().trim(),
                            branchName = binding.branchname.text.toString().trim(),
                            benName = binding.banificeryName.text.toString().trim(),
                            branchAddress = binding.branchaddress.text.toString().trim()
                        )

                        if (!isValid) {
                            Toast.makeText(this@PaymentInformation, errorMessage, Toast.LENGTH_SHORT).show()
                        } else {
                            hitApiForRequestPennyDrop()
                        }

                    } else {

                        // Offline case - Bank details optional
                        if (!hasBankDetails) {

                            AccountNumber = binding.accountnumber.text.toString().trim()
                            BankIFSCCode = binding.ifsccode.text.toString().trim()
                            BankName = binding.bankname.text.toString().trim()
                            AccountType = binding.acounttype.selectedItem.toString().trim()
                            BranchName = binding.branchname.text.toString().trim()
                            AccountHolderName = binding.banificeryName.text.toString().trim()
                            BranchAddress = binding.branchaddress.text.toString().trim()
                            BankID = 0

                            setselectionForSecondCard()

                        } else {

                            val (isValid, errorMessage) = isPaymentValidForm(
                                accountNumber = binding.accountnumber.text.toString().trim(),
                                ifscCode = binding.ifsccode.text.toString().trim(),
                                bankName = binding.bankname.text.toString().trim(),
                                accountType = binding.acounttype.selectedItem.toString().trim(),
                                branchName = binding.branchname.text.toString().trim(),
                                benName = binding.banificeryName.text.toString().trim(),
                                branchAddress = binding.branchaddress.text.toString().trim()
                            )

                            if (!isValid) {
                                Toast.makeText(this@PaymentInformation, errorMessage, Toast.LENGTH_SHORT).show()
                            } else {
                                hitApiForRequestPennyDrop()
                            }
                        }
                    }

                }

                selectSecond -> {
                    if(!isBankVerified){
                        setselectionForThirdCard()
                    }
                    else{
                        var check = binding.aggrementchecked.isChecked
                        if(check){
                            setselectionForThirdCard()
                        }
                        else {
                            Toast.makeText(this@PaymentInformation, getString(R.string.please_accept_the_agreement), Toast.LENGTH_SHORT).show()
                        }
                    }


                }

                selectThird -> {
                    if(ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.online)){
                            val (isValid, errorMessage) = isReferenceValidOnlineForm(
                                referName = binding.refername.text.toString().trim(),
                                referLast = binding.referlast.text.toString().trim(),
                                referRelation = binding.referrelatinonship.text.toString().trim(),
                                refMobile = binding.refmobno.text.toString().trim(),
                                refAddress = binding.refaddress.text.toString().trim())

                            if (!isValid) {
                                binding.referenceKycChecked.isChecked = false
                                Toast.makeText(this@PaymentInformation, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                            else
                            {
                                if(checkKYC) {
                                    RefName = binding.refername.text.toString().trim()
                                    RefRelationShip = binding.referrelatinonship.text.toString().trim()
                                    RefmobileNo = binding.refmobno.text.toString().trim()
                                    RefAddress = binding.refaddress.text.toString().trim()
                                    startActivity(Intent(this@PaymentInformation, IMEIDetailsPage::class.java))
                                }
                                else {
                                    binding.referenceKycChecked.isChecked = false
                                    Toast.makeText(this@PaymentInformation, getString(R.string.please_accept_kyc_agreement), Toast.LENGTH_SHORT).show()
                                }

                            }

                    }

                    else{
                        if(ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.kit)){
                            RefName = binding.refername.text.toString().trim()
                            RefRelationShip = binding.referrelatinonship.text.toString().trim()
                            RefmobileNo = binding.refmobno.text.toString().trim()
                            RefAddress =  binding.refaddress.text.toString().trim()
                            startActivity(Intent(this@PaymentInformation, com.bosandroidapp.aopaykit.ui.view.activity.retailer.IMEIDetailsPage::class.java))
                        }
                        else{
                            val (isValid, errorMessage) = isReferenceValidForm(
                                referName = binding.refername.text.toString().trim(),
                                referRelation = binding.referrelatinonship.text.toString().trim(),
                                refMobile = binding.refmobno.text.toString().trim(),
                                refAddress = binding.refaddress.text.toString().trim())

                            if (!isValid) {
                                Toast.makeText(this@PaymentInformation, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                            else
                            {
                                RefName = binding.refername.text.toString().trim()
                                RefRelationShip = binding.referrelatinonship.text.toString().trim()
                                RefmobileNo = binding.refmobno.text.toString().trim()
                                RefAddress =  binding.refaddress.text.toString().trim()
                                startActivity(Intent(this@PaymentInformation, com.bosandroidapp.aopaykit.ui.view.activity.retailer.IMEIDetailsPage::class.java))
                            }
                        }

                    }


                }


            }

        }
    }

    fun hitApiForRequestPennyDrop(){
        binding.nextlayout.isEnabled=false
        var request = PennyDropRequest(
            bankName = binding.bankname.text.toString().trim(),
            benificiaryName = binding.banificeryName.text.toString().trim(),
            address = binding.branchaddress.text.toString().trim(),
            paymentMode = ConstantClass.ModeOfPayment,
            iFSCCode = binding.ifsccode.text.toString().trim(),
            registrationID = ConstantClass.PENNYDROP_REGISTRATION_ID,
            refID = "",
            accountNumber = binding.accountnumber.text.toString().trim(),
        )

        Log.d("PennyDropReq",Gson().toJson(request))

        panViewModel.getpennyDropReq(request).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                Log.d("PennyDropRes",Gson().toJson(response))
                                ConstantClass.dialog.dismiss()
                                if(response!!.model?.status.equals(ConstantClass.SUCCESS)){
                                    isBankVerified = true
                                    var beneficiaryName =  response.model!!.beneficiaryName

                                    val isMatch = beneficiaryName!!.trim()
                                        .lowercase()
                                        .startsWith(CustFirstName.trim().lowercase())

                                    if(ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.online)){
                                        if(isMatch){
                                            if(response!!.model!!.clientRefNum!!.isNotEmpty()){
                                                hitApiForRequestPennyDropCheckStatus(response!!.model!!.clientRefNum!!)
                                            }
                                        }
                                        else {
                                            OpenAlertForExit()
                                        }
                                    }
                                    else {
                                        if(response!!.model!!.clientRefNum!!.isNotEmpty()){
                                            hitApiForRequestPennyDropCheckStatus(response!!.model!!.clientRefNum!!)
                                        }
                                    }

                                }
                                else{
                                    isBankVerified = false
                                    binding.nextlayout.isEnabled = true
                                    Toast.makeText(this@PaymentInformation,response.message,Toast.LENGTH_LONG).show()
                                }
                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        binding.nextlayout.isEnabled = true
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }

                }

            }

        }

    }

    fun hitApiForRequestPennyDropCheckStatus(refID: String){
        var request = PennyDropCheckStatusRequest(
            registrationID = ConstantClass.PENNYDROP_REGISTRATION_ID,
            refID = refID,
        )

        Log.d("PennyDropCheckStatusReq",Gson().toJson(request))

        panViewModel.getpennyDropCheckStatusReq(request).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                Log.d("PennyDropCheckStatusRes",Gson().toJson(response))
                                ConstantClass.dialog.dismiss()
                                if(response!!.model?.status.equals(ConstantClass.SUCCESS)){
                                    AccountNumber = binding.accountnumber.text.toString().trim()
                                    BankIFSCCode = binding.ifsccode.text.toString().trim()
                                    BankName =  binding.bankname.text.toString().trim()
                                    AccountType = binding.acounttype.selectedItem.toString().trim()
                                    BranchName = binding.branchname.text.toString().trim()
                                    AccountHolderName= binding.banificeryName.text.toString().trim()
                                    BranchAddress= binding.branchaddress.text.toString().trim()
                                    BankID = bankList.find { it.first == BankName }?.second!!
                                    Log.d("BankID", "${BankID}")
                                    setselectionForSecondCard()
                                }
                                else{
                                    Toast.makeText(this@PaymentInformation,response.message,Toast.LENGTH_SHORT).show()
                                }
                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {

                    }

                }

            }

        }

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
                                        var firstName = binding.refername.text.toString()
                                        var customerName = firstName
                                        hitApiForMobVerify(mailidormobile, customerName, otp)
                                }
                                else{
                                    Toast.makeText(this@PaymentInformation,response.message,Toast.LENGTH_SHORT).show()
                                    ConstantClass.dialog.dismiss()
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

    fun hitApiForMobVerify(mobnumber: String, customerName: String, OTP: String) {
        // hint: Dear  Naim Khan, Your OTP for Verification is 1234. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER
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
                        this@PaymentInformation,
                        "Otp sent on your mobile number!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val loanData = response.body()

                    if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                        ConstantClass.dialog.dismiss()
                        OpenLoader(mobnumber, OTP)
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


    fun OpenLoader(EmailID: String, otp: String) {
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
        val title = dialog.findViewById<TextView>(R.id.text_subtitle)

        startOtpTimer(resendtxt, timer)

        title.text = "Enter four digit OTP send on your registered mobile number"


        cancel.setOnClickListener {
            dialog.dismiss()
        }

        resendlayout.setOnClickListener {

            if (validateLoginInput(EmailID, this)) {
                if (isInternetAvailable(this@PaymentInformation)) {
                    hitApiForReSendOTP(EmailID, "Mobile")
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
                    hitApiForOTPVerify(EmailID, enteredOTP, "Mobile verify")

            } else {
                Toast.makeText(this, "Please enter complete OTP", Toast.LENGTH_SHORT).show()
            }

        }

        dialog.show()

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
                                        binding.refmobno.isEnabled = false
                                        isRefMobVerified = true
                                        binding.verifyiconphonenumber.visibility = View.VISIBLE
                                        binding.verifymobilenumber.visibility = View.GONE

                                    if (dialog != null && dialog.isShowing) {
                                        dialog.dismiss()
                                    }
                                }else{
                                    isRefMobVerified = false

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
                                    var otp = response.value
                                    var firstName = binding.refername.text.toString()
                                    var customerName = firstName
                                    hitApiForResendMobVerify(mailidormobile, customerName, otp)
                                }
                                else{
                                    Toast.makeText(this@PaymentInformation,response.message,Toast.LENGTH_SHORT).show()
                                    ConstantClass.dialog.dismiss()
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

    fun hitApiForResendMobVerify(mobnumber: String, customerName: String, OTP: String) {
        // hint: Dear  Naim Khan, Your OTP for Verification is 1234. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER
        var message =
            "Dear $customerName, Your OTP for Verification is $OTP. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER "

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
                        this@PaymentInformation,
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


    fun isPaymentValidForm(
        accountNumber: String,
        ifscCode: String,
        bankName: String,
        accountType: String,
        branchName: String,
        benName: String,
        branchAddress: String,
    ): Pair<Boolean, String?> {
        // Bank details
        if (accountNumber.isBlank()) return Pair(false, "Enter account number")

        if (benName.isBlank()) return Pair(false, "Enter benificiary number")

        if (!ifscCode.matches(Regex("^[A-Z]{4}0[A-Z0-9]{6}$"))) return Pair(false, "Enter valid IFSC code")

        if(checkIFSCCOde) return Pair(false, "Enter valid IFSC code")

        if (bankName.equals(ConstantClass.SelectBank)) return Pair(false, "Please Select Bank")

        if (accountType.equals(ConstantClass.accountType)) return Pair(false, "Please select account type")

        if (branchName.isBlank()) return Pair(false, "Enter branch name")

        if (checkBranchName) return Pair(false, "Enter valid branch name")


        if (checkRefrenceName) return Pair(false, "Enter valid reference name")

        if (branchAddress.isBlank()) return Pair(false, "Enter branch address")

        return Pair(true, null)
    }


    fun isReferenceValidForm(referName: String, referRelation: String, refMobile: String, refAddress: String): Pair<Boolean, String?> {

        // Reference details
        if (referName.isBlank()) return Pair(false, "Enter reference name")

        if (checkRefrenceName) return Pair(false, "Enter valid reference name")

        if (referRelation.isBlank()) return Pair(false, "Enter reference relationship")

        if (checkRefrenceRelation) return Pair(false, "Enter valid reference relationship")

        if (!refMobile.matches(Regex("^[6-9]\\d{9}$"))) return Pair(false, "Enter valid reference mobile number")

        if (refAddress.isBlank()) return Pair(false, "Enter reference address")

        if (checkRefrenceaddress) return Pair(false, "Enter valid reference address")

        return Pair(true, null)
    }


    fun isReferenceValidOnlineForm(
        referName: String,
        referLast: String,
        referRelation: String,
        refMobile: String,
        refAddress: String
    ): Pair<Boolean, String?> {

        // First Name
        if (referName.isBlank())
            return Pair(false, "Enter reference first name")

        if (checkRefrenceName)
            return Pair(false, "Enter valid reference first name")

        // Last Name
        if (referLast.isBlank())
            return Pair(false, "Enter reference last name")

        // Relationship
        if (referRelation.isBlank())
            return Pair(false, "Enter reference relationship")

        if (checkRefrenceRelation)
            return Pair(false, "Enter valid reference relationship")

        // Primary Mobile
        if (!refMobile.matches(Regex("^[6-9]\\d{9}$")))
            return Pair(false, "Enter valid reference mobile number")


        // Address
        if (refAddress.isBlank())
            return Pair(false, "Enter reference address")

        if (checkRefrenceaddress)
            return Pair(false, "Enter valid reference address")

        return Pair(true, null)
    }



    fun OpenAlertForExit(){

        dialog = Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.emialert_retailer)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }

        var textmsg = dialog.findViewById<TextView>(R.id.dialog_message)
        var btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        var Ok = dialog.findViewById<Button>(R.id.Ok)

        btnCancel.visibility=View.GONE

        textmsg.text = getString(R.string.aadhaar_and_bank)


        Ok.setOnClickListener {
            binding.nextlayout.isEnabled = true
            dialog.dismiss()
        }

        dialog.setCanceledOnTouchOutside(false)

        dialog.show()

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
                                ConstantClass.checkActiveStatusAndLogout(this@PaymentInformation, response.status, preference)
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
                                val intent = Intent(this@PaymentInformation, ChooseYourRolePage::class.java)
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



    fun setselectionForFirstCard(){
        selectFirst= true
        selectThird = false
        selectSecond= false

        binding.firststepCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        binding.firststep.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.firstview.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))


        binding.secondstepCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.secondstep.setTextColor(ContextCompat.getColor(this, R.color.lightgrey))
        binding.secondview.setBackgroundColor(ContextCompat.getColor(this, R.color.grey))

        binding.thirdstepCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.thirdstep.setTextColor(ContextCompat.getColor(this, R.color.lightgrey))

        binding.paymentdetails.visibility=View.VISIBLE
        binding.enachDetailslayout.visibility=View.GONE
        binding.referenceDetailsLayout.visibility=View.GONE

    }


    fun setselectionForSecondCard(){
        selectFirst = false
        selectThird = false
        selectSecond= true

        binding.nextlayout.isEnabled = true

        binding.firststepCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        binding.firststep.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.firstview.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))

        binding.secondstepCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        binding.secondstep.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.secondview.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))

        binding.thirdstepCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
        binding.thirdstep.setTextColor(ContextCompat.getColor(this, R.color.lightgrey))

        binding.paymentdetails.visibility=View.GONE
        binding.enachDetailslayout.visibility=View.VISIBLE
        binding.referenceDetailsLayout.visibility=View.GONE

    }


    fun setselectionForThirdCard(){
        selectFirst = false
        selectThird = true
        selectSecond= false

        binding.firststepCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        binding.firststep.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.firstview.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))

        binding.secondstepCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        binding.secondstep.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.secondview.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))

        binding.thirdstepCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blue))
        binding.thirdstep.setTextColor(ContextCompat.getColor(this, R.color.white))

        binding.paymentdetails.visibility=View.GONE
        binding.enachDetailslayout.visibility=View.GONE
        binding.referenceDetailsLayout.visibility=View.VISIBLE

        if (ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.online)) {
            binding.referenceKycChecked.visibility = View.VISIBLE
        }
        else{
            binding.referenceKycChecked.visibility = View.GONE
        }
    }


    fun hitApiForAadharVerification() {
        val firstName = binding.refername.text.toString().trim().orEmpty()
        val lastName = binding.referlast.text.toString().trim().orEmpty()
        val emailId = "bos.centerpvtltd@gmail.com"
        val mob = binding.refmobno.text.toString().trim().orEmpty()

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
                                    Toast.makeText(this@PaymentInformation, response.message, Toast.LENGTH_SHORT).show()
                                }
                                if (response!!.code.equals("200")) {
                                    AadharCardReferenceWebViewDIGILockerPage.digilockerLink = response!!.model.kycUrl
                                    RefAadharTransactionIdNo = response.model.transactionId
                                    Log.d("Referencedigilockeurl",  AadharCardReferenceWebViewDIGILockerPage.digilockerLink)
                                    startActivity(Intent(this@PaymentInformation, AadharCardReferenceWebViewDIGILockerPage::class.java))
                                }
                                else {
                                    Toast.makeText(this@PaymentInformation, response.message, Toast.LENGTH_SHORT).show()
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



}