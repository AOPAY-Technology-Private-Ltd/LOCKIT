package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bumptech.glide.Glide
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityEmicalculationDetailsPageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.BrandName
import com.bosandroidapp.aopaykit.constant.ConstantClass.ClickOnCardDashboard
import com.bosandroidapp.aopaykit.constant.ConstantClass.DownPayment
import com.bosandroidapp.aopaykit.constant.ConstantClass.EmiAmount
import com.bosandroidapp.aopaykit.constant.ConstantClass.InterestAmt
import com.bosandroidapp.aopaykit.constant.ConstantClass.InterestRate
import com.bosandroidapp.aopaykit.constant.ConstantClass.LoanSecurityHoldAmount
import com.bosandroidapp.aopaykit.constant.ConstantClass.MRPPrice
import com.bosandroidapp.aopaykit.constant.ConstantClass.ModelColor
import com.bosandroidapp.aopaykit.constant.ConstantClass.ModelName
import com.bosandroidapp.aopaykit.constant.ConstantClass.ModelVarient
import com.bosandroidapp.aopaykit.constant.ConstantClass.ProcessingFees
import com.bosandroidapp.aopaykit.constant.ConstantClass.SellingPrice
import com.bosandroidapp.aopaykit.constant.ConstantClass.Tenure
import com.bosandroidapp.aopaykit.constant.ConstantClass.ToBePaidAmount
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.DataItem
import com.bosandroidapp.aopaykit.data.model.loginsignup.DataItems
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetEMISplitDetlailsReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.slideshow.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.PaymentInformation.Companion.checkKYC
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson

class EMICalculationDetailsPage : AppCompatActivity() {
    lateinit var binding: ActivityEmicalculationDetailsPageBinding
    lateinit var viewModel: AuthenticationViewModel
    private var sellingPriceHandler = Handler(Looper.getMainLooper())
    private var sellingPriceRunnable: Runnable? = null
    lateinit var dialog: Dialog
    lateinit var preference : SharedPreference
    lateinit var avlbColors : ArrayList<String>
    lateinit var variantList : List<String>

    private var emiRetryCount = 0
    private val MAX_RETRY_COUNT = 1

    private var isApiRunning = false

    var isUserTyping = false

    var isclick = true
    var sellingPriceValidate = true
    var lastSellingPrice: String? = null

    companion object {
        var EmiSplitDataModel: MutableList<DataItems> = mutableListOf()
        var FilterDataEmiSplitDataModel: MutableList<DataItems> = mutableListOf()
        lateinit var MobileData: DataItem
    }


    override fun onStart() {
        super.onStart()
        preference = SharedPreference(this)
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

        if (!MobileData!!.avlbColors.isNullOrBlank()) {

            val colorList = MobileData!!.avlbColors

            Log.d("ColorList", Gson().toJson(colorList))

            var color = colorList.split(",").map { it.trim() }
        }

        if (EmiSplitDataModel.isEmpty()) {
            hitApiForGetEmiPercent(MobileData!!.brandName, MobileData!!.modelName)
        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmicalculationDetailsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        preference = SharedPreference(this)
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]


        if (ClickOnCardDashboard.equals("Customer")) {
            binding.nextbuttonlayout.visibility = View.VISIBLE
        }

        if (ClickOnCardDashboard.equals("Product")) {
            binding.nextbuttonlayout.visibility = View.GONE
        }

        hitApiForGetEmiPercent(MobileData.brandName, MobileData.modelName)

        setonClickListner()

    }


    override fun onResume() {
        super.onResume()
        hitApiForLogin()
    }


    fun hitApiForGetEmiPercent(brandName: String, modelName: String) {
        if (isApiRunning) return
        isApiRunning = true
        var emisplitReq = GetEMISplitDetlailsReq(
            brandName = brandName,
            modelName = modelName
        )
        Log.d("EmiPercentReq", Gson().toJson(emisplitReq))

        BrandName = brandName
        ModelName = modelName

        viewModel.getSplitEmiDetails(emisplitReq).observe(this) { resources ->

            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->

                                Log.d("EmiPercentRes", Gson().toJson(response.data))
                                isApiRunning = false
                                if (response.status.equals("True", true) && !response.data.isNullOrEmpty()){
                                    emiRetryCount = 0
                                    if (ConstantClass.dialog.isShowing) {
                                        ConstantClass.dialog.dismiss()
                                    }
                                    EmiSplitDataModel = response.data

                                    variantList = EmiSplitDataModel
                                        .map { it.variantName.trim() }
                                        .distinct()

                                    val variantAdapter = ArrayAdapter(this, R.layout.mobilenamelayout, variantList)
                                    variantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                    binding.stroage.adapter = variantAdapter


                                }else{
                                    if (emiRetryCount < MAX_RETRY_COUNT){
                                        emiRetryCount++
                                        Log.d("EMI_API", "Retrying API call : $emiRetryCount")
                                        Handler(Looper.getMainLooper()).postDelayed({

                                            hitApiForGetEmiPercent(
                                                brandName,
                                                modelName
                                            )

                                        }, 2000)
                                    }
                                    else{
                                        emiRetryCount = 0

                                        if (ConstantClass.dialog.isShowing) {
                                            ConstantClass.dialog.dismiss()
                                        }

                                        Toast.makeText(this, response.message ?: "No data found", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                /* if (response.status.equals("True")) {
                                     ConstantClass.dialog.dismiss()
                                     var EmiDataList = response.data
                                     if (EmiDataList!!.size > 0) {
                                         EmiSplitDataModel = EmiDataList
                                         variantList = EmiSplitDataModel.map { it.variantName.trim() }.distinct()
                                         val variantAdapter = ArrayAdapter(this, R.layout.mobilenamelayout, variantList)
                                         variantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                         binding.stroage.adapter = variantAdapter

                                     }
                                 }
                                 else {
                                     ConstantClass.dialog.dismiss()
                                     Toast.makeText(this@EMICalculationDetailsPage, response.message, Toast.LENGTH_SHORT).show()
                                 }*/
                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                        isApiRunning = false
                        if (emiRetryCount < MAX_RETRY_COUNT) {
                            emiRetryCount++
                            Log.d("EMI_API", "Retrying after error : $emiRetryCount")
                            Handler(Looper.getMainLooper()).postDelayed({

                                hitApiForGetEmiPercent(
                                    brandName,
                                    modelName
                                )

                            }, 2000)
                        }
                        else {

                            emiRetryCount = 0

                            if (ConstantClass.dialog.isShowing) {
                                ConstantClass.dialog.dismiss()
                            }

                            Toast.makeText(this, "Unable to load EMI details. Please try again.", Toast.LENGTH_SHORT).show()
                        }

                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }

                }

            }

        }

    }


    fun setDataOnUI(SellingPrice: String,mrp:String) {
        Glide.with(this).load(MobileData.imagePath).placeholder(R.drawable.samsung).error(R.drawable.samsung).into(binding.deviceImage)
        binding.brandname.text = MobileData.brandName
        binding.designtype.text = MobileData.modelName
        binding.editiontxt.text = "Android OS  : ${MobileData.remark}"
       // binding.colorvarient.text = MobileData.avlbColors
       // binding.stroage.text = MobileData.variantName
       // binding.mrpamount.text = "₹ ".plus(MobileData.mrpPrice)
        binding.mrpamount.text = "₹  $mrp"

        // binding.sellingamount.text = "₹ ".plus(EmiSplitDataModel[0].sellingPrice)
        binding.sellingamount.setText(SellingPrice)
        Log.d("selling" ,"$SellingPrice")

        if (ClickOnCardDashboard.equals("Customer")) {
            binding.nextbuttonlayout.visibility = View.VISIBLE
        }
        if (ClickOnCardDashboard.equals("Product")) {
            binding.nextbuttonlayout.visibility = View.GONE
        }



    }

    fun setonClickListner() {

        binding.synchicon.setOnClickListener {
            isApiRunning = false
            EmiSplitDataModel.clear()
            FilterDataEmiSplitDataModel.clear()
            hitApiForGetEmiPercent(MobileData!!.brandName, MobileData!!.modelName)
        }

        binding.resetdata.setOnClickListener {
            isApiRunning = false
            EmiSplitDataModel.clear()
            FilterDataEmiSplitDataModel.clear()
            hitApiForGetEmiPercent(MobileData!!.brandName, MobileData!!.modelName)
        }

        binding.stroage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val selectedVariant = variantList[position]

                // find all entries for this variant
                val selectedVariantData = EmiSplitDataModel.filter { it.variantName.trim() == selectedVariant } as MutableList<DataItems>
                FilterDataEmiSplitDataModel= selectedVariantData

                // collect all colors and split comma-separated lists
                val colorList = selectedVariantData.flatMap { it.avlbColors.split(",").map { color -> color.trim() } }.distinct()

                // set to color spinner
                val colorAdapter = ArrayAdapter(this@EMICalculationDetailsPage, R.layout.mobilenamelayout, colorList)
                colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.colorvarient.adapter = colorAdapter


                Log.d("filterdata", "$selectedVariantData")

                if (selectedVariantData.isNotEmpty()) {
                    val data = selectedVariantData[0]  // or .first()
                    // 🪙 Show processing fee
                    binding.processingfees.text = "₹ ${data.processingFees}"
                    // 💰 Get selling price
                    SellingPrice = data.sellingPrice
                    lastSellingPrice = SellingPrice
                    // 🔢 Perform your EMI calculation
                    UpdateEmiCalculationOnSellingPrice(selectedVariantData, lastSellingPrice!!,data.tenure,data.downPaymentPerc)
                } else {
                    Log.e("Variant", "No data found for variant: $selectedVariant")
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        binding.colorvarient.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedColor = binding.colorvarient.selectedItem.toString()
                ModelColor = selectedColor
                Log.d("Color",selectedColor)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

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



        binding.sellingamount.isCursorVisible = true
        binding.sellingamount.requestFocus()

        binding.sellingamount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isclick) {
                    isclick = false
                    return
                }

                val enteredPriceStr = s.toString().trim()

                if (enteredPriceStr.isEmpty()) {
                    val minSellingPriceStr = FilterDataEmiSplitDataModel[0].sellingPrice
                    isclick = true // prevent re-trigger
                    binding.sellingamount.setText(minSellingPriceStr)
                    binding.sellingamount.setSelection(minSellingPriceStr.length) // keep cursor blinking at end
                    return
                }

                val enteredPrice = enteredPriceStr.toDoubleOrNull() ?: return
                val minSellingPrice = FilterDataEmiSplitDataModel[0].sellingPrice.toDoubleOrNull() ?: 0.0
                val mrpPrice = binding.mrpamount.text.toString().replace("₹", "").trim().toDoubleOrNull() ?: 0.0
                sellingPriceValidate = true

                when {
                    enteredPrice < minSellingPrice -> {
                        sellingPriceValidate=false
                        binding.sellingamount.error = "Selling Price cannot be less than ₹$minSellingPrice"
                        return
                    }

                    enteredPrice > mrpPrice -> {
                        sellingPriceValidate=false
                        binding.sellingamount.error = "Selling Price cannot be more than MRP ₹$mrpPrice"
                        return
                    }

                    else -> {
                        // ✅ Call API only if value changed from last call
                        if (enteredPriceStr != lastSellingPrice) {
                            lastSellingPrice = enteredPriceStr
                            if (FilterDataEmiSplitDataModel.isNotEmpty()) {
                                UpdateEmiCalculationOnSellingPrice(FilterDataEmiSplitDataModel,  lastSellingPrice !!,binding.tenureSlider.value.toInt().toString(),binding.downPaymentSlider.value.toInt().toString())
                            }
                        }
                        // ✅ Keep cursor at last index
                        binding.sellingamount.setSelection(s?.length ?: 0)
                    }
                }
            }
        })


        binding.downPaymentSlider.addOnChangeListener { _, value, _ ->
            val percent = value.toInt()
            binding.txtDownPercent.text = "${percent}%"
            binding.sellingamount.setText(lastSellingPrice)
            if (FilterDataEmiSplitDataModel.isNotEmpty()) {
                UpdateEmiCalculationOnSellingPrice(FilterDataEmiSplitDataModel, lastSellingPrice!!,binding.tenureSlider.value.toInt().toString(),percent.toString())
            }
        }


        binding.tenureSlider.addOnChangeListener { _, value, _ ->
            binding.tenuretxt.text = "${value.toInt()} Months"
            binding.sellingamount.setText(lastSellingPrice)
            if (FilterDataEmiSplitDataModel.isNotEmpty()) {
                UpdateEmiCalculationOnSellingPrice(FilterDataEmiSplitDataModel, lastSellingPrice!!, value.toInt().toString(),binding.downPaymentSlider.value.toInt().toString())
            }
        }


        binding.nextbuttonlayout.setOnClickListener {

            val loanAmount = ConstantClass.LoanAmount ?: 0.0
            val maxHoldAmount = LoanSecurityHoldAmount.toDoubleOrNull() ?: 0.0

            if (loanAmount < maxHoldAmount) {
                Toast.makeText(this@EMICalculationDetailsPage, "Your loan amount is below to the hold amount. Please contact Admin.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!ToBePaidAmount.isNullOrBlank()&& sellingPriceValidate) {
                checkKYC = false
                startActivity(Intent(this@EMICalculationDetailsPage, com.bosandroidapp.aopaykit.ui.view.activity.retailer.PaymentInformation::class.java))
            }

        }

    }


    /* fun UpdateEmiCalculationOnSellingPrice(EmiDataList: List<DataItems>, SellingPrice: String) {

        binding.tenuretxt.text = EmiDataList[0].tenure.plus(" Months")

        val sellingPrice = SellingPrice.toDouble()
        val downPaymentPerc = EmiDataList[0].downPaymentPerc.toDouble() // or .toFloat()
        val annualInterestRate = EmiDataList[0].interestPerc
        val tenureMonths = EmiDataList[0].tenure.toInt()
        val processingFee = EmiDataList[0].processingFees.toDouble()


        // Step 1: Calculate down payment
        val downPayment = (sellingPrice * downPaymentPerc) / 100
        binding.downPayment.text = "₹ ".plus("%.2f".format(downPayment))

        // Step 2: Calculate principal
        val principalAmount = sellingPrice.toDouble() - downPayment

        // Step 3: Calculate monthly interest rate
        val monthlyInterestRate = annualInterestRate.toDouble() / 12 / 100

        println("Monthly Interest Rate = $monthlyInterestRate")

        //MonthlyInterestAmount = Principal×MonthlyInterestRate principal amount is loan amount
        var MonthlyInterestAmount = principalAmount * monthlyInterestRate


        binding.monthlyinterest.text = "₹ ".plus("%.2f".format(MonthlyInterestAmount))

        Log.d("monthinterest","$MonthlyInterestAmount" )

        // Step 4: EMI Calculation
        val tenureYears = tenureMonths / 12.0
        val totalInterest = principalAmount.toDouble() * annualInterestRate.toDouble() * tenureYears / 100

        val emi = (principalAmount.toDouble() + totalInterest) / tenureMonths

        binding.emitxt.text = "₹ ".plus("%.2f".format(emi))

        val toBePaidNow = downPayment + processingFee
        binding.tobepaynowamt.text = "₹ ".plus("%.2f".format(toBePaidNow))


        binding.totalAmt.text = "₹ ".plus("%.2f".format(principalAmount))

        DownPayment = downPayment.toString()
        Tenure = tenureMonths.toString()
        EmiAmount = emi.toString()
        ConstantClass.LoanAmount = principalAmount
        InterestRate = annualInterestRate
        ToBePaidAmount = "%.2f".format(toBePaidNow)
        ProcessingFees =  "%.2f".format(processingFee)
        InterestAmt = "$totalInterest".trim()
        MRPPrice = EmiDataList[0].mrpPrice
        ModelVarient = EmiDataList[0].variantName
        Log.d("totalinterest","$totalInterest" )


        setDataOnUI(SellingPrice,EmiDataList[0].mrpPrice)

    }*/


    // as discuss by Naim Sir 03/06/2026


    fun UpdateEmiCalculationOnSellingPrice(EmiDataList: List<DataItems>, SellingPrice: String, tenureMonth: String,downPaymentPercentange: String) {

        binding.tenuretxt.text= "${tenureMonth} Month"
        binding.txtDownPercent.text = "${downPaymentPercentange}%"
        binding.downPaymentSlider.value= downPaymentPercentange.toFloat()
        binding.tenureSlider.value = tenureMonth.toFloat()

        val sellingPrice = SellingPrice.toDouble()
       // val downPaymentPerc = EmiDataList[0].downPaymentPerc.toDouble()
        val downPaymentPerc = downPaymentPercentange.toDouble()
        val totalInterestPerc = EmiDataList[0].interestPerc.toDouble() // Total interest %
        //val tenureMonths = EmiDataList[0].tenure.toInt()
        val tenureMonths = tenureMonth.toInt()
        val processingFee = EmiDataList[0].processingFees.toDouble()

        // Step 1: Down Payment
        val downPayment = (sellingPrice * downPaymentPerc) / 100
        binding.downPayment.text = "₹ %.2f".format(downPayment)

       // binding.downpaymenttxt.text = "${resources.getString(R.string.down_payment)}(${EmiDataList[0].downPaymentPerc})%"
        binding.downpaymenttxt.text = "${resources.getString(R.string.down_payment)}(${downPaymentPercentange})%"

        // Step 2: Loan Amount (Principal)
        val principalAmount = sellingPrice - downPayment

        // Step 3: Monthly Interest Rate
        // Example: 20% interest and 5 months tenure = 4% per month
        val monthlyInterestRate = (totalInterestPerc / tenureMonths) / 100

        Log.d("MonthlyInterestRate", monthlyInterestRate.toString())

        // Monthly Interest Amount
        val monthlyInterestAmount = principalAmount * monthlyInterestRate

        binding.monthlyinterest.text = "₹ %.2f".format(monthlyInterestAmount)

        // Step 4: Total Interest
        val totalInterest = monthlyInterestAmount * tenureMonths

        // Step 5: Total Payable Amount
        val totalPayableAmount = principalAmount + totalInterest

        // Step 6: EMI
        val emi = totalPayableAmount / tenureMonths

        Log.d("EMI", emi.toString())

        binding.emitxt.text = "₹ %.2f".format(emi)

        // Step 7: To Be Paid Now
        val toBePaidNow = downPayment + processingFee
        binding.tobepaynowamt.text = "₹ %.2f".format(toBePaidNow)

        // Step 8: Loan Amount
        binding.totalAmt.text = "₹ %.2f".format(principalAmount)

        binding.emitxt.text = "₹ ".plus("%.2f".format(emi))

        binding.totalAmt.text = "₹ ".plus("%.2f".format(principalAmount))

        DownPayment = downPayment.toString()
        Tenure = tenureMonths.toString()
        EmiAmount = emi.toString()
        ConstantClass.LoanAmount = principalAmount
        InterestRate =  "%.2f".format(totalInterestPerc)
        ToBePaidAmount = "%.2f".format(toBePaidNow)
        ProcessingFees = "%.2f".format(processingFee)
        InterestAmt = "$totalInterest".trim()
        MRPPrice = EmiDataList[0].mrpPrice
        ModelVarient = EmiDataList[0].variantName
        Log.d("totalinterest", "$totalInterest")

        setDataOnUI(SellingPrice, EmiDataList[0].mrpPrice)

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
                                ConstantClass.checkActiveStatusAndLogout(this@EMICalculationDetailsPage, response.status, preference)
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
                                val intent = Intent(this@EMICalculationDetailsPage, ChooseYourRolePage::class.java)
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