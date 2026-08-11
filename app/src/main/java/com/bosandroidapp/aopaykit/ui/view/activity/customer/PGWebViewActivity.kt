package com.bosandroidapp.aopaykit.ui.view.activity.customer

import android.R
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.getCurrentUtcTimestamp
import com.bosandroidapp.aopaykit.constant.ConstantClass.isPgClosing
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPlanListDataItem
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPurchaseHistoryRequest
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPurchasePlanSaveRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerLoanEmiReceiveReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityPgwebViewBinding
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus

import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PGWebViewActivity : BaseActivity() {
    lateinit var binding : ActivityPgwebViewBinding
    lateinit var dialog: Dialog
    lateinit var preference : SharedPreference
    lateinit var viewModel: AuthenticationViewModel
    lateinit var Activityname: String



    companion object{
        var emiList = mutableListOf<EmiLoanDetailPage.EmiData>()
        var EMIamountPG : String =""
        var LoanCodePG : String = ""

        lateinit var kitPlanListDataItem : KitPlanListDataItem
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        super.onCreate(savedInstanceState)

        binding = ActivityPgwebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBarsInsets.left,
                systemBarsInsets.top,    // Top padding
                systemBarsInsets.right,
                systemBarsInsets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }

        preference = SharedPreference(this)
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface))
        )[AuthenticationViewModel::class.java]

        clearWebViewData(binding.pgwebview)

        if(intent.hasExtra("kittopup")){
            Activityname= intent.getStringExtra("kittopup").toString()
        }

        launchPGOnWebView()
    }

    fun launchPGOnWebView(){
        val pgUrl = intent.getStringExtra("pgurl")

        val finalHtml = """
    <html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    </head>
    <body>
        $pgUrl
    </body>
    </html>
""".trimIndent()
        binding.pgwebview.settings.javaScriptEnabled = true
        binding.pgwebview.settings.domStorageEnabled = true
        binding.pgwebview.webViewClient = object : WebViewClient() {

            private var isSuccessPage = false

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                Log.d("URL", url.orEmpty())

                if (url.isNullOrEmpty()) {
                    return true
                }

                return if (url.startsWith("http://") || url.startsWith("https://")) {

                    // Handle PayU response URLs
                    when {
                        // Payment Success
                        url.contains("status=success", ignoreCase = true) || url.contains("/success", ignoreCase = true) -> {

                            Handler(Looper.getMainLooper()).postDelayed({
                                val uri = Uri.parse(url)
                                val utrNumber = uri.getQueryParameter("utrNumber")
                                val paymentMode = uri.getQueryParameter("PaymentMode") // handles different casing
                                val transactionNo = uri.getQueryParameter("txnid")

                                Log.d("TAG", "UTR: $utrNumber")
                                Log.d("TAG", "Payment Mode: $paymentMode")
                                Log.d("TAG", "Transaction No: $transactionNo")

                                Log.d("UTR", utrNumber ?: "")
                                showingSuccessPopUp(utrNumber!!,paymentMode!!,transactionNo!!)

                            }, 1000)

                            return true

                           /* isSuccessPage = true
                              return false*/
                           // Let WebView load the success page

                        }

                        // Payment Failed
                        url.contains("status=failure", ignoreCase = true) || url.contains("/failure", ignoreCase = true) -> {
                            Log.d("PAYU", "Payment Failed : $url")
                            showingRejectionePGPopUp()
                            return true
                        }

                        // Payment Cancelled
                        url.contains("/cancel", ignoreCase = true) || url.contains("status=cancel", ignoreCase = true) || url.contains("action=userCancel", ignoreCase = true) -> {
                            Log.d("PAYU", "Payment Cancelled : $url")
                            showingRejectionePGPopUp()
                            return true
                        }

                    }

                    false // Let WebView load the URL itself

                }
                else {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

                        val activities = packageManager.queryIntentActivities(intent, 0)
                        Log.d("UPI", "Activities Count: ${activities.size}")

                        if (activities.isNotEmpty()) {
                            startActivity(Intent.createChooser(intent, "Pay with"))
                        } else {
                            Toast.makeText(this@PGWebViewActivity, "No UPI app found", Toast.LENGTH_SHORT).show()
                        }

                    }
                    catch (e: Exception) {
                        Log.e("UPI", "Error launching app", e)
                        Toast.makeText(this@PGWebViewActivity, "No app found to handle this action", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
            }

          /*  override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("URL", url.orEmpty())

                if (isSuccessPage) {
                    isSuccessPage = false
                    showingSuccessPopUp()
                }
            }*/

        }

        binding.pgwebview.loadDataWithBaseURL("https://secure.payu.in/", finalHtml, "text/html", "UTF-8", null)

        binding.pgwebview.loadUrl(pgUrl!!)

    }


    fun HitApiForPayEmiAmount(emicount:Int,loopcount :Int,emiamount : String,fine:String?/*,imageFile:File*/,loanCode:String,dialog: Dialog,utrNumber: String){

        var  createdBy = preference.getStringValue(ConstantClass.CustomerCode, "")
        var customercode =  preference.getStringValue(ConstantClass.CustomerCode, "")
        var retailercode =  preference.getStringValue(ConstantClass.RetailerCode, "")

        val request = CustomerLoanEmiReceiveReq(
            mode = "UPDATE",
            loanCode = loanCode,
            paymentDate = getCurrentUtcTimestamp(),
            paymentMode = "Online",
            utrNumber = utrNumber,
            remarks = "Payment",
            createdBy = createdBy,
            receiptNo = "",
            customerCode =customercode,
            retailerCode = retailercode,
            bankName = "PG",
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
                                     emiList .clear()
                                     EMIamountPG  =""
                                     LoanCodePG  = ""
                                    Toast.makeText(this@PGWebViewActivity,response.message,Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@PGWebViewActivity, DashBoard::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)

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


    fun clearWebViewData(webView: WebView) {
        webView.clearCache(true)
        webView.clearHistory()
        webView.clearFormData()

        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
        WebStorage.getInstance().deleteAllData()
    }


    fun showingRejectionePGPopUp(){
        dialog = Dialog(this, R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.bosandroidapp.aopaykit.R.layout.payment_reject_alert)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }

        var Ok = dialog.findViewById<AppCompatButton>(com.bosandroidapp.aopaykit.R.id.btnOk)

        /*Ok.setOnClickListener {
            finish()
            dialog.dismiss()
        }*/

        Ok.setOnClickListener {
            isPgClosing = true
            dialog.dismiss()
            closePg()
            window.decorView.post {
                finish()
            }
        }

        dialog.setCanceledOnTouchOutside(false)

        dialog.show()

    }

    private fun closePg() {
        binding.pgwebview.stopLoading()
        binding.pgwebview.loadUrl("about:blank")
        binding.pgwebview.clearHistory()
        binding.pgwebview.removeAllViews()
        binding.pgwebview.destroy()
    }

    fun showingSuccessPopUp(utrNumber: String,paymentMode:String,transactionNo:String){
        dialog = Dialog(this, R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.bosandroidapp.aopaykit.R.layout.payment_success_alert)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }

        var Ok = dialog.findViewById<AppCompatButton>(com.bosandroidapp.aopaykit.R.id.btnOk)
        var textmessage = dialog.findViewById<TextView>(com.bosandroidapp.aopaykit.R.id.loancodewithamount)
        var tvTitle = dialog.findViewById<TextView>(com.bosandroidapp.aopaykit.R.id.tvTitle)

        if(ConstantClass.KitPlan==Activityname){
            val message = "Kit purchase successful. ${kitPlanListDataItem.noOfKits.toString()} kits have been purchased successfully and will be added to your available kit inventory."
            textmessage.text = message
            tvTitle.text = "Kit Purchase Successful"
        }
        else{
            val message = "Your EMI payment of ${EMIamountPG} for Loan Code ${LoanCodePG} has been successfully processed."
            textmessage.text = message
            tvTitle.text = "EMI Payment Successful"
        }


        Ok.setOnClickListener {
            if(ConstantClass.KitPlan==Activityname){
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                val currentDateTime = LocalDateTime.now().format(formatter)
                val endDateTime = LocalDateTime.now()
                    .plusYears(1)
                    .format(formatter)

                val purchaseCode = transactionNo.substringAfterLast("_")

                var request = KitPurchasePlanSaveRequest(
                    companyCode= ConstantClass.ClientCode ,
                    gstAmount= kitPlanListDataItem.gstAmount,
                    purchaseCode= purchaseCode,
                    purchaseDate = currentDateTime,
                    netAmount= kitPlanListDataItem.totalAmount ,
                    paymentMode= paymentMode ,
                    transactionNo= transactionNo,
                    discountAmount= kitPlanListDataItem.discountAmount ,
                    paymentReferenceNo= utrNumber,
                    mappingCode= kitPlanListDataItem.mappingCode,
                    isActive= true ,
                    planCode= kitPlanListDataItem.planCode,
                    createdBy= "Retailer" ,
                    retailerCode= preference.getStringValue(ConstantClass.RetailerCode,""),
                    planStartDate= currentDateTime ,
                    invoiceNo= "",
                    planEndDate= endDateTime,
                    planAmount= kitPlanListDataItem.planAmount,
                    paymentStatus= "SUCCESS",
                    remarks= "Plan purchased successfully",
                    noOfKits = kitPlanListDataItem.noOfKits.toString()
                )
                Log.d("kitpurchaserequest", Gson().toJson(request))

                saveKitPlanDataAfterSuccess(request)
            }
            else {
                if(emiList.size>0){
                    for(i in 0 until emiList.size){
                        HitApiForPayEmiAmount(emiList[i].selectedNoofEmi, emiList[i].emiNo, emiList[i].emiAmount,emiList[i].lateFine,emiList[i].loancode,dialog,utrNumber)
                    }
                }
            }

        }

        dialog.setCanceledOnTouchOutside(false)

        dialog.show()

    }



    fun saveKitPlanDataAfterSuccess(request : KitPurchasePlanSaveRequest) {
        viewModel.savePurchaseHistoryDataOnSuccessPG(request).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("savePurchaseHistoryDataOnSuccessPG", Gson().toJson(response))
                                ConstantClass.dialog.dismiss()
                                Toast.makeText(this,response.message,Toast.LENGTH_SHORT).show()
                                if(response.status==true){
                                    finish()
                                }
                            }
                        }

                    }
                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }
                    ApiStatus.ERROR->{
                        ConstantClass.dialog.dismiss()
                    }
                }
            }

        }
    }



    override fun onBackPressed() {
        showingRejectionePGPopUp()
    }




}