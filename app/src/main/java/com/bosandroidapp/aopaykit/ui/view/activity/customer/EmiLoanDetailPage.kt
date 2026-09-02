package com.bosandroidapp.aopaykit.ui.view.activity.customer

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bos.payment.appName.network.ApiInterface
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityEmiLoanDetailPageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.Customer
import com.bosandroidapp.aopaykit.constant.ConstantClass.HoldAmount
import com.bosandroidapp.aopaykit.constant.ConstantClass.MaxHoldingAmount
import com.bosandroidapp.aopaykit.constant.ConstantClass.MinHoldingAmount
import com.bosandroidapp.aopaykit.constant.ConstantClass.OTPTYPE
import com.bosandroidapp.aopaykit.constant.ConstantClass.WalletBalance
import com.bosandroidapp.aopaykit.constant.ConstantClass.formatDateToDDMMYYYY
import com.bosandroidapp.aopaykit.constant.ConstantClass.formatDueDateGracePeriodDateToDDMMYYYY
import com.bosandroidapp.aopaykit.constant.ConstantClass.getCurrentUtcTimestamp
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.constant.ConstantClass.saveImageToCache
import com.bosandroidapp.aopaykit.constant.ConstantClass.validateLoginInput
import com.bosandroidapp.aopaykit.data.model.RetailerWalletAmountReq
import com.bosandroidapp.aopaykit.data.model.RetailerWalletPayoutAtMakePaymentTimeReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerLoanEmiReceiveReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.VerifyOTPReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.SendOtpReq
import com.bosandroidapp.aopaykit.data.pg.PGRequestCall
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.repository.PanRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.data.viewModelFactory.PanViewModelFactory
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.view.activity.customer.PGWebViewActivity.Companion.emiList
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.ui.viewmodel.PanViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.bosandroidapp.aopaykit.utils.MonthsAndPayables
import com.bosandroidapp.aopaykit.utils.getCurrentLastPaidDueDate
import com.chaos.view.PinView
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import kotlin.text.equals
import kotlin.toString

class EmiLoanDetailPage : BaseActivity() {
    lateinit var binding : ActivityEmiLoanDetailPageBinding
    lateinit var dialog : Dialog
    lateinit var viewModel: AuthenticationViewModel
    var loanCode: String = ""
    var emiAmount: String = ""
    var paymentDate: String = ""
    var paidAmount: String = ""
    var paymentMode: String = ""
    var createdBy: String = ""
    var latefine : String? = ""
    var isEmandateVerified : String? = ""

    var isPannydropVerified : String? = ""

    var MonthlyInterestAmount : Double = 0.0

    var emiamount : Double = 0.0
    var AllgracePeriod : String? = ""
    var CustomergracePeriod : String? = ""

    var BounceCharge : String = ""

    var Othercharges : String = ""

    var WaiveOff : String = ""
    var nextDueDate : String = ""
    var checkpaynow : Boolean = false
    lateinit var preference : SharedPreference
    var listOfDueWithGraceDate : ArrayList<MonthsAndPayables> = arrayListOf()
    lateinit var logintype: String
    private val CAMERA_REQUEST_CODE_FRONT = 1001
    private var photoUri: Uri? = null
    lateinit var receiptUri : Uri
    var imagepath: String? = ""
    var checktxnNumber : Boolean = false
    lateinit var api : ApiInterface

    var BounceChargeApplicable : String = ""

    lateinit var countDownTimer: CountDownTimer

    var selectedNoofEmi: Int = 0
    lateinit var panViewModel: PanViewModel


    companion object{
        lateinit var LoanId : String
        lateinit var customerCode : String
    }


    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Handle the photoUri, e.g., show image in ImageView
            binding.receiptPhoto.visibility = View.VISIBLE
            binding.cameraicon.visibility = View.GONE
            binding.receiptPhoto.setImageURI(photoUri)
            binding.clicktosealphoto1.text="Re-Send"
            receiptUri= photoUri!!
            imagepath = saveImageToCache(this,receiptUri,"ReceiptPhoto")!!.absolutePath

        }else{
            photoUri = null
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmiLoanDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        panViewModel = ViewModelProvider(this, PanViewModelFactory(PanRepository(RetrofitClient.apiInterfacePAN)))[PanViewModel::class.java]

        api = RetrofitClient.apiInterfaceSMS
        preference = SharedPreference(this)
        logintype = preference.getStringValue(ConstantClass.LoginType, "").orEmpty()

        setOnClickListner()
        setviewCondition()

    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        HitApiForEmiList()
        hitApiForRetailerWalletAmount()
    }



    fun hitApiForRetailerWalletAmount() {

        var registrationID = preference.getStringValue(ConstantClass.RetailerCode, "")

        var request = RetailerWalletAmountReq(
            retailerID = registrationID,
            amountType = "CreditBalance",
            clientCode = preference.getStringValue(ConstantClass.ClientCode, "")
        )

        Log.d("walletAmountReq", Gson().toJson(request))
        viewModel.getRetailerWalletAmountReq(request).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {

                        it.data?.let { users ->
                            users.body()?.let { response ->

                                Log.d("Walletamount", response.walletBalance!!)

                                val walletAmount = response.walletBalance!!.toDoubleOrNull() ?: 0.0
                                val holdAmount = response.holdAmount!!.toDoubleOrNull() ?: 0.0
                                val maxholdAmount = response.maxholdAmount!!.toDoubleOrNull() ?: 0.0
                                val minholdAmount = response.miniholdamountrequest!!.toDoubleOrNull() ?: 0.0


                                val myWalletAmount = walletAmount
                                val myWalletAmountStr = String.format("%.2f", myWalletAmount)

                                WalletBalance = myWalletAmountStr
                                HoldAmount = String.format("%.2f", holdAmount)
                                MaxHoldingAmount = String.format("%.2f", maxholdAmount)
                                MinHoldingAmount = String.format("%.2f", minholdAmount)


                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                            ConstantClass.dialog.dismiss()
                        }

                        // ✅ Print the full error details
                        Log.e("API_ERROR", "Status: ERROR")
                        Log.e("API_ERROR_CODE", resources.data?.code().toString())
                        Log.e("API_ERROR_MSG", resources.message ?: "Unknown Error")

                        Toast.makeText(this, "Server error occurred (Code: ${resources.data?.code() ?: "Unknown"})", Toast.LENGTH_LONG).show()

                        // Optional: Handle specific 500 error
                        if (resources.data?.code() == 500) {
                            Log.e("API_ERROR", "Internal Server Error from backend.")
                        }

                    }

                    ApiStatus.LOADING -> {
                        // ConstantClass.OpenLoader(this)
                    }

                }
            }
        }

    }


    fun setviewCondition(){

        binding.remarkEdt.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.remarkEdt.error = "Emoji not allowed"
                        checkpaynow = true
                    }else{
                        checkpaynow = false
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

    fun setDataForProgress(paidAmount:Double, remainingAmount:Double){
        val totalAmount = paidAmount + remainingAmount
        val progressPercent = (paidAmount * 100) / totalAmount

        binding.progressBar.max = 100
        binding.progressBar.progress = progressPercent.toInt()

    }

    fun setDataInspinner(tenure:Int,emiAmount:Double?){

        if (logintype.equals(Customer)){
            binding.qrcodelayout.visibility=View.GONE
            binding.txnNumber.visibility=View.GONE
            binding.uploadphotolayout.visibility=View.GONE
            binding.paymentmodelayout.visibility=View.GONE
            val companyAdapter = ArrayAdapter.createFromResource(this,  R.array.paymentmodeCustomer, R.layout.mobilenamelayout)
            companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.paymentmode.adapter = companyAdapter
        }
        else{
            binding.qrcodelayout.visibility=View.GONE
            binding.txnNumber.visibility=View.GONE
            binding.uploadphotolayout.visibility=View.GONE
            binding.paymentmodelayout.visibility=View.GONE
            val companyAdapter = ArrayAdapter.createFromResource(this,  R.array.paymentmodeRetailer, R.layout.mobilenamelayout)
            companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.paymentmode.adapter = companyAdapter
        }


        binding.paymentmode.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                Log.d("paymentmode", selectedItem)


                if(selectedItem.equals("Cash")){
                    binding.txnNumber.visibility=View.GONE
                    binding.uploadphotolayout.visibility=View.GONE
                    binding.qrcodelayout.visibility=View.GONE
                }

                if(selectedItem.equals("NEFT")||selectedItem.equals("Cheque")){
                    binding.txnNumber.visibility=View.VISIBLE
                    binding.uploadphotolayout.visibility=View.VISIBLE
                    binding.qrcodelayout.visibility=View.GONE
                }

                if(selectedItem.equals("UPI")){
                    binding.qrcodelayout.visibility=View.GONE
                    binding.txnNumber.visibility=View.GONE
                    binding.uploadphotolayout.visibility=View.GONE
                }

                else{

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val emiOptions = ConstantClass.generateEMIOptions(tenure)
        val noOfEmiAdapter = ArrayAdapter(this, R.layout.mobilenamelayout, emiOptions )
        noOfEmiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.noOfEmi.adapter = noOfEmiAdapter

        binding.noOfEmi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString().toInt()
                var totalEmiAmount = 0.0   // use Double for calculation
                Log.d("ListDueGrace",":".plus(listOfDueWithGraceDate))
                if(listOfDueWithGraceDate.isNotEmpty()){

                    for (j in 1..selectedItem) {
                        val emiIndex = j - 1
                        val emiAmountWithFine = if (listOfDueWithGraceDate[emiIndex].lateFeesApplied) {
                            if (isEmandateVerified!!.toLowerCase().equals("yes",ignoreCase = true)) {
                                emiAmount!!.toDouble() + latefine!!.toDouble() + BounceCharge!!.toDouble() + Othercharges!!.toDouble() + WaiveOff!!.toDouble()
                            }
                            else {
                                emiAmount!!.toDouble() + latefine!!.toDouble() + Othercharges!!.toDouble()
                            }
                        }
                        else {
                            emiAmount!!.toDouble()
                        }
                        Log.d("totalamount", emiAmountWithFine.toString())
                        totalEmiAmount += emiAmountWithFine
                    }

                    binding.amount.text = "₹ ${totalEmiAmount}"

                    // makepaymentamount =  "$totalEmiAmount"
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional
            }

        }

    }


    fun setOnClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.clicktosealphoto1.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }


        binding.txnNumber.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.txnNumber.error = "Emoji not allowed"
                        checktxnNumber = true
                    }else{
                        checktxnNumber = false
                    }
                }
            }

        })


        binding.submitpayment.setOnClickListener {
            if(isInternetAvailable(this@EmiLoanDetailPage)) {
                emiamount = binding.amount.text.toString().replace("₹ ","").toDouble()

                if(binding.paymentmode.selectedItem.toString().equals("Cash")){
                    if(checkpaynow){
                        Toast.makeText(this,"Please enter valid remarks.",Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    Log.d("emiamount", " $emiamount $WalletBalance")

                    if(emiamount <= WalletBalance.toDouble()) {
                        OpenAlertForEmiRequest(customerCode)
                        //HitApiForRetailerWalletPayoutAmount(emiamount)
                    }
                    else{
                        Toast.makeText(this, "You don't have sufficient balance for raise emi.", Toast.LENGTH_SHORT).show()
                    }

                }
                else
                {
                    /*if(isInternetAvailable(this@EmiLoanDetailPage)) {

                        if (imagepath!!.isBlank()) {
                            Toast.makeText(this, "Please upload receipt photo", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        if(binding.txnNumber.text.toString().equals("")){
                            Toast.makeText(this, "Please enter transaction number", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            if(checktxnNumber){
                                Toast.makeText(this, "Please enter valid transaction number", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }

                            if(binding.remarkEdt.text.toString().trim().equals("")){
                                Toast.makeText(this, "Please enter remarks", Toast.LENGTH_SHORT).show()
                                return@setOnClickListener
                            }

                            lifecycleScope.launch {
                                var selectedNoofEmi = binding.noOfEmi.selectedItem.toString().toInt()
                                val file = saveImageToCache(this@EmiLoanDetailPage,receiptUri,"ReceiptPhoto")
                                ConstantClass.OpenLoader(this@EmiLoanDetailPage)
                                var ForServerlatefine:String ?= ""

                                for (j in 1..selectedNoofEmi) {

                                    val emiIndex = j - 1

                                    val emiAmountWithFine: String

                                    if (listOfDueWithGraceDate[emiIndex].lateFeesApplied) {
                                        if(isEmandateVerified!!.toLowerCase().equals("yes",ignoreCase = true)){
                                            emiAmountWithFine = (emiAmount.toDouble() + latefine!!.toDouble() + BounceCharge!!.toDouble() + Othercharges!!.toDouble() + WaiveOff!!.toDouble()).toString()
                                            BounceChargeApplicable = BounceCharge
                                        }
                                        else{
                                            emiAmountWithFine = (emiAmount.toDouble() + latefine!!.toDouble() + Othercharges!!.toDouble()).toString()
                                            BounceChargeApplicable = "0"
                                        }
                                        ForServerlatefine = latefine
                                    }
                                    else {
                                        emiAmountWithFine = emiAmount
                                        ForServerlatefine = "0"
                                        BounceChargeApplicable = "0"
                                    }
                                    Log.d("EMIAmount", emiAmountWithFine)
                                    HitApiForPayEmiAmount(selectedNoofEmi, j, emiAmountWithFine,ForServerlatefine, file!!)
                                    delay(1000) // wait 1 second between calls
                                }

                            }
                        }
                    }*/

                    if(isInternetAvailable(this@EmiLoanDetailPage)) {

                        lifecycleScope.launch {
                            selectedNoofEmi = binding.noOfEmi.selectedItem.toString().toInt()
                            // val file = saveImageToCache(this@EmiLoanDetailPage,receiptUri,"ReceiptPhoto")
                            // ConstantClass.OpenLoader(this@EmiLoanDetailPage)
                            var ForServerlatefine:String ?= ""
                            PGWebViewActivity.emiList.clear()

                            for (j in 1..selectedNoofEmi) {

                                val emiIndex = j - 1

                                val emiAmountWithFine: String

                                if (listOfDueWithGraceDate[emiIndex].lateFeesApplied) {
                                    if(isEmandateVerified!!.toLowerCase().equals("yes",ignoreCase = true)){
                                        emiAmountWithFine = (emiAmount.toDouble() + latefine!!.toDouble() + BounceCharge!!.toDouble() + Othercharges!!.toDouble() + WaiveOff!!.toDouble()).toString()
                                        BounceChargeApplicable = BounceCharge
                                    }
                                    else{
                                        emiAmountWithFine = (emiAmount.toDouble() + latefine!!.toDouble() + Othercharges!!.toDouble()).toString()
                                        BounceChargeApplicable = "0"
                                    }
                                    ForServerlatefine = latefine
                                }
                                else {
                                    emiAmountWithFine = emiAmount
                                    ForServerlatefine = "0"
                                    BounceChargeApplicable = "0"
                                }

                                Log.d("emiAmountWithFine", emiAmountWithFine)
                                Log.d("EMIAmount", emiAmountWithFine)
                                emiList.add(EmiData(selectedNoofEmi,emiNo = j, emiAmount = emiAmountWithFine, lateFine = ForServerlatefine!!,BounceChargeApplicable,loanCode))
                                //val file = saveImageToCache(this@EmiLoanDetailPage,receiptUri,"ReceiptPhoto")
                                // HitApiForPayEmiAmount(selectedNoofEmi, j, emiAmountWithFine,ForServerlatefine)
                                // delay(1000) // wait 1 second between calls
                            }

                            val email = preference.getStringValue(ConstantClass.CustomerEmailID, "") .ifEmpty { "bos.centerpvtltd@gmail.com" }
                            val emiNumbers=  (1..selectedNoofEmi).joinToString("")
                            PGWebViewActivity.LoanCodePG = loanCode

                            var req = PGRequestCall(
                                payCustomerPhoneNo = preference.getStringValue(ConstantClass.CustomerMobileNumber, ""),
                                customerEmailID = email,
                                registrationID = ConstantClass.PAN_VERIFICATION_REGISTRATION_ID,
                                payCartAmount = "1"/*emiamount.toString()*/,
                                eMINumbers = "EMI${emiNumbers}",
                                customerCode = preference.getStringValue(ConstantClass.CustomerCode, ""),
                                payCustomerName = "${preference.getStringValue(ConstantClass.FirstName, "")} ${preference.getStringValue(ConstantClass.LastName, "")}",
                                loanCode = loanCode,
                                clientCode = preference.getStringValue(ConstantClass.ClientCode, "")
                            )

                            hitApiForRequestPG(req)

                            // }
                        }

                    }
                }
            }
            else{
                Toast.makeText(this@EmiLoanDetailPage,"Please connect with internet", Toast.LENGTH_SHORT).show()
            }

        }


    }


    fun hitApiForRequestPG(req : PGRequestCall){

        Log.d("PGRequest", Gson().toJson(req))

        panViewModel.getPGRequestCall(req).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                Log.d("PanVerificationResp", Gson().toJson(response))

                                if (response!!.status?.toLowerCase().equals("true",ignoreCase = true) && !response.preparePOSTForm.isNullOrEmpty()) {
                                    // Open WebView with the provided URL
                                    ConstantClass.dialog.dismiss()
                                    val intent = Intent(this@EmiLoanDetailPage, PGWebViewActivity::class.java)
                                    intent.putExtra("pgurl", response.preparePOSTForm)
                                    startActivity(intent)
                                }
                                else {
                                    ConstantClass.dialog.dismiss()
                                    Toast.makeText(this@EmiLoanDetailPage, response.message, Toast.LENGTH_SHORT).show()
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


    data class EmiData(
        val selectedNoofEmi:Int,
        val emiNo: Int,
        val emiAmount: String,
        val lateFine: String,
        val bounceCharge: String,
        val loancode : String
    )


    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            clickCameraForUploadDocument()
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE_FRONT)
        }
    }


    fun clickCameraForUploadDocument() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
        cameraLauncher.launch(photoUri!!)
    }


    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun HitApiForEmiList(){
        var loanemireq = GetCustomerLoanDetailsReq(
            loancode = LoanId,
            customercode = "",
            clientCode = preference.getStringValue(ConstantClass.ClientCode, "")
        )
        Log.d("customerloanEmireq", Gson().toJson(loanemireq))

        viewModel.getCustomerLoanEmiDetailsReq(loanemireq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let {
                                    response ->
                                Log.d("customerLoanemiresp", response.toString())
                                if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                                    ConstantClass.dialog.dismiss()
                                    var LoanEmiList = response.data
                                    var currentDate = response.indiaTimeIST

                                    val df = DecimalFormat("0.00")

                                    binding.loanamount.text = "₹  ${df.format(LoanEmiList!![0]!!.loanAmount)}"

                                    latefine = LoanEmiList[0]!!.lateFine
                                    binding.latefineamount.text = latefine
                                    AllgracePeriod = LoanEmiList[0]!!.gracePeriod
                                    CustomergracePeriod = LoanEmiList[0]!!.customerGracePeriod

                                    var amount = calculateEMIPaymentStatus(LoanEmiList!![0]!!.paidEMI!!.toInt(),LoanEmiList!![0]!!.emiAmount!!.toDouble(), LoanEmiList!![0]!!.tenure)

                                    binding.paidamount.text = "₹ ${df.format(amount.first)}"
                                    binding.remainamount.text = "₹ ${df.format(amount.second)}"

                                    setDataForProgress(amount.first,amount.second!!)

                                    binding.monthlyemi.text = "₹ ${df.format(LoanEmiList!![0]!!.emiAmount!!.toDoubleOrNull() ?: 0.0)}"
                                    binding.modelname.text = LoanEmiList!![0]!!.modelName.toString()

                                    binding.enddate.text = formatDateToDDMMYYYY(LoanEmiList!![0]!!.endDate.toString())
                                    binding.noofpaidemi.text = LoanEmiList!![0]!!.paidEMI.toString()
                                    binding.pendingnoofemi.text = LoanEmiList!![0]!!.duesEMI.toString()
                                    binding.color.text = LoanEmiList!![0]!!.avlbColors.toString()
                                    binding.bouncecharge.text = LoanEmiList!![0]!!.applicableBounceCharge.toString()
                                    binding.othercharge.text = LoanEmiList!![0]!!.applicableOtherCharge.toString()
                                    binding.waiveoff.text = LoanEmiList!![0]!!.applicableWaiveOff.toString()


                                    BounceCharge = String.format("%.2f", LoanEmiList?.get(0)?.applicableBounceCharge?.toDoubleOrNull() ?: 0.0)
                                    Othercharges = String.format("%.2f", LoanEmiList?.get(0)?.applicableOtherCharge?.toDoubleOrNull() ?: 0.0)
                                    WaiveOff = String.format("%.2f", LoanEmiList?.get(0)?.applicableWaiveOff?.toDoubleOrNull() ?: 0.0)


                                    loanCode = LoanEmiList!![0]!!.loanCode.toString()
                                    emiAmount = LoanEmiList!![0]!!.emiAmount.toString()
                                    paidAmount = LoanEmiList!![0]!!.emiAmount.toString()
                                    isEmandateVerified = LoanEmiList!![0]!!.isEmandateVerified.toString()
                                    isPannydropVerified = LoanEmiList!![0]!!.isPannydropVerified.toString()

                                    val gracePeriod = AllgracePeriod!!.toInt()+CustomergracePeriod!!.toInt()

                                    nextDueDate =  formatDueDateGracePeriodDateToDDMMYYYY(LoanEmiList!![0]!!.startDate.toString(),gracePeriod.toInt(),LoanEmiList!![0]!!.paidEMI!!.toInt()) // start date is due date of emi and here due is showing next due date

                                    lifecycleScope.launch {
                                        listOfDueWithGraceDate = formatDateToDDMMYYYY(LoanEmiList!![0]!!.startDate.toString()).getCurrentLastPaidDueDate(this@EmiLoanDetailPage,LoanEmiList!![0]!!.paidEMI!!.toLong(),LoanEmiList!![0]!!.duesEMI!!.toLong(), AllgracePeriod!!.toInt(),CustomergracePeriod!!.toInt(),currentDate!!)
                                        Log.d("DueList", "Data". plus(listOfDueWithGraceDate))
                                        setDataInspinner(LoanEmiList!![0]!!.duesEMI!!.toInt(),LoanEmiList?.get(0)?.emiAmount?.toDoubleOrNull() ?: 0.0)
                                    }

                                    if(LoanEmiList!![0]!!.paidEMI.toString()==LoanEmiList!![0]!!.tenure.toString()){
                                        binding.emidetailspaynowlayout.visibility=View.GONE
                                        binding.doneemitext.visibility=View.VISIBLE
                                        binding.duestatustitle.text = this.getString(R.string.status)
                                        binding.startdate.text = this.getString(R.string.emistatus)
                                        binding.startdate.setTextColor(getColor(R.color.green))
                                    }

                                    else{
                                        binding.emidetailspaynowlayout.visibility=View.VISIBLE
                                        binding.doneemitext.visibility=View.GONE
                                        binding.duestatustitle.text = this.getString(R.string.due_date)
                                        binding.startdate.text = formatDateToDDMMYYYY(LoanEmiList!![0]!!.startDate.toString())
                                        binding.startdate.setTextColor(getColor(R.color.black))
                                    }

                                }
                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
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



    fun HitApiForPayEmiAmount(emicount:Int,loopcount :Int,emiamount : String,fine:String?){

        var createdBy=""

        if(logintype.equals(Customer)){
            createdBy = preference.getStringValue(ConstantClass.CustomerCode, "")
        }
        else{
            createdBy = preference.getStringValue(ConstantClass.RetailerCode, "")
        }

        var customercode =  customerCode
        var retailercode =  preference.getStringValue(ConstantClass.RetailerCode, "")


        val request = CustomerLoanEmiReceiveReq(
            mode = "UPDATE",
            loanCode = loanCode,
            paymentDate = getCurrentUtcTimestamp(),
            paymentMode =  "",
            utrNumber = "",
            remarks = binding.remarkEdt.text.toString(),
            createdBy = createdBy,
            receiptNo = "",
            customerCode =customercode,
            retailerCode = retailercode,
            bankName = "",
            receiptImagePath = ""/*,
            imageFile = imageFile*/
        )

        Log.d("loanEmiReceiveReq", Gson().toJson(request))

        viewModel.getCustomerLoanDetailsReq(request).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let {
                                    response ->
                                Log.d("loanEmiReceiveResp", response.toString())
                                if(loopcount==emicount){
                                    if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                                        ConstantClass.dialog.dismiss()
                                    }
                                    Toast.makeText(this@EmiLoanDetailPage,response.message,Toast.LENGTH_SHORT).show()
                                    finish()

                                    /* if(logintype.equals(Customer)){
                                         OpenLoader()
                                     }
                                     else{
                                         startActivity(Intent(this@EmiLoanDetailPage, MakePaymentPage::class.java))
                                         makepaymentloanid = LoanId
                                         makepaymentemicount = "$loopcount"
                                         finish()
                                     }*/

                                }
                            }
                        }

                    }

                    ApiStatus.ERROR -> {
                        if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                            ConstantClass.dialog.dismiss()
                        }

                    }

                    ApiStatus.LOADING -> {

                    }

                }
            }
        }

    }


    fun OpenLoader(){
        dialog = Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_for_payment)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }

        var exitbutton = dialog.findViewById<LinearLayout>(R.id.exitlayout)
        var shareImage = dialog.findViewById<ImageView>(R.id.shareimage)

        shareImage.setOnClickListener {
            shareImageFromDrawable(this, R.drawable.bosqrimage)
        }


        exitbutton.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this@EmiLoanDetailPage, DashBoard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


        dialog.setOnDismissListener{
            dialog.dismiss()
            val intent = Intent(this@EmiLoanDetailPage, DashBoard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        dialog.setCanceledOnTouchOutside(false)

        dialog.show()

    }


    fun calculateEMIPaymentStatus(paidEMI: Int, emiAmount: Double, tenure: Int?): Pair<Double, Double?> {
        val paidAmount = paidEMI * emiAmount
        val remainingAmount = (tenure?.minus(paidEMI))?.times(emiAmount)
        return Pair(paidAmount, remainingAmount)
    }


    fun shareImageFromDrawable(context: Context, drawableResId: Int) {
        // Convert drawable to bitmap
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)

        // Save bitmap to cache directory
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs() // Create directory if not exists
        val file = File(cachePath, "shared_image.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()

        // Get content URI
        val imageUri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        // Share Intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
    }


    fun HitApiForRetailerWalletPayoutAmount(selectedNoofEmi:Int,j :Int,emiAmountWithFine : String,ForServerlatefine:String?,imageFile:File){

        var amount = emiAmountWithFine.toDouble()

        var retailercode =  preference.getStringValue(ConstantClass.RetailerCode, "")
        var request = RetailerWalletPayoutAtMakePaymentTimeReq(
            amountType = "EMI",
            gstAmount = 0,
            transferFrom = retailercode,
            serviceschargeAmount = 0,
            actualCommissionAmount = 0,
            transIpAddress = "",
            transferAmount = amount,
            remark = binding.remarkEdt.text.toString(),
            customerCommissionWithoutGST = 0,
            transferTo = "Admin",
            transferToMsg = "Your Account is credited by ${amount} Due to Paid EMI on customer code :${customerCode}",
            serviceschargeWithoutGST = 0,
            customerCommission = 0,
            customerCommissionGST = 0,
            commissionWithoutGST = 0,
            transferFromMsg = "Your Account is debited by ${amount}Rs.Due to Paid EMI on customer code :${customerCode}",
            registrationId = retailercode,
            tdsAmount = 0,
            serviceschargeGSTAmount = 0,
            transactionStatus = "Approved",
            loanCode = loanCode,
            clientCode = preference.getStringValue(ConstantClass.ClientCode, "")
        )

        Log.d("payoutrequest", Gson().toJson(request))

        viewModel.getRetailerWalletPayoutReqForMakePayment(request).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let {
                                    response ->
                                Log.d("payoutresponse", response.toString())

                                if(response.statuss.equals("True")){
                                    /*var selectedNoofEmi = binding.noOfEmi.selectedItem.toString().toInt()

                                    lifecycleScope.launch {
                                        val imageFile = File(" ")
                                        var ForServerlatefine:String? = ""

                                        for (j in 1..selectedNoofEmi) {

                                            val emiIndex = j - 1

                                            val emiAmountWithFine: String

                                            if (listOfDueWithGraceDate[emiIndex].lateFeesApplied) {
                                                if(isEmandateVerified.equals("Yes")){
                                                    emiAmountWithFine = (emiAmount.toDouble() + latefine!!.toDouble() + BounceCharge!!.toDouble() + Othercharges!!.toDouble() + WaiveOff!!.toDouble()).toString()
                                                }
                                                else{
                                                    emiAmountWithFine = (emiAmount.toDouble() + latefine!!.toDouble() + Othercharges!!.toDouble()).toString()
                                                }
                                                ForServerlatefine = latefine
                                            }
                                            else {
                                                emiAmountWithFine = emiAmount
                                                ForServerlatefine = "0"
                                            }
                                            Log.d("EMIAmount", emiAmountWithFine)
                                            HitApiForPayEmiAmount(selectedNoofEmi, j, emiAmountWithFine,ForServerlatefine,imageFile)
                                            delay(1000) // wait 1 second between calls
                                        }
                                    }*/

                                    HitApiForPayEmiAmount(selectedNoofEmi, j, emiAmountWithFine,ForServerlatefine)
                                }
                                else{
                                    Toast.makeText(this@EmiLoanDetailPage,response.message,Toast.LENGTH_SHORT).show()
                                }

                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                            ConstantClass.dialog.dismiss()
                        }
                        // ✅ Print the full error details
                        Log.e("API_ERROR", "Status: ERROR")
                        Log.e("API_ERROR_CODE", resources.data?.code().toString())
                        Log.e("API_ERROR_MSG", resources.message ?: "Unknown Error")

                        Toast.makeText(this, "Server error occurred (Code: ${resources.data?.code() ?: "Unknown"})", Toast.LENGTH_LONG).show()

                        // Optional: Handle specific 500 error
                        if (resources.data?.code() == 500) {
                            Log.e("API_ERROR", "Internal Server Error from backend.")
                        }

                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }

                }
            }
        }

    }



    fun OpenAlertForEmiRequest(customerCode : String){

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

        textmsg.text = "Are you sure you want to proceed with the EMI payment for this customer (${customerCode})?"


        btnCancel.setOnClickListener {
            dialog.dismiss()
        }


        Ok.setOnClickListener {
            if (isInternetAvailable(this@EmiLoanDetailPage)) {
                hitApiForSendOTP(preference.getStringValue(ConstantClass.CustomerMobileNumber,""),"Mobile" /*OTPTYPE*/)
            } else {
                Toast.makeText(this, "Please check your internet connection!!", Toast.LENGTH_SHORT).show()
            }
        }


        dialog.setCanceledOnTouchOutside(false)

        dialog.show()

    }


    fun hitApiForSendOTP(mailidormobile: String, type: String) {
        var sendOtpReq = SendOtpReq(
            mobileoremailId = mailidormobile,
            otpType = type,
            clientCode = preference.getStringValue(ConstantClass.ClientCode, "")
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
                                    if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                                        ConstantClass.dialog.dismiss()
                                    }
                                    dialog.dismiss()
                                    var customerName = "${preference.getStringValue(ConstantClass.FirstName,"")} ${preference.getStringValue(ConstantClass.LastName, "")}"
                                    Log.d("RetailerName", customerName)
                                    hitApiForMobVerify(mailidormobile, customerName, otp)
                                }
                                else{
                                    Toast.makeText(this@EmiLoanDetailPage,response.message,Toast.LENGTH_SHORT).show()
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
        var message = "Dear $customerName, Your OTP for Verification is $OTP. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER"

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
                    Toast.makeText(this@EmiLoanDetailPage, "Otp sent on your mobile number.", Toast.LENGTH_SHORT).show()
                    val loanData = response.body()

                    OpenLoader(mobnumber, OTP)

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


        val pinView=dialog.findViewById<PinView>(R.id.pinview)

        val verifyButton = dialog.findViewById<LinearLayout>(R.id.verifylayout)
        val cancel = dialog.findViewById<ImageView>(R.id.cancel)
        val resendlayout = dialog.findViewById<RelativeLayout>(R.id.resendlayout)
        val resendtxt = dialog.findViewById<TextView>(R.id.resendtxt)
        val timer = dialog.findViewById<TextView>(R.id.timer)
        val title = dialog.findViewById<TextView>(R.id.text_subtitle)

        startOtpTimer(resendtxt, timer)

        title.text = "Please enter the 4-digit OTP sent to your registered mobile number."

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        resendlayout.setOnClickListener {
            if (validateLoginInput(EmailID, this)) {
                if (isInternetAvailable(this@EmiLoanDetailPage)) {
                    hitApiForReSendOTP(EmailID,OTPTYPE )
                    startOtpTimer(resendtxt, timer)
                }
                else {
                    Toast.makeText(this, "Please check your internet connection.", Toast.LENGTH_SHORT).show()
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


    fun startOtpTimer(resendtxt: TextView, timer: TextView) {
        resendtxt.visibility = View.INVISIBLE
        timer.visibility = View.VISIBLE

        // 5 minutes
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


    fun hitApiForReSendOTP(mailidormobile: String, type: String) {
        var sendOtpReq = SendOtpReq(
            mobileoremailId = mailidormobile,
            otpType = type,
            clientCode = preference.getStringValue(ConstantClass.ClientCode, "")
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
                                    var customerName = "${preference.getStringValue(ConstantClass.FirstName,"")} ${preference.getStringValue(ConstantClass.LastName,
                                        "")}"
                                    hitApiForResendMobVerify(mailidormobile, customerName, otp)
                                }
                                else{
                                    Toast.makeText(this@EmiLoanDetailPage,response.message,Toast.LENGTH_SHORT).show()
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
                                    if (dialog != null && dialog.isShowing) {
                                        dialog.dismiss()
                                    }
                                    var selectedNoofEmi = binding.noOfEmi.selectedItem.toString().toInt()

                                    lifecycleScope.launch {
                                        val imageFile = File(" ")
                                        var ForServerlatefine:String? = ""

                                        var remainingBalance = WalletBalance.toDouble()

                                        for (j in 1..selectedNoofEmi) {

                                            val emiIndex = j - 1
                                            val emiAmountWithFine: String

                                            if (listOfDueWithGraceDate[emiIndex].lateFeesApplied) {
                                                if (isEmandateVerified!!.toLowerCase().equals("yes",ignoreCase = true)) {
                                                    emiAmountWithFine = (emiAmount.toDouble() + latefine!!.toDouble() + BounceCharge!!.toDouble() + Othercharges!!.toDouble() + WaiveOff!!.toDouble()).toString()
                                                    BounceChargeApplicable = BounceCharge
                                                }
                                                else {
                                                    emiAmountWithFine = (emiAmount.toDouble() + latefine!!.toDouble() + Othercharges!!.toDouble()).toString()
                                                    BounceChargeApplicable = "0"
                                                }
                                                ForServerlatefine = latefine
                                            }
                                            else {
                                                emiAmountWithFine = emiAmount
                                                ForServerlatefine = "0"
                                                BounceChargeApplicable = "0"
                                            }

                                            val emiAmountWithFineDouble = emiAmountWithFine.toDoubleOrNull() ?: 0.0

                                            if (emiAmountWithFineDouble <= remainingBalance) {
                                                remainingBalance -= emiAmountWithFineDouble
                                                HitApiForRetailerWalletPayoutAmount(selectedNoofEmi, j, emiAmountWithFine, ForServerlatefine, imageFile)
                                            }
                                            else {
                                                Toast.makeText(this@EmiLoanDetailPage, "You don't have sufficient balance for raising EMI.", Toast.LENGTH_SHORT).show()
                                                break
                                            }

                                            delay(1000)

                                        }

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
                    Toast.makeText(this@EmiLoanDetailPage, "Otp sent on your mobile number!!", Toast.LENGTH_SHORT).show()
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



}