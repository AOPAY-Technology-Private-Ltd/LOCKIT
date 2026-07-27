package com.bosandroidapp.aopaykit.kioskmode

import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityKioskBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.formatDateToDDMMYYYY
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.constant.ConstantClass.isLockTaskStarted
import com.bosandroidapp.aopaykit.constant.ConstantClass.isPgClosing
import com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerDataItem
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.RetailerProfileReq
import com.bosandroidapp.aopaykit.data.pg.PGRequestCall
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.repository.PanRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.data.viewModelFactory.PanViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.customer.EmiLoanDetailPage.EmiData
import com.bosandroidapp.aopaykit.ui.view.activity.customer.PGWebViewActivity
import com.bosandroidapp.aopaykit.ui.view.activity.customer.PGWebViewActivity.Companion.emiList
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.ui.viewmodel.PanViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.bosandroidapp.aopaykit.utils.MonthsAndPayables
import com.bosandroidapp.aopaykit.utils.getCurrentLastPaidDueDate
import com.google.gson.Gson
import kotlinx.coroutines.launch

class KioskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKioskBinding
    lateinit var preference: SharedPreference
    lateinit var viewModel: AuthenticationViewModel
    var retailerCode : String = ""
    var  MobileNumber : String= ""
    var  EmailId : String= ""
    var  FName : String= ""
    var  LName : String= ""
    var latefine : String? = ""

    var AllgracePeriod : String? = ""
    var CustomergracePeriod : String? = ""
    var BounceCharge : String = ""
    var Othercharges : String = ""
    var WaiveOff : String = ""
    var loanCode: String = ""
    var emiAmount: String = ""
    var isEmandateVerified : String? = ""
    var isPannydropVerified : String? = ""
    var emiamount : Double = 0.0
    var selectedNoofEmi: Int = 0
    var listOfDueWithGraceDate : ArrayList<MonthsAndPayables> = arrayListOf()
    var BounceChargeApplicable : String = ""

    lateinit var panViewModel: PanViewModel

    private var previousLoanData: List<CustomerDataItem?>? = null
    private var previousCurrentDate: String = ""
    private var LoanEmiList: List<CustomerDataItem?>? = null

    private var isApiRunning = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.exitTransition = null
        window.returnTransition = null
        window.reenterTransition = null
        binding = ActivityKioskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference= SharedPreference(this)
        retailerCode = preference.getStringValue(ConstantClass.RetailerCode,"")
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        panViewModel = ViewModelProvider(this, PanViewModelFactory(PanRepository(RetrofitClient.apiInterfacePAN)))[PanViewModel::class.java]

        hitapiforGetUpdateProfile()
        HitApiForEmiList()

        val apps = findViewById<RecyclerView>(R.id.paymentApps)
        apps.layoutManager = GridLayoutManager(this, 3)
        apps.visibility=View.GONE

        Log.d("Check","Kiosk")

        if (isLocked()) {
            apps.visibility=View.GONE
            apps.fillApps(this,getPaymentApps())
        }
        else {
            apps.visibility=View.GONE
        }


        setOnClickListner()

    }

    override fun onResume() {
        super.onResume()

        if (!previousLoanData.isNullOrEmpty()) {
            setData(previousLoanData, previousCurrentDate)

        } else if (!isApiRunning) {

            HitApiForEmiList()
        }

        hitapiforGetUpdateProfile()

        // for testing transferOwnerShip.....................................................
        val dpm = getSystemService(DevicePolicyManager::class.java)
        val admin = ComponentName(this, KioskDeviceAdminReceiver::class.java)
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        if (dpm.isDeviceOwnerApp(packageName)) {

            // Whitelist the app for Lock Task Mode (typically done once during provisioning)
            dpm.setLockTaskPackages(admin, arrayOf(packageName))

            // Start Lock Task only if not already active
            if (!isLockTaskStarted &&activityManager.lockTaskModeState == ActivityManager.LOCK_TASK_MODE_NONE) {
                startLockTask()
                isLockTaskStarted = true
            }
        }

        isPgClosing = false
    }


    fun setOnClickListner(){

        binding.submitpayment.setOnClickListener {

            emiamount = binding.amount.text.toString().replace("₹ ","").toDouble()

                 if(isInternetAvailable(this@KioskActivity)) {
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
                            emiList.add(EmiData(selectedNoofEmi,emiNo = j, emiAmount = emiAmountWithFine, lateFine = ForServerlatefine!!,BounceChargeApplicable,loanCode)
                            )
                        }

                        val email = preference.getStringValue(ConstantClass.CustomerEmailID, "") .ifEmpty { "bos.centerpvtltd@gmail.com" }
                        val emiNumbers=  (1..selectedNoofEmi).joinToString("")

                        PGWebViewActivity.LoanCodePG = loanCode

                        var req = PGRequestCall(
                            payCustomerPhoneNo = preference.getStringValue(ConstantClass.CustomerMobileNumber, ""),
                            customerEmailID = email,
                            registrationID = ConstantClass.PAN_VERIFICATION_REGISTRATION_ID,
                            payCartAmount = emiamount.toString(),
                            eMINumbers = "EMI${emiNumbers}",
                            customerCode = preference.getStringValue(ConstantClass.CustomerCode, ""),
                            payCustomerName = "${preference.getStringValue(ConstantClass.FirstName, "")} ${preference.getStringValue(ConstantClass.LastName, "")}",
                            loanCode = loanCode
                        )

                        hitApiForRequestPG(req)


                    }
                 }
                else{
                     Toast.makeText(this@KioskActivity,"Please connect with internet", Toast.LENGTH_SHORT).show()
                 }
        }
    }

    override fun onPause() {
        super.onPause()
        if(isLocked()) finish()
    }

    override fun onStop() {
        super.onStop()
        Log.d("Accessibility","onStop")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) enterImmersiveMode()
    }

    fun hitapiforGetUpdateProfile(){

        var req = RetailerProfileReq(
            mode="GET",
            customerType="Retailer",
            customerCode=retailerCode,
            firstName="",
            lastName="",
            mobileNo="",
            emailid="",
            address="",
            aadharNumber="",
            panNumber="",
            activeStatus=""
        )

        Log.d("retailergetprofileReq", Gson().toJson(req))

        viewModel.getRetailerProfileReq(req).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->

                                if (response!!.statuss.equals("True")) {
                                    Log.d("retailerDetailsResponse", Gson().toJson(response))
                                    ConstantClass.dialog.dismiss()
                                    EmailId= response.emailid.toString()
                                    MobileNumber= response.mobileNo.toString()
                                    FName= response.firstName.toString()
                                    LName= response.lastName.toString()
                                    binding.infomasg.text = "This device is locked due to a pending payment. " + "To unlock, please contact your retailer ${FName} ${LName} at $MobileNumber or email at $EmailId"
                                }
                                else {

                                    finish()
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

    fun HitApiForEmiList(){
        if (isApiRunning) {
            Log.d("EMI_API", "Already running")
            return
        }

        isApiRunning = true
        var loanemireq = GetCustomerLoanDetailsReq(
            loancode = "",
            customercode = preference.getStringValue(ConstantClass.CustomerCode,""))
        Log.d("customerloanEmireq", Gson().toJson(loanemireq))

        viewModel.getCustomerLoanEmiDetailsReq(loanemireq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("customerLoanemiresp", response.toString())

                                isApiRunning = false

                                resources.data?.body()?.let { response ->
                                    ConstantClass.dialog.dismiss()
                                    LoanEmiList = response.data
                                    previousLoanData = response.data
                                    previousCurrentDate = response.indiaTimeIST ?: ""

                                    setData(response.data, response.indiaTimeIST ?: "")
                                }
                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                            ConstantClass.dialog.dismiss()
                        }
                        HitApiForEmiList()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }

                }
            }
        }
    }


    fun setData(LoanEmiList:List<CustomerDataItem?>? = null,  currentDate : String){
        latefine = LoanEmiList?.get(0)!!.lateFine
        AllgracePeriod = LoanEmiList!![0]!!.gracePeriod
        CustomergracePeriod = LoanEmiList!![0]!!.customerGracePeriod


        BounceCharge = String.format("%.2f", LoanEmiList?.get(0)?.applicableBounceCharge?.toDoubleOrNull() ?: 0.0)
        Othercharges = String.format("%.2f", LoanEmiList?.get(0)?.applicableOtherCharge?.toDoubleOrNull() ?: 0.0)
        WaiveOff = String.format("%.2f", LoanEmiList?.get(0)?.applicableWaiveOff?.toDoubleOrNull() ?: 0.0)


        loanCode = LoanEmiList!![0]!!.loanCode.toString()
        emiAmount = LoanEmiList!![0]!!.emiAmount.toString()

        isEmandateVerified = LoanEmiList!![0]!!.isEmandateVerified.toString()
        isPannydropVerified = LoanEmiList!![0]!!.isPannydropVerified.toString()

        val gracePeriod = AllgracePeriod!!.toInt()+CustomergracePeriod!!.toInt()


        lifecycleScope.launch {
            listOfDueWithGraceDate = formatDateToDDMMYYYY(LoanEmiList!![0]!!.startDate.toString()).getCurrentLastPaidDueDate(this@KioskActivity,LoanEmiList!![0]!!.paidEMI!!.toLong(),LoanEmiList!![0]!!.duesEMI!!.toLong(), AllgracePeriod!!.toInt(),CustomergracePeriod!!.toInt(),currentDate!!)
            Log.d("DueList", "Data". plus(listOfDueWithGraceDate))
            setDataInspinner(LoanEmiList!![0]!!.duesEMI!!.toInt(),LoanEmiList?.get(0)?.emiAmount?.toDoubleOrNull() ?: 0.0)
        }
    }
    fun setDataInspinner(tenure:Int,emiAmount:Double?){
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

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional
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
                                    isLockTaskStarted = false
                                    stopLockTask()
                                    val intent = Intent(this@KioskActivity, PGWebViewActivity::class.java)
                                    intent.putExtra("pgurl", response.preparePOSTForm)
                                    startActivity(intent)
                                }
                                else {
                                    ConstantClass.dialog.dismiss()
                                    Toast.makeText(this@KioskActivity, response.message, Toast.LENGTH_SHORT).show()
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