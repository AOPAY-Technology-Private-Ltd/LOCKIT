package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bos.payment.appName.network.ApiInterface
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.BureauScore.Companion.userScore
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.CongratulationPage.Companion.FirstName
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.CongratulationPage.Companion.LastName
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.CongratulationPage.Companion.MiddleName
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.CongratulationPage.Companion.loaneCode
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityQrcodePageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadhaarResponse
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadharBackImageUri
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadharFrontImageUri
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadharNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadharVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.AccountNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.AccountType
import com.bosandroidapp.aopaykit.constant.ConstantClass.BankID
import com.bosandroidapp.aopaykit.constant.ConstantClass.BankIFSCCode
import com.bosandroidapp.aopaykit.constant.ConstantClass.BankName
import com.bosandroidapp.aopaykit.constant.ConstantClass.BranchAddress
import com.bosandroidapp.aopaykit.constant.ConstantClass.BranchName
import com.bosandroidapp.aopaykit.constant.ConstantClass.BrandName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CardType
import com.bosandroidapp.aopaykit.constant.ConstantClass.CheckOnlineOrOffline
import com.bosandroidapp.aopaykit.constant.ConstantClass.CibilResponse
import com.bosandroidapp.aopaykit.constant.ConstantClass.ClickOnCardLowCibilScore
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustAlternateMobileNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustAlternateMobileOTP
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustAlternateMobileVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustAreaSector
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustCityName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustCode
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustCountry
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustCurrentAddress
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustFirstName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustFlatNo
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustLastName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustMiddleName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustPhotoPath
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustPinCode
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustPrimaryMobileNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustPrimaryMobileVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustPrimaryOTP
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustStateName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CusteMailID
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustomerCodeForEnach
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustomerLoanStatusPending
import com.bosandroidapp.aopaykit.constant.ConstantClass.DefaulterEmiDebitAutoApproved
import com.bosandroidapp.aopaykit.constant.ConstantClass.DefaulterEmiDebitPending
import com.bosandroidapp.aopaykit.constant.ConstantClass.DownPayment
import com.bosandroidapp.aopaykit.constant.ConstantClass.EmiAmount
import com.bosandroidapp.aopaykit.constant.ConstantClass.ImeiNumber1
import com.bosandroidapp.aopaykit.constant.ConstantClass.ImeiNumber1SealPhotoPath
import com.bosandroidapp.aopaykit.constant.ConstantClass.ImeiNumber2
import com.bosandroidapp.aopaykit.constant.ConstantClass.ImeiNumber2SealPhotoPath
import com.bosandroidapp.aopaykit.constant.ConstantClass.ImeiNumberPhotoPath
import com.bosandroidapp.aopaykit.constant.ConstantClass.InterestAmt
import com.bosandroidapp.aopaykit.constant.ConstantClass.InterestRate
import com.bosandroidapp.aopaykit.constant.ConstantClass.Invoive_Path
import com.bosandroidapp.aopaykit.constant.ConstantClass.IsRetailerAggrementVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.LoanCodeForEnach
import com.bosandroidapp.aopaykit.constant.ConstantClass.LoanEndDate
import com.bosandroidapp.aopaykit.constant.ConstantClass.LoanStartDate
import com.bosandroidapp.aopaykit.constant.ConstantClass.ModelColor
import com.bosandroidapp.aopaykit.constant.ConstantClass.ModelName
import com.bosandroidapp.aopaykit.constant.ConstantClass.ModelVarient
import com.bosandroidapp.aopaykit.constant.ConstantClass.OTPTYPE
import com.bosandroidapp.aopaykit.constant.ConstantClass.PENNYDROP_REGISTRATION_ID
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanFrontImageUri
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanNumberVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanResponse
import com.bosandroidapp.aopaykit.constant.ConstantClass.ProcessingFees
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefAddress
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefName
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefRelationShip
import com.bosandroidapp.aopaykit.constant.ConstantClass.ReferenceAadharNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.ReferenceAadharVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefmobileNo
import com.bosandroidapp.aopaykit.constant.ConstantClass.RetailerCodeForEnach
import com.bosandroidapp.aopaykit.constant.ConstantClass.Tenure
import com.bosandroidapp.aopaykit.constant.ConstantClass.ToBePaidAmount
import com.bosandroidapp.aopaykit.constant.ConstantClass.calculateEmiEndDateFromNow
import com.bosandroidapp.aopaykit.constant.ConstantClass.createMultipartFromUri
import com.bosandroidapp.aopaykit.constant.ConstantClass.dialog
import com.bosandroidapp.aopaykit.constant.ConstantClass.getCurrentStartDate
import com.bosandroidapp.aopaykit.constant.ConstantClass.iisAggrementVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.isAggrementVerified

import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.constant.ConstantClass.saveImageToCache
import com.bosandroidapp.aopaykit.data.enach.EMandateRequest
import com.bosandroidapp.aopaykit.data.enach.EnachDateUploadReq
import com.bosandroidapp.aopaykit.data.loancharge.LoanChargeReq
import com.bosandroidapp.aopaykit.data.model.CustomerKitRequest
import com.bosandroidapp.aopaykit.data.model.RaiseMakePaymentReq
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateAccessKeyReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.VerifyCustomerReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetIsEligibleLoanReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LoanCreatedReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.repository.PanRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.RetailerEMandateVerifyPage.Companion.webUrl
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment.MakePaymentRequestPage.Companion.BankAccountNumber
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment.MakePaymentRequestPage.Companion.BankHolderName
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment.MakePaymentRequestPage.Companion.BankIFSCCODE
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.ui.viewmodel.PanViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File
import kotlin.math.roundToInt
import kotlin.text.trim

class QRCodePage : BaseActivity() {
    lateinit var binding: ActivityQrcodePageBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var panViewModel: PanViewModel
    lateinit var api: ApiInterface
    lateinit var preference: SharedPreference
    var downPayment: String = ""
    var membershipAmt: String = ""
    var isEmandateVerified : String= ""
    var customerCode : String= ""
    var isPannydropVerified : String= "Yes"
    var loancreatedreq: LoanCreatedReq? = null

    companion object{
        var isEnachCancelled : Boolean = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQrcodePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        if (IMEIDetailsPage.dialog != null && IMEIDetailsPage.dialog.isShowing) {
            IMEIDetailsPage.dialog.dismiss()
        }

        preference = SharedPreference(this)
        api = RetrofitClient.apiInterface
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        panViewModel = ViewModelProvider(this, com.bosandroidapp.aopaykit.data.viewModelFactory.PanViewModelFactory(PanRepository(RetrofitClient.apiInterfacePAN)))[PanViewModel::class.java]

        binding.accesstoken.filters = arrayOf(InputFilter.AllCaps())
        binding.accesstoken.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS

        setOnClickListner()
        hitApiForMemberShipFee()

    }


    override fun onResume() {
        super.onResume()

        hitApiForLogin()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setOnClickListner() {


        binding.home.setOnClickListener {
            val intent = Intent(this, DashBoard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            onBackPressed()
        }


        binding.back.setOnClickListener {
            OpenPopUpForVAlert()
        }


        binding.validatekeylayout.setOnClickListener {
            if (!binding.accesstoken.text.toString().isNullOrBlank()) {
                hitApiForValidateKey()
            } else {
                Toast.makeText(this@QRCodePage, "Enter access key first!!", Toast.LENGTH_SHORT).show()
            }
        }


        binding.nextlayout.setOnClickListener {
            loaneCode=""
            LoanStartDate=""
            LoanEndDate=""
            loancreatedreq = null
            if (isInternetAvailable(this@QRCodePage)) {
                binding.nextlayout.isEnabled= false
                hitApiForCustomerRegister()
            }
            else {
                binding.nextlayout.isEnabled= true
                Toast.makeText(this, "Please check your internet connection .", Toast.LENGTH_SHORT).show()
            }

        }


        binding.clicktoopenappqr.setOnClickListener {
            OpenPopUpForQRScanAlert()
        }


        binding.LoanCreatelayout.setOnClickListener {
            if (loancreatedreq != null) {
                if (loaneCode.trim().isNotEmpty() && BankIFSCCode.trim().isNotEmpty() && ConstantClass.AccountHolderName.trim().isNotEmpty() && AccountType.trim().isNotEmpty()) {
                    // Loan already created in a previous attempt, retry E-Nach directly
                    val startDate = LoanStartDate
                    val endDate = LoanEndDate
                    val emiAmount = EmiAmount.toDouble().roundToInt()

                    val request = EMandateRequest(
                        categoryID = 7,
                        collectionAmount = emiAmount,
                        collectCollectionUntilCancle = false,
                        seqType = "RCUR",
                        iFSCCode = BankIFSCCode,
                        frequncy = "MNTH",
                        registrationID = ConstantClass.PAN_VERIFICATION_REGISTRATION_ID,
                        accountHolderName = ConstantClass.AccountHolderName,
                        finalCollectionDate = endDate,
                        loanNo = loaneCode,
                        accountType = AccountType,
                        emailAddress = CusteMailID,
                        firstCollectionDate = startDate,
                        mobileNumber = CustPrimaryMobileNumber,
                        bankAccountNumberConfirmation = AccountNumber,
                        addIn2 = BranchAddress,
                        addIn3 = "",
                        debitType = true,
                        teleNumber = "",
                        authType = "",
                        bankID = BankID,
                        bankAccountNumber = AccountNumber
                    )
                    hitApiForEnach(request,true)
                }
                else {
                    // No loan created yet, proceed with the normal flow
                    hitApiForCheckLoanCharge(loancreatedreq!!)
                }
            }
        }



    }



    @SuppressLint("SetTextI18n")
    fun OpenPopUpForQRScanAlert() {
        dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.appdownloadqrlayout)


        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }


        dialog.setCanceledOnTouchOutside(false)

        val cancel = dialog.findViewById<Button>(R.id.btnClose)

        val provisioningQR = dialog.findViewById<ImageView>(R.id.qr_code_provising)
        val progressbar = dialog.findViewById<ProgressBar>(R.id.progressbar)

        hitApiForDownloadAppUrlLinkQR(provisioningQR,progressbar)

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun hitApiForCustomerRegister() {

        val custPhotoPart = createMultipartFromUri(this, CustPhotoPath, "CustPhoto_File", "CustomerPhoto")

        val imei1SealPart = createMultipartFromUri(
            this,
            ImeiNumber1SealPhotoPath,
            "IMEINumber1_SealPhotoFile",
            "IMEINumber1Image"
        )

        val imei2SealPart = createMultipartFromUri(
            this,
            ImeiNumber2SealPhotoPath,
            "IMEINumber2_SealPhotoFile",
            "IMEINumber2Image"
        )

        val imeiPhotoPart = createMultipartFromUri(
            this,
            ImeiNumberPhotoPath,
            "IMEINumberPhotoFile",
            "IMEINumberImage"
        )
        val invoicePart = createMultipartFromUri(this, Invoive_Path, "InvoiceFile", "InvoiceImage")
        val aadharFrontPart = createMultipartFromUri(
            this,
            AadharFrontImageUri,
            "CustAadharPhoto_File",
            "AadharFrontImage"
        )
        val aadharBackPart = createMultipartFromUri(
            this,
            AadharBackImageUri,
            "CustAadharBackPhoto_File",
            "AadharBackImage"
        )
        val PanFrontPart = createMultipartFromUri(this, PanFrontImageUri, "CustPanNumberPhoto_File", "PanFrontImage")

        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val safeLastName = if (!lastName.isNullOrBlank() && lastName != "null") lastName else ""
        var createdBy = firstName.plus(" ").plus(safeLastName)
        var retailercode = preference.getStringValue(ConstantClass.RetailerCode, "")

        if(CustCountry.isNullOrBlank()){
            CustCountry ="India"
        }

        if(ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.kit)){
            VeryfyKitCustomer()
        }
        else{
            ConstantClass.OpenLoader(this)
            if (ConstantClass.ClickOnCardLowCibilScore.equals(CardType)) {
                // Map safely (avoid !!)
                val requestMap = hashMapOf(
                    "Mode" to "UPDATE".toRequestBody(),
                    "FirstName" to (CustFirstName ?: "").toRequestBody(),
                    "MiddleName" to (CustMiddleName ?: "").toRequestBody(),
                    "LastName" to (CustLastName ?: "").toRequestBody(),
                    "PrimaryMobileNumber" to (CustPrimaryMobileNumber ?: "").toRequestBody(),
                    "PrimaryOTP" to (CustPrimaryOTP ?: "").toRequestBody(),
                    "PrimaryMobileVerified" to (CustPrimaryMobileVerified ?: "").toRequestBody(),
                    "AlternateMobileNumber" to (CustAlternateMobileNumber ?: "").toRequestBody(),
                    "AlternateMobileOTP" to "".toRequestBody(),
                    "PAlternateMobileVerified" to "no".toRequestBody(),
                    "EMailID" to (CusteMailID ?: "").toRequestBody(),
                    "FlatNo" to (CustFlatNo ?: "").toRequestBody(),
                    "AearSector" to (CustAreaSector ?: "").toRequestBody(),
                    "PinCode" to (CustPinCode ?: "").toRequestBody(),
                    "CurrentAddress" to (CustCurrentAddress ?: "").toRequestBody(),
                    "StateName" to (CustStateName ?: "").toRequestBody(),
                    "CityName" to (CustCityName ?: "").toRequestBody(),
                    "Country" to (CustCountry ?: "").toRequestBody(),
                    "AadharNumber" to (AadharNumber ?: "").toRequestBody(),
                    "AadharNumberVerified" to (ConstantClass.AadharVerified ?: "").toRequestBody(),
                    "PANNumber" to (PanNumber ?: "").toRequestBody(),
                    "PANNumberVerified" to (PanNumberVerified ?: "").toRequestBody(),
                    "BrandName" to BrandName.toRequestBody(),
                    "ModelName" to ConstantClass.ModelName.toRequestBody(),
                    "ModelVariant" to ConstantClass.ModelVarient.toRequestBody(),
                    "Color" to ConstantClass.ModelColor.toRequestBody(),
                    "SellingPrice" to ConstantClass.SellingPrice.toRequestBody(),
                    "DownPayment" to downPayment.toRequestBody(),
                    "Tenure" to ConstantClass.Tenure.toRequestBody(),
                    "EMIAmount" to EmiAmount.toRequestBody(),
                    "IMEINumber1" to ImeiNumber1.toRequestBody(),
                    "IMEINumber2" to ImeiNumber2.toRequestBody(),
                    "AccountNumber" to AccountNumber.toRequestBody(),
                    "BankIFSCCode" to BankIFSCCode.toRequestBody(),
                    "BankName" to BankName.toRequestBody(),
                    "AccountType" to AccountType.toRequestBody(),
                    "BranchName" to BranchName.toRequestBody(),
                    "RefName" to RefName.toRequestBody(),
                    "MemberShipFees" to membershipAmt.toRequestBody(),
                    "RefRelationShip" to RefRelationShip.toRequestBody(),
                    "RefmobileNo" to RefmobileNo.toRequestBody(),
                    "RefAddress" to RefAddress.toRequestBody(),
                    "PanApiResponse" to (PanResponse ?: "").toRequestBody(),
                    "AadhaarApiResponse" to (AadhaarResponse ?: "").toRequestBody(),
                    "CibilApiResponse" to (CibilResponse ?: "").toRequestBody(),
                    "CustomerCodes" to CustCode.toRequestBody(),
                    "RefRelationShip" to RefRelationShip.toRequestBody(),
                    "RefmobileNo" to RefmobileNo.toRequestBody(),
                    "RefAddress" to RefAddress.toRequestBody(),
                    "DebitOrCreditCard" to "".toRequestBody(),
                    "UPIMandate" to "yes".toRequestBody(),
                    "CreatedBy" to createdBy.toRequestBody(),
                    "RetailerCode" to retailercode.toRequestBody(),
                    "CibilScore" to userScore.toString().toRequestBody(),
                    "IsAggrementVerified" to isAggrementVerified.toRequestBody(),
                    "IsRetailerAggrementVerified" to IsRetailerAggrementVerified.toRequestBody(),
                )

                // Debug log full request
                Log.e("API_REQ_MAP", Gson().toJson(requestMap))

                Log.e(
                    "API_REQ_IMAGES",
                    "CustPhoto=$CustPhotoPath | AadharFront=$AadharFrontImageUri | PanFront=$PanFrontImageUri"
                )

                lifecycleScope.launch {
                    try {
                        val response = api.getCustomerCibilApprovedReq(
                            requestMap["Mode"]!!,
                            requestMap["FirstName"]!!,
                            requestMap["MiddleName"]!!,
                            requestMap["LastName"]!!,
                            requestMap["PrimaryMobileNumber"]!!,
                            requestMap["PrimaryOTP"]!!,
                            requestMap["PrimaryMobileVerified"]!!,
                            requestMap["AlternateMobileNumber"]!!,
                            requestMap["AlternateMobileOTP"]!!,
                            requestMap["PAlternateMobileVerified"]!!,
                            requestMap["EMailID"]!!,
                            requestMap["FlatNo"]!!,
                            requestMap["AearSector"]!!,
                            requestMap["PinCode"]!!,
                            requestMap["CurrentAddress"]!!,
                            requestMap["StateName"]!!,
                            requestMap["CityName"]!!,
                            requestMap["Country"]!!,
                            requestMap["AadharNumber"]!!,
                            requestMap["AadharNumberVerified"]!!,
                            requestMap["PANNumber"]!!,
                            requestMap["PANNumberVerified"]!!,
                            requestMap["BrandName"]!!,
                            requestMap["ModelName"]!!,
                            requestMap["ModelVariant"]!!,
                            requestMap["Color"]!!,
                            requestMap["SellingPrice"]!!,
                            requestMap["DownPayment"]!!,
                            requestMap["Tenure"]!!,
                            requestMap["EMIAmount"]!!,
                            requestMap["IMEINumber1"]!!,
                            requestMap["IMEINumber2"]!!,
                            requestMap["AccountNumber"]!!,
                            requestMap["BankIFSCCode"]!!,
                            requestMap["BankName"]!!,
                            requestMap["AccountType"]!!,
                            requestMap["BranchName"]!!,
                            requestMap["RefName"]!!,
                            requestMap["RefRelationShip"]!!,
                            requestMap["RefmobileNo"]!!,
                            requestMap["RefAddress"]!!,
                            requestMap["DebitOrCreditCard"]!!,
                            requestMap["UPIMandate"]!!,
                            requestMap["CreatedBy"]!!,
                            requestMap["MemberShipFees"]!!,
                            requestMap["PanApiResponse"]!!,
                            requestMap["AadhaarApiResponse"]!!,
                            requestMap["CibilApiResponse"]!!,
                            requestMap["CustomerCodes"]!!,
                            requestMap["RetailerCode"]!!,
                            requestMap["CibilScore"]!!,
                            requestMap["IsAggrementVerified"]!!,
                            requestMap["IsRetailerAggrementVerified"]!!,
                            custPhotoPart,
                            imei1SealPart,
                            imei2SealPart,
                            imeiPhotoPart,
                            invoicePart,
                            aadharFrontPart,
                            aadharBackPart,
                            PanFrontPart
                        )

                        withContext(Dispatchers.Main) {
                            ConstantClass.dialog?.takeIf { it.isShowing }?.dismiss()
                            Log.e("API_RESPONSE_CODE", response.code().toString())


                            if (response.isSuccessful) {
                                val body = response.body()
                                var message = body?.message ?: "Success"
                                Log.e("API_RESPONSE_SUCCESS", Gson().toJson(body))

                                Log.d("createcustresp", Gson().toJson(body))

                                if (body!!.statuss.equals("FAILED")) {
                                    if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                                        ConstantClass.dialog.dismiss()
                                    }
                                    binding.validateKeyLayout.visibility = View.GONE
                                    binding.nextlayout.visibility = View.VISIBLE
                                    CheckOnlineOrOffline =""
                                    binding.nextlayout.isEnabled= true
                                    Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this@QRCodePage, DashBoard::class.java))
                                    finish()
                                    clearData()
                                }
                                else if (body!!.statuss.equals("401")) {
                                    if (ConstantClass.dialog?.isShowing == true) {
                                        ConstantClass.dialog.dismiss()
                                    }
                                    binding.nextlayout.isEnabled= true
                                    binding.validateKeyLayout.visibility = View.GONE
                                    binding.nextlayout.visibility = View.VISIBLE
                                    Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_LONG).show()
                                }
                                else {

                                    customerCode = body?.customerCode!!

                                    if (body.statuss.equals("218")) {
                                        if (ConstantClass.dialog?.isShowing == true) {
                                            ConstantClass.dialog.dismiss()
                                        }
                                        binding.nextlayout.isEnabled= true
                                        binding.validateKeyLayout.visibility = View.GONE
                                        binding.nextlayout.visibility = View.VISIBLE
                                        Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()
                                    } else if (body.statuss.equals("219")) {
                                        if (ConstantClass.dialog?.isShowing == true) {
                                            ConstantClass.dialog.dismiss()
                                        }
                                        binding.nextlayout.isEnabled= true
                                        binding.validateKeyLayout.visibility = View.GONE
                                        binding.nextlayout.visibility = View.VISIBLE
                                        Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()
                                    }
                                    else if(body.statuss.equals("226")){
                                        if (ConstantClass.dialog?.isShowing == true) {
                                            ConstantClass.dialog.dismiss()
                                        }
                                        binding.nextlayout.isEnabled= true
                                        binding.validateKeyLayout.visibility = View.GONE
                                        binding.nextlayout.visibility = View.VISIBLE
                                        Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()
                                    }
                                    else if(body.statuss.equals("213")){
                                        if (ConstantClass.dialog?.isShowing == true) {
                                            ConstantClass.dialog.dismiss()
                                        }
                                        binding.nextlayout.isEnabled= true
                                        binding.validateKeyLayout.visibility = View.GONE
                                        binding.nextlayout.visibility = View.VISIBLE
                                        Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()
                                    }
                                    else {
                                        val safecustomerCode = if (!customerCode.isNullOrBlank() && customerCode != "null") customerCode else ""
                                        if(safecustomerCode.isNullOrBlank()){
                                            if (ConstantClass.dialog?.isShowing == true) {
                                                ConstantClass.dialog.dismiss()
                                            }
                                            binding.nextlayout.isEnabled= true
                                            binding.validateKeyLayout.visibility = View.GONE
                                            binding.nextlayout.visibility = View.VISIBLE
                                            Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()
                                        }else{
                                            val startDate = getCurrentStartDate()
                                            val endDate = calculateEmiEndDateFromNow(Tenure.toInt())

                                            loancreatedreq = LoanCreatedReq(
                                                modetype = "INSERT",
                                                rid = 0,
                                                customerCode = safecustomerCode,
                                                loanAmount = ConstantClass.LoanAmount.toDouble(),
                                                downPayment = downPayment.toDouble(),
                                                emiAmount = EmiAmount.toDouble(),
                                                tenure = Tenure.toInt(),
                                                interestRate = InterestRate.toDouble(),
                                                startDate = startDate,
                                                endDate = endDate,
                                                imeiNumber = ImeiNumber1,
                                                createdBy = createdBy,
                                                brandname = BrandName,
                                                modelname = ModelName,
                                                variantname = ModelVarient,
                                                avlcolor = ModelColor,
                                                retailerCode = retailercode,
                                                processingFees = ProcessingFees,
                                                interestAmt = InterestAmt,
                                                remarks = "",
                                                recordStatus = CustomerLoanStatusPending,
                                                creditScore = userScore.toString(),
                                                validateKey = binding.accesstoken.text.toString(),
                                                defaultEmidebit = DefaulterEmiDebitPending,
                                                sellingPrice = ConstantClass.SellingPrice.toDouble(),
                                                loanMode = ConstantClass.online
                                            )
                                            Log.d("LoanCreateReq", Gson().toJson(loancreatedreq))

                                            if (ConstantClass.dialog?.isShowing == true) {
                                                ConstantClass.dialog.dismiss()
                                            }

                                            binding.validateKeyLayout.visibility = View.VISIBLE
                                            binding.nextlayout.visibility = View.GONE


                                        }

                                    }
                                }
                            }
                            else {
                                val err = response.errorBody()?.string()
                                binding.nextlayout.isEnabled= true
                                Log.e("API_RESPONSE_ERROR", err ?: "Unknown error")
                            }

                        }

                    } catch (e: Exception) {
                        binding.nextlayout.isEnabled= true
                        ConstantClass.dialog?.takeIf { it.isShowing }?.dismiss()
                        Log.e("API_EXCEPTION", "Error: ${e.localizedMessage}", e)
                    }
                }

            }
            else {

                if (ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.online)) {

                }
                else {
                    userScore = 0.0f
                }

                val requestMap = hashMapOf(
                    "Mode" to "INSERT".toRequestBody(),
                    "FirstName" to CustFirstName.toRequestBody(),
                    "MiddleName" to CustMiddleName.toRequestBody(),
                    "LastName" to CustLastName.toRequestBody(),
                    "PrimaryMobileNumber" to CustPrimaryMobileNumber.toRequestBody(),
                    "PrimaryOTP" to CustPrimaryOTP.toRequestBody(),
                    "PrimaryMobileVerified" to CustPrimaryMobileVerified.toRequestBody(),
                    "AlternateMobileNumber" to CustAlternateMobileNumber.toRequestBody(),
                    "AlternateMobileOTP" to CustAlternateMobileOTP.toRequestBody(),
                    "PAlternateMobileVerified" to CustAlternateMobileVerified.toRequestBody(),
                    "EMailID" to CusteMailID.toRequestBody(),
                    "FlatNo" to CustFlatNo.toRequestBody(),
                    "AearSector" to CustAreaSector.toRequestBody(),
                    "PinCode" to CustPinCode.toRequestBody(),
                    "CurrentAddress" to CustCurrentAddress.toRequestBody(),
                    "StateName" to CustStateName.toRequestBody(),
                    "CityName" to CustCityName.toRequestBody(),
                    "Country" to CustCountry!!.toRequestBody(),
                    "AadharNumber" to AadharNumber.toRequestBody(),
                    "AadharNumberVerified" to ConstantClass.AadharVerified.toRequestBody(),/*"".toRequestBody()*/
                    "PANNumber" to PanNumber.toRequestBody(),
                    "PANNumberVerified" to PanNumberVerified.toRequestBody(), /*"".toRequestBody()*/
                    "BrandName" to BrandName.toRequestBody(),
                    "ModelName" to ConstantClass.ModelName.toRequestBody(),
                    "ModelVariant" to ConstantClass.ModelVarient.toRequestBody(),
                    "Color" to ConstantClass.ModelColor.toRequestBody(),
                    "SellingPrice" to ConstantClass.SellingPrice.toRequestBody(),
                    "DownPayment" to downPayment.toRequestBody(),
                    "Tenure" to ConstantClass.Tenure.toRequestBody(),
                    "EMIAmount" to EmiAmount.toRequestBody(),
                    "IMEINumber1" to ImeiNumber1.toRequestBody(),
                    "IMEINumber2" to ImeiNumber2.toRequestBody(),
                    "AccountNumber" to AccountNumber.toRequestBody(),
                    "BankIFSCCode" to BankIFSCCode.toRequestBody(),
                    "BankName" to BankName.toRequestBody(),
                    "AccountType" to AccountType.toRequestBody(),
                    "BranchName" to BranchName.toRequestBody(),
                    "RefName" to RefName.toRequestBody(),
                    "MemberShipFees" to membershipAmt.toRequestBody(),
                    "RefRelationShip" to RefRelationShip.toRequestBody(),
                    "RefmobileNo" to RefmobileNo.toRequestBody(),
                    "RefAddress" to RefAddress.toRequestBody(),
                    "DebitOrCreditCard" to "".toRequestBody(),
                    "UPIMandate" to "yes".toRequestBody(),
                    "CreatedBy" to createdBy.toRequestBody(),
                    "RetailerCode" to retailercode.toRequestBody(),
                    "PanApiResponse" to (PanResponse ?: "").toRequestBody(),
                    "AadhaarApiResponse" to (AadhaarResponse ?: "").toRequestBody(),
                    "CibilApiResponse" to (CibilResponse ?: "").toRequestBody(),
                    "CibilScore" to userScore.toString().toRequestBody(),
                    "IsAggrementVerified" to isAggrementVerified.toRequestBody(),
                    "IsRetailerAggrementVerified" to IsRetailerAggrementVerified.toRequestBody(),
                    "IsRefAdhaarVerified" to ReferenceAadharVerified.toRequestBody(),
                    "IsRefAadhaarNumber" to ReferenceAadharNumber.toRequestBody()
                )

                Log.d("RefVerified", "${ ReferenceAadharVerified } ${ ReferenceAadharNumber }")

                Log.d("RequestRegis", requestMap.toString())

                if (ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.online)) {

                    lifecycleScope.launch {
                        try {
                            val response = api.getRegisterOnlineCustomerReq(
                                requestMap["Mode"]!!,
                                requestMap["FirstName"]!!,
                                requestMap["MiddleName"]!!,
                                requestMap["LastName"]!!,
                                requestMap["PrimaryMobileNumber"]!!,
                                requestMap["PrimaryOTP"]!!,
                                requestMap["PrimaryMobileVerified"]!!,
                                requestMap["AlternateMobileNumber"]!!,
                                requestMap["AlternateMobileOTP"]!!,
                                requestMap["PAlternateMobileVerified"]!!,
                                requestMap["EMailID"]!!,
                                requestMap["FlatNo"]!!,
                                requestMap["AearSector"]!!,
                                requestMap["PinCode"]!!,
                                requestMap["CurrentAddress"]!!,
                                requestMap["StateName"]!!,
                                requestMap["CityName"]!!,
                                requestMap["Country"]!!,
                                requestMap["AadharNumber"]!!,
                                requestMap["AadharNumberVerified"]!!,
                                requestMap["PANNumber"]!!,
                                requestMap["PANNumberVerified"]!!,
                                requestMap["BrandName"]!!,
                                requestMap["ModelName"]!!,
                                requestMap["ModelVariant"]!!,
                                requestMap["Color"]!!,
                                requestMap["SellingPrice"]!!,
                                requestMap["DownPayment"]!!,
                                requestMap["Tenure"]!!,
                                requestMap["EMIAmount"]!!,
                                requestMap["IMEINumber1"]!!,
                                requestMap["IMEINumber2"]!!,
                                requestMap["AccountNumber"]!!,
                                requestMap["BankIFSCCode"]!!,
                                requestMap["BankName"]!!,
                                requestMap["AccountType"]!!,
                                requestMap["BranchName"]!!,
                                requestMap["RefName"]!!,
                                requestMap["RefRelationShip"]!!,
                                requestMap["RefmobileNo"]!!,
                                requestMap["RefAddress"]!!,
                                requestMap["DebitOrCreditCard"]!!,
                                requestMap["UPIMandate"]!!,
                                requestMap["CreatedBy"]!!,
                                requestMap["MemberShipFees"]!!,
                                requestMap["RetailerCode"]!!,
                                requestMap["PanApiResponse"]!!,
                                requestMap["AadhaarApiResponse"]!!,
                                requestMap["CibilApiResponse"]!!,
                                requestMap["CibilScore"]!!,
                                requestMap["IsAggrementVerified"]!!,
                                requestMap["IsRetailerAggrementVerified"]!!,
                                requestMap["IsRefAdhaarVerified"]!!,
                                requestMap["IsRefAadhaarNumber"]!!,
                                custPhotoPart!!,
                                imei1SealPart!!,
                                imei2SealPart!!,
                                imeiPhotoPart!!,
                                invoicePart!!
                            )

                            if (response.isSuccessful) {
                                // Handle success
                                val body = response.body()
                                /* ConstantClass.dialog.dismiss()
                                 startActivity(Intent(this@QRCodePage,CongratulationPage::class.java))*/
                                var message = body?.message ?: "Success"

                                Log.d("createcustresp", Gson().toJson(body))


                                if (body!!.statuss.equals("FAILED")) {
                                    if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                                        ConstantClass.dialog.dismiss()
                                    }
                                    CheckOnlineOrOffline =""
                                    binding.nextlayout.isEnabled= true
                                    binding.validateKeyLayout.visibility = View.GONE
                                    binding.nextlayout.visibility = View.VISIBLE
                                    Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this@QRCodePage, DashBoard::class.java))
                                    finish()
                                    clearData()
                                }

                                else if (body!!.statuss.equals("401")) {
                                    if (ConstantClass.dialog?.isShowing == true) {
                                        ConstantClass.dialog.dismiss()
                                    }
                                    binding.validateKeyLayout.visibility = View.GONE
                                    binding.nextlayout.visibility = View.VISIBLE
                                    Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_LONG).show()
                                }

                                else if(body.statuss.equals("226")){
                                    if (ConstantClass.dialog?.isShowing == true) {
                                        ConstantClass.dialog.dismiss()
                                    }
                                    binding.nextlayout.isEnabled= true
                                    binding.validateKeyLayout.visibility = View.GONE
                                    binding.nextlayout.visibility = View.VISIBLE
                                    Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()
                                }
                                else if(body.statuss.equals("213")){
                                    if (ConstantClass.dialog?.isShowing == true) {
                                        ConstantClass.dialog.dismiss()
                                    }
                                    binding.nextlayout.isEnabled= true
                                    binding.validateKeyLayout.visibility = View.GONE
                                    binding.nextlayout.visibility = View.VISIBLE
                                    Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()
                                }

                                else {
                                    customerCode = body?.customerCode!!


                                    val safecustomerCode = if (!customerCode.isNullOrBlank() && customerCode != "null") customerCode else ""

                                    if(safecustomerCode.isNullOrBlank()){
                                        if (ConstantClass.dialog?.isShowing == true) {
                                            ConstantClass.dialog.dismiss()
                                        }
                                        binding.nextlayout.isEnabled= true
                                        binding.validateKeyLayout.visibility = View.GONE
                                        binding.nextlayout.visibility = View.VISIBLE
                                        Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()
                                    }
                                    else{
                                        val startDate = getCurrentStartDate()
                                        val endDate = calculateEmiEndDateFromNow(Tenure.toInt())

                                        loancreatedreq = LoanCreatedReq(
                                            modetype = "INSERT",
                                            rid = 0,
                                            customerCode = safecustomerCode,
                                            loanAmount = ConstantClass.LoanAmount.toDouble(),
                                            downPayment = downPayment.toDouble(),
                                            emiAmount = EmiAmount.toDouble(),
                                            tenure = Tenure.toInt(),
                                            interestRate = InterestRate.toDouble(),
                                            startDate = startDate,
                                            endDate = endDate,
                                            imeiNumber = ImeiNumber1,
                                            createdBy = createdBy,
                                            brandname = BrandName,
                                            modelname = ModelName,
                                            variantname = ModelVarient,
                                            avlcolor = ModelColor,
                                            retailerCode = retailercode,
                                            processingFees = ProcessingFees,
                                            interestAmt = InterestAmt,
                                            remarks = "",
                                            recordStatus = ConstantClass.CustomerLoanStatus,
                                            creditScore = userScore.toString(),
                                            validateKey = binding.accesstoken.text.toString(),
                                            defaultEmidebit = DefaulterEmiDebitAutoApproved,
                                            sellingPrice = ConstantClass.SellingPrice.toDouble(),
                                            loanMode = ConstantClass.online
                                        )
                                        Log.d("LoanCreateReq", Gson().toJson(loancreatedreq))

                                        if (ConstantClass.dialog?.isShowing == true) {
                                            ConstantClass.dialog.dismiss()
                                        }

                                        binding.validateKeyLayout.visibility = View.VISIBLE
                                        binding.nextlayout.visibility = View.GONE
                                    }
                                }

                            }
                            else {
                                val errorMsg = response.errorBody()?.string()
                                handleApiError(response.code(), errorMsg)
                            }
                        }
                        catch (e: Exception) {

                            if (ConstantClass.dialog?.isShowing == true) {
                                ConstantClass.dialog.dismiss()
                            }
                            binding.nextlayout.isEnabled= true
                            val errorMsg = when (e) {
                                is java.net.SocketTimeoutException ->
                                    "Connection timed out. Please check internet."

                                is java.net.UnknownHostException ->
                                    "No internet connection."

                                is java.io.IOException ->
                                    "Network error. Please try again."

                                else ->
                                    "Unexpected error occurred."

                            }

                            Log.e("API_EXCEPTION", e.message ?: "Unknown Exception")

                            Toast.makeText(this@QRCodePage, errorMsg, Toast.LENGTH_LONG).show()

                        }

                    }

                }
                else {
                    lifecycleScope.launch {
                        try {
                            val response = api.getRegisterCustomerReq(
                                requestMap["Mode"]!!,
                                requestMap["FirstName"]!!,
                                requestMap["MiddleName"]!!,
                                requestMap["LastName"]!!,
                                requestMap["PrimaryMobileNumber"]!!,
                                requestMap["PrimaryOTP"]!!,
                                requestMap["PrimaryMobileVerified"]!!,
                                requestMap["AlternateMobileNumber"]!!,
                                requestMap["AlternateMobileOTP"]!!,
                                requestMap["PAlternateMobileVerified"]!!,
                                requestMap["EMailID"]!!,
                                requestMap["FlatNo"]!!,
                                requestMap["AearSector"]!!,
                                requestMap["PinCode"]!!,
                                requestMap["CurrentAddress"]!!,
                                requestMap["StateName"]!!,
                                requestMap["CityName"]!!,
                                requestMap["Country"]!!,
                                requestMap["AadharNumber"]!!,
                                requestMap["AadharNumberVerified"]!!,
                                requestMap["PANNumber"]!!,
                                requestMap["PANNumberVerified"]!!,
                                requestMap["BrandName"]!!,
                                requestMap["ModelName"]!!,
                                requestMap["ModelVariant"]!!,
                                requestMap["Color"]!!,
                                requestMap["SellingPrice"]!!,
                                requestMap["DownPayment"]!!,
                                requestMap["Tenure"]!!,
                                requestMap["EMIAmount"]!!,
                                requestMap["IMEINumber1"]!!,
                                requestMap["IMEINumber2"]!!,
                                requestMap["AccountNumber"]!!,
                                requestMap["BankIFSCCode"]!!,
                                requestMap["BankName"]!!,
                                requestMap["AccountType"]!!,
                                requestMap["BranchName"]!!,
                                requestMap["RefName"]!!,
                                requestMap["RefRelationShip"]!!,
                                requestMap["RefmobileNo"]!!,
                                requestMap["RefAddress"]!!,
                                requestMap["DebitOrCreditCard"]!!,
                                requestMap["UPIMandate"]!!,
                                requestMap["CreatedBy"]!!,
                                requestMap["MemberShipFees"]!!,
                                requestMap["RetailerCode"]!!,
                                requestMap["CibilScore"]!!,
                                requestMap["IsAggrementVerified"]!!,
                                requestMap["IsRetailerAggrementVerified"]!!,
                                custPhotoPart ?: null,
                                imei1SealPart ?: null,
                                imei2SealPart ?: null,
                                imeiPhotoPart ?: null,
                                invoicePart ?: null,
                                aadharFrontPart ?: null,
                                aadharBackPart ?: null,
                                PanFrontPart ?: null
                            )

                            if (response.isSuccessful) {
                                // Handle success
                                val body = response.body()

                                var message = body?.message ?: "Success"

                                Log.d("createcustresp", Gson().toJson(body))


                                if (body!!.statuss.equals("FAILED")) {
                                    if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                                        ConstantClass.dialog.dismiss()
                                    }
                                    CheckOnlineOrOffline =""
                                    binding.nextlayout.isEnabled= true
                                    binding.validateKeyLayout.visibility = View.GONE
                                    binding.nextlayout.visibility = View.VISIBLE
                                    Log.d("customer create", body.message)
                                    Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this@QRCodePage, DashBoard::class.java))
                                    finish()
                                    clearData()
                                }
                                else if (body!!.statuss.equals("401")) {
                                    binding.validateKeyLayout.visibility = View.GONE
                                    binding.nextlayout.visibility = View.VISIBLE
                                    if (ConstantClass.dialog?.isShowing == true) {
                                        ConstantClass.dialog.dismiss()
                                    }
                                    binding.nextlayout.isEnabled= true
                                    Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_LONG).show()
                                }
                                else {
                                    customerCode = body?.customerCode!!

                                    if (body.statuss.equals("218")) {
                                        if (ConstantClass.dialog?.isShowing == true) {
                                            ConstantClass.dialog.dismiss()
                                        }
                                        binding.nextlayout.isEnabled= true
                                        binding.validateKeyLayout.visibility = View.GONE
                                        binding.nextlayout.visibility = View.VISIBLE
                                        Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()

                                    } else if (body.statuss.equals("219")) {
                                        if (ConstantClass.dialog?.isShowing == true) {
                                            ConstantClass.dialog.dismiss()
                                        }
                                        binding.nextlayout.isEnabled= true
                                        binding.validateKeyLayout.visibility = View.GONE
                                        binding.nextlayout.visibility = View.VISIBLE
                                        Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()
                                    }
                                    else if(body.statuss.equals("226")){
                                        if (ConstantClass.dialog?.isShowing == true) {
                                            ConstantClass.dialog.dismiss()
                                        }
                                        binding.nextlayout.isEnabled= true
                                        binding.validateKeyLayout.visibility = View.GONE
                                        binding.nextlayout.visibility = View.VISIBLE
                                        Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()
                                    }
                                    else if(body.statuss.equals("213")){
                                        if (ConstantClass.dialog?.isShowing == true) {
                                            ConstantClass.dialog.dismiss()
                                        }
                                        binding.nextlayout.isEnabled= true
                                        binding.validateKeyLayout.visibility = View.GONE
                                        binding.nextlayout.visibility = View.VISIBLE
                                        Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()
                                    }
                                    else {
                                        val safecustomerCode = if (!customerCode.isNullOrBlank() && customerCode != "null") customerCode else ""
                                        customerCode = safecustomerCode

                                        if(safecustomerCode.isNullOrBlank()){
                                            if (ConstantClass.dialog?.isShowing == true) {
                                                ConstantClass.dialog.dismiss()
                                            }
                                            binding.nextlayout.isEnabled= true
                                            binding.validateKeyLayout.visibility = View.GONE
                                            binding.nextlayout.visibility = View.VISIBLE
                                            Toast.makeText(this@QRCodePage, body.message, Toast.LENGTH_SHORT).show()
                                        }
                                        else{
                                            val startDate = getCurrentStartDate()
                                            val endDate = calculateEmiEndDateFromNow(Tenure.toInt())

                                            loancreatedreq = LoanCreatedReq(
                                                modetype = "INSERT",
                                                rid = 0,
                                                customerCode = safecustomerCode,
                                                loanAmount = ConstantClass.LoanAmount.toDouble(),
                                                downPayment = downPayment.toDouble(),
                                                emiAmount = EmiAmount.toDouble(),
                                                tenure = Tenure.toInt(),
                                                interestRate = InterestRate.toDouble(),
                                                startDate = startDate,
                                                endDate = endDate,
                                                imeiNumber = ImeiNumber1,
                                                createdBy = createdBy,
                                                brandname = BrandName,
                                                modelname = ModelName,
                                                variantname = ModelVarient,
                                                avlcolor = ModelColor,
                                                retailerCode = retailercode,
                                                processingFees = ProcessingFees,
                                                interestAmt = InterestAmt,
                                                remarks = "",
                                                recordStatus = ConstantClass.CustomerLoanStatus,
                                                creditScore = userScore.toString(),
                                                validateKey = binding.accesstoken.text.toString(),
                                                defaultEmidebit = DefaulterEmiDebitPending,
                                                sellingPrice = ConstantClass.SellingPrice.toDouble(),
                                                loanMode = ConstantClass.CheckOnlineOrOffline
                                            )
                                            Log.d("LoanCreateReq", Gson().toJson(loancreatedreq))

                                            if (ConstantClass.dialog?.isShowing == true) {
                                                ConstantClass.dialog.dismiss()
                                            }

                                            binding.validateKeyLayout.visibility = View.VISIBLE
                                            binding.nextlayout.visibility = View.GONE
                                        }
                                    }

                                }

                            }
                            else {
                                val errorMsg = response.errorBody()?.string()
                                handleApiError(response.code(), errorMsg)
                            }
                        } catch (e: Exception) {
                            if (ConstantClass.dialog?.isShowing == true) {
                                ConstantClass.dialog.dismiss()
                            }
                            binding.nextlayout.isEnabled= true

                            val errorMsg = when (e) {
                                is java.net.SocketTimeoutException ->
                                    "Connection timed out. Please check internet."

                                is java.net.UnknownHostException ->
                                    "No internet connection."

                                is java.io.IOException ->
                                    "Network error. Please try again."

                                else ->
                                    "Unexpected error occurred."
                            }

                            Log.e("API_EXCEPTION", e.message ?: "Unknown Exception")

                            Toast.makeText(this@QRCodePage, errorMsg, Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }


    }

    fun VeryfyKitCustomer(){
        var sendOtpReq = VerifyCustomerReq(
            primaryMobileNumber = CustPrimaryMobileNumber.toString().trim()
        )
        Log.d("verifyKitcustomerreq", Gson().toJson(sendOtpReq))
        viewModel.verifyKitcustomerReq(sendOtpReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("verifycustomerresp", Gson().toJson(response))
                                ConstantClass.dialog.dismiss()

                                if(response.statuss!!.toLowerCase().equals("true", ignoreCase = true)){
                                    hitApiForKitCustomerRegister()
                                }
                                else{
                                    // if customer exist
                                    haskitCustomerRegisterPopUp()

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

    fun haskitCustomerRegisterPopUp(){
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
        cancel.visibility =View.GONE

        done.text = "OK"

        txt.text = "The customer already exists with the same number ${CustPrimaryMobileNumber} and has already installed the kit."

        done.setOnClickListener {
            dialog.dismiss()
            hitApiForKitCustomerRegister()
        }


        dialog.show()
    }


    fun hitApiForKitCustomerRegister(){
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val safeLastName = if (!lastName.isNullOrBlank() && lastName != "null") lastName else ""
        var createdBy = firstName.plus(" ").plus(safeLastName)
        var retailercode = preference.getStringValue(ConstantClass.RetailerCode, "")

        val custPhotoPart = CustPhotoPath?.let {
            saveImageToCache(this, it, "CustomerPhoto")
        }

        val imei1SealPart = ImeiNumber1SealPhotoPath?.let {
            saveImageToCache(this, it, "IMEINumber1Image")
        }

        val imei2SealPart = ImeiNumber2SealPhotoPath?.let {
            saveImageToCache(this, it, "IMEINumber2Image")
        }

        val imeiPhotoPart = ImeiNumberPhotoPath?.let {
            saveImageToCache(this, it, "IMEINumberImage")
        }

        val invoicePart = Invoive_Path?.let {
            saveImageToCache(this, it, "InvoiceImage")
        }

        val aadharFrontPart = AadharFrontImageUri?.let {
            saveImageToCache(this, it, "AadharFrontImage")
        }

        val aadharBackPart = AadharBackImageUri?.let {
            saveImageToCache(this, it, "AadharBackImage")
        }

        val PanFrontPart = PanFrontImageUri?.let {
            saveImageToCache(this, it, "PanFrontImage")
        }


        var registationRequest = CustomerKitRequest(
            mode = "INSERT",
            firstName = CustFirstName ?: "",
            middleName = CustMiddleName ?: "",
            lastName = CustLastName ?: "",
            primaryMobileNumber = CustPrimaryMobileNumber ?: "",
            primaryOTP = CustPrimaryOTP ?: "",
            primaryMobileVerified = CustPrimaryMobileVerified ?: "",
            alternateMobileNumber = CustAlternateMobileNumber ?: "",
            alternateMobileOTP = CustAlternateMobileOTP ?: "",
            pAlternateMobileVerified = CustAlternateMobileVerified ?: "",
            eMailID = CusteMailID ?: "",
            flatNo = CustFlatNo ?: "",
            aearSector = CustAreaSector ?: "",
            pinCode = CustPinCode ?: "",
            currentAddress = CustCurrentAddress ?: "",
            stateName = CustStateName ?: "",
            cityName = CustCityName ?: "",
            country = CustCountry ?: "India",
            aadharNumber = AadharNumber ?: "",
            aadharNumberVerified = AadharVerified ?: "",
            panNumber = PanNumber ?: "",
            panNumberVerified = PanNumberVerified ?: "",
            brandName = BrandName ?: "",
            modelName = ModelName ?: "",
            modelVariant = ModelVarient ?: "",
            color = ModelColor ?: "",
            sellingPrice = ConstantClass.SellingPrice ?: "",
            downPayment = downPayment,
            tenure = Tenure ?: "",
            emiAmount = EmiAmount ?: "",
            imeiNumber1 = ImeiNumber1 ?: "",
            imeiNumber2 = ImeiNumber2 ?: "",
            accountNumber = AccountNumber ?: "",
            bankIFSCCode = BankIFSCCode ?: "",
            bankName = BankName ?: "",
            accountType = AccountType ?: "",
            branchName = BranchName ?: "",
            refName = RefName ?: "",
            refRelationShip = RefRelationShip ?: "",
            refmobileNo = RefmobileNo ?: "",
            refAddress = RefAddress ?: "",
            debitOrCreditCard = "",
            upiMandate = "yes",
            createdBy = createdBy,
            membershipfees = membershipAmt,
            retailercode = retailercode,
            cibilScore = userScore.toString(),
            isAggrementVerified = isAggrementVerified,
            IsRetailerAggrementVerified = IsRetailerAggrementVerified,
            custPhoto_File = custPhotoPart?: null,
            imeiNumber1_SealPhotoPath = imei1SealPart?: null,
            imeiNumber2_SealPhotoPath = imei2SealPart?: null,
            imeiNumber_PhotoPath = imeiPhotoPart?: null,
            invoive_Path = invoicePart?: null,
            aadharFront_Path = aadharFrontPart?: null,
            aadharBack_Path = aadharBackPart?: null,
            panFront_Path = PanFrontPart?: null
        )

        Log.d("RegistationRequest", Gson().toJson(registationRequest))
        viewModel.getCustomerKitRequest(registationRequest).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                ConstantClass.dialog.dismiss()
                                if (response!!.statuss!!.toLowerCase().equals("success", ignoreCase = true)) {
                                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                                    customerCode = response.customerCode!!
                                    binding.validateKeyLayout.visibility = View.VISIBLE
                                    binding.nextlayout.visibility = View.GONE
                                }
                                else {
                                    binding.validateKeyLayout.visibility = View.VISIBLE
                                    binding.nextlayout.visibility = View.GONE
                                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
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


    private fun handleApiError(responseCode: Int, errorBody: String?) {
        if (ConstantClass.dialog?.isShowing == true) {
            ConstantClass.dialog.dismiss()
        }
        binding.nextlayout.isEnabled= true

        val message = when (responseCode) {
            400 -> "Bad request. Please check entered data with code 400."
            401 -> "Session expired. Please login again with code 401."
            403 -> "You are not authorized to perform this action with code 403."
            404 -> "Service not found. Please try again later with code 404."
            500 -> "Server error. Please try after some time with code 500."
            else -> "Something went wrong. Please try again."
        }

        Log.e("API_ERROR", "Code: $responseCode Body: $errorBody")

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    fun hitApiForRetailerCreatedLoan(requset: LoanCreatedReq) {

        Log.d("customerReq", Gson().toJson(requset))

        viewModel.getRetailerLoanCreatedReq(requset).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("customerres", Gson().toJson(response))

                                if (response.status?.toLowerCase().equals(ConstantClass.LoanSuccessStatus)) {
                                    loaneCode = response.data!!.loanCode!!
                                    FirstName = CustFirstName
                                    MiddleName = CustMiddleName
                                    LastName = CustLastName
                                    CustomerCodeForEnach = response.data!!.customerCode!!
                                    LoanCodeForEnach = response.data!!.loanCode!!
                                    RetailerCodeForEnach = response.data!!.retailerCode!!

                                    LoanStartDate = response.data.startDate!!
                                    LoanEndDate = response.data.endDate!!
                                    val emiAmount = EmiAmount.toDouble().roundToInt()

                                    if(BankIFSCCode.trim().isNotEmpty() &&ConstantClass.AccountHolderName.trim().isNotEmpty() && AccountType.trim().isNotEmpty()){
                                        val request = EMandateRequest(
                                            categoryID = 7,
                                            collectionAmount = emiAmount,
                                            collectCollectionUntilCancle = false,
                                            seqType = "RCUR",
                                            iFSCCode = BankIFSCCode,
                                            frequncy = "MNTH",
                                            registrationID = ConstantClass.PAN_VERIFICATION_REGISTRATION_ID,
                                            accountHolderName = ConstantClass.AccountHolderName,
                                            finalCollectionDate = LoanEndDate,
                                            loanNo = loaneCode,
                                            accountType = AccountType,
                                            emailAddress = CusteMailID,
                                            firstCollectionDate = LoanStartDate,
                                            mobileNumber = CustPrimaryMobileNumber,
                                            bankAccountNumberConfirmation = AccountNumber,
                                            addIn2 = BranchAddress,
                                            addIn3 = "",
                                            debitType = true,
                                            teleNumber = "",
                                            authType = "",
                                            bankID = BankID,
                                            bankAccountNumber = AccountNumber
                                        )
                                        hitApiForEnach(request,false)
                                    }
                                    else{
                                        startActivity(Intent(this@QRCodePage, CongratulationPage::class.java))
                                        clearData()
                                        finish()
                                    }

                                } else {
                                    if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                                        ConstantClass.dialog.dismiss()
                                    }
                                }

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

                    }

                }
            }
        }
        
    }



    fun String.toRequestBody(): RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), this)


    fun clearData() {
        CustFirstName = ""
        CustMiddleName = ""
        CustLastName = ""
        CustPrimaryMobileNumber = ""
        CustPrimaryOTP = ""
        CustPrimaryMobileVerified = ""
        CustAlternateMobileNumber = ""
        CustAlternateMobileOTP = ""
        CustAlternateMobileVerified = ""
        CusteMailID = ""
        CustFlatNo = ""
        CustAreaSector = ""
        CustPinCode = ""
        CustCurrentAddress = ""
        CustStateName = ""
        CustCityName = ""
        CustCountry = ""
        LoanStartDate = ""
        LoanEndDate = ""

        AadharNumber = ""
        PanNumber = ""

        BrandName = ""
        ConstantClass.ModelName = ""
        ConstantClass.ModelVarient = ""
        ConstantClass.ModelColor = ""
        loaneCode = ""
        ConstantClass.SellingPrice = ""
        ConstantClass.DownPayment = ""
        ConstantClass.Tenure = ""

        EmiAmount = ""

        ImeiNumber1 = ""
        ImeiNumber2 = ""

        AccountNumber = ""
        BankIFSCCode = ""
        BankName = ""
        AccountType = ""
        BranchName = ""

        RefName = ""
        RefRelationShip = ""
        RefmobileNo = ""
        RefAddress = ""
        ClickOnCardLowCibilScore = ""
        ConstantClass.ClickOnCardDashboard = ""
        CustCode = ""
        CibilResponse = ""
        userScore = 0f
        PanResponse = ""
        PanNumberVerified = ""
        PanNumber = ""
        AadharVerified = ""
        AadharNumber = ""
        CustPrimaryOTP = ""
        CustCode = ""
        iisAggrementVerified = false


    }


    fun hitApiForMemberShipFee() {

        val startTime = System.currentTimeMillis()
        var req = GetIsEligibleLoanReq(
            panNumber = PanNumber,
            aadharNumber = ""
        )

        Log.d("checkMemberShipReq", Gson().toJson(req))

        viewModel.getgetMemberShipReqeReq(req).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                Log.d("API_TIME", "Response Time: ${System.currentTimeMillis() - startTime} ms")
                                val response = users?.body()
                                if (response != null) {
                                    Log.d("PanVerificationResp", Gson().toJson(response))
                                    if (response!!.statuss.equals("True")) {
                                        ConstantClass.dialog.dismiss()
                                        binding.membershipfee.text = "₹ ".plus(response.membershipFee?.toDouble())
                                        Log.d("membership", ":".plus(response.membershipFee))
                                        binding.username.text = CustFirstName.plus(" ").plus(CustLastName)
                                        binding.customerimage.setImageURI(CustPhotoPath)
                                        binding.downpayment.text = "₹ ".plus(DownPayment)
                                        val processingFee = (ToBePaidAmount.toDoubleOrNull() ?: 0.0) - (DownPayment.toDoubleOrNull() ?: 0.0)
                                        binding.processingfee.text = "₹ ${String.format("%.2f", processingFee)}"

                                        val amount = ToBePaidAmount.toDoubleOrNull() ?: 0.0
                                        Log.d("ToBePaidAmount", ":".plus(amount))
                                        val membership = response.membershipFee?.toDouble() ?: 0.0

                                        val totalamount = amount + membership

                                        binding.clientcode.text = "₹ ${String.format("%.2f", totalamount)}"
                                        Log.d("totalamt", ":".plus(totalamount))
                                        downPayment = DownPayment // downpayment+processing
                                        membershipAmt = "$membership"
                                    }
                                    else {
                                        ConstantClass.dialog.dismiss()
                                        Log.d("API_TIME", "Failed after: ${System.currentTimeMillis() - startTime} ms")
                                        finish()
                                    }
                                }
                                else {
                                    ConstantClass.dialog.dismiss()
                                    hitApiForMemberShipFee()
                                }

                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                        Log.d("API_TIME", "Failed after: ${System.currentTimeMillis() - startTime} ms")
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }
                }
            }
        }

    }


    fun hitApiForDownloadAppUrlLinkQR(qrCodeProvising: ImageView, progressBar: ProgressBar) {
        lifecycleScope.launch {

            progressBar.visibility = View.VISIBLE
            qrCodeProvising.visibility = View.GONE

            try {

                val bitmap = withContext(Dispatchers.IO) {

                    val response = RetrofitClient.apiInterface.getApkUrlLink()

                    if (response!!.isSuccessful && response.body() != null) {

                        response.body()!!.byteStream().use { inputStream ->
                            BitmapFactory.decodeStream(inputStream)
                        }

                    } else {
                        null
                    }
                }

                bitmap?.let {
                    qrCodeProvising.setImageBitmap(it)
                }

            } catch (e: Exception) {

                e.printStackTrace()

            } finally {

                progressBar.visibility = View.GONE
                qrCodeProvising.visibility = View.VISIBLE
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
                                ConstantClass.checkActiveStatusAndLogout(
                                    this@QRCodePage,
                                    response.status,
                                    preference
                                )
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
        viewModel.getSessionExpiredReq(request).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("validateresp", Gson().toJson(response))
                                if (response.status == 0) {
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
                                val intent = Intent(this@QRCodePage, ChooseYourRolePage::class.java)
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


    fun hitApiForValidateKey() {
        var keyvalidatereq = ValidateAccessKeyReq(
            apiacessKey = binding.accesstoken.text.toString().trim()
        )

        Log.d("keyvalidatereq", Gson().toJson(keyvalidatereq))

        viewModel.getAccessKeyForValidateAPKReq(keyvalidatereq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {

                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("LoginResponse", Gson().toJson(response))
                                ConstantClass.dialog.dismiss()

                                if (response.success!! && response.statusCode == 200) {
                                    binding.accesstoken.isEnabled= false
                                    binding.verifykeyicon.visibility= View.VISIBLE
                                    binding.validatekeylayout.visibility = View.GONE

                                    if(!CheckOnlineOrOffline.equals(ConstantClass.kit)){
                                        binding.LoanCreatelayout.visibility = View.VISIBLE
                                    }
                                    else{
                                        FirstName = CustFirstName
                                        MiddleName = CustMiddleName
                                        LastName = CustLastName
                                        CongratulationPage.customerCode =  customerCode
                                        startActivity(Intent(this@QRCodePage, CongratulationPage::class.java))
                                        clearData()
                                        finish()
                                    }

                                }
                                else {
                                    binding.accesstoken.isEnabled= true
                                    binding.verifykeyicon.visibility= View.GONE
                                    binding.validateKeyLayout.visibility = View.VISIBLE
                                    binding.LoanCreatelayout.visibility = View.GONE
                                }

                                Toast.makeText(this@QRCodePage, response.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                        binding.validatekeylayout.visibility = View.VISIBLE
                        binding.accesstoken.isEnabled = true
                        binding.nextlayout.visibility = View.GONE
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }
                }

            }

        }

    }


    fun hitApiForEnach(request: EMandateRequest,check: Boolean) {
        Log.d("eManadateReq", Gson().toJson(request))

        panViewModel.getEMandateRequestReq(request).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                Log.d("eMandateRes", Gson().toJson(response))

                                if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                                    ConstantClass.dialog.dismiss()
                                }

                                if (response!!.data?.customer != null) {
                                    webUrl = response!!.data!!.url
                                    startActivity(Intent(this@QRCodePage, RetailerEMandateVerifyPage::class.java))
                                }
                                else {
                                    ConstantClass.dialog.dismiss()
                                    isEmandateVerified= "No"
                                    isEnachCancelled = true
                                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                                }

                                if(!isEmandateVerified.isNullOrBlank()){
                                    var request = EnachDateUploadReq(
                                        isEmandateVerified = isEmandateVerified,
                                        emAccountType = AccountType,
                                        isPannydropVerified = isPannydropVerified,
                                        emAccountNumber = AccountNumber,
                                        customerCode = CustomerCodeForEnach,
                                        retailerCode= RetailerCodeForEnach,
                                        loanCode= loaneCode,
                                        emBankName=BankName,
                                        emIfscCode =BankIFSCCode
                                    )

                                    hitApiForUploadEnachMandateDataResponse(request)
                                }

                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
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
                       if(check){
                           ConstantClass.OpenLoader(this)
                       }
                    }

                }

            }

        }

    }


    fun hitApiForCheckLoanCharge(loancreatedreq: LoanCreatedReq){

        var request = LoanChargeReq(
            registrationID = PENNYDROP_REGISTRATION_ID,
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, "")
        )

        panViewModel.loanApplyChargesReq(request).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                Log.d("LoanChargeRes", Gson().toJson(response))

                                if(response!!.status!!.lowercase().equals("true")){
                                    hitApiForRetailerCreatedLoan(loancreatedreq)
                                }
                                else {
                                    if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                                        ConstantClass.dialog.dismiss()
                                    }
                                    Toast.makeText(this,response!!.message.toString(), Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        // ✅ Print the full error details
                        Log.e("API_ERROR", "Status: ERROR")
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

    

    fun  hitApiForUploadEnachMandateDataResponse(request:EnachDateUploadReq){

        Log.d("EmandateUploadreq", Gson().toJson(request))

        viewModel.UpdateEmandateDetails(request).observe(this){
            resources ->
            resources.let {

                when(it.apiStatus){
                    ApiStatus.SUCCESS ->{
                        it.data.let { users ->
                            users!!.body().let { response ->
                                Log.d("EmandateUploadRes", Gson().toJson(response))
                            }
                        }

                    }
                    ApiStatus.ERROR ->{
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

                    }
                }
            }
        }

    }


    /* fun hitApiForEMandateStatus(request: ENachStatusReq) {
      Log.d("eManadateStatusReq", Gson().toJson(request))

      panViewModel.geteMandateSatusRequest(request).observe(this) { resources ->
          resources.let {
              when (it.apiStatus) {
                  ApiStatus.SUCCESS -> {
                      it.data.let { users ->
                          users!!.body().let { response ->
                              Log.d("eMandateStatusRes", Gson().toJson(response))
                              if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                                  ConstantClass.dialog.dismiss()
                              }
                              if (response!!.statusCode.equals("NP000")) {
                                  CheckOnlineOrOffline =""
                                  Toast.makeText(this, "ENach Mandate is Active", Toast.LENGTH_SHORT).show()
                                  startActivity(Intent(this@QRCodePage, CongratulationPage::class.java))
                                  clearData()
                                  finish()

                              }
                              else {
                                  Toast.makeText(this, "ENach Mandate is not Active", Toast.LENGTH_SHORT).show()
                              }
                          }

                      }

                  }

                  ApiStatus.ERROR -> {
                      ConstantClass.dialog.dismiss()
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

                  }

              }

          }

      }
  }*/

}
