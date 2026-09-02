package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityPanCardVerificationPageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.CheckOnlineOrOffline
import com.bosandroidapp.aopaykit.constant.ConstantClass.ENTEREDCUSTOMERDOB
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanAddress
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanBuilding
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanCity
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanCountry
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanDOB
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanEmailId
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanFirstName
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanFrontImageUri
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanLastName
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanMiddleName
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanMobileNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanNumberVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanResponse
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanState
import com.bosandroidapp.aopaykit.constant.ConstantClass.saveImageToCache
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetIsEligibleLoanReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.PanVerificationReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.repository.PanRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.ui.viewmodel.PanViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PanCardVerificationPage : BaseActivity() {
    lateinit var binding: ActivityPanCardVerificationPageBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var panViewModel: PanViewModel
    lateinit var preference: SharedPreference
    private val CAMERA_REQUEST_CODE_FRONT = 1001
    private var photoFrontUri: Uri? = null
    lateinit var dialog: Dialog
    var checkPanNumber:Boolean = false


    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Handle the photoUri, e.g., show image in ImageView
                binding.frontcardimage.visibility = View.VISIBLE
                binding.frontcardicon.visibility = View.GONE
                binding.frontcardimage.setImageURI(photoFrontUri)
                binding.uploadtextfront.text = "Re- Upload"
                // binding.frontcardimage.setImageURI(Uri.fromFile(saveImageToCache(this@AadharCardVerificationPage,photoFrontUri!!,"AadharFrontImage")))
                Log.d(
                    "ImageCache",
                    "File saved at: ${
                        saveImageToCache(
                            this@PanCardVerificationPage,
                            photoFrontUri!!,
                            "PanFrontImage"
                        )!!.absolutePath
                    }, size: ${
                        saveImageToCache(
                            this@PanCardVerificationPage,
                            photoFrontUri!!,
                            "PanFrontImage"
                        )!!.length()
                    } bytes"
                )

            }

            else {
                photoFrontUri = null
            }


        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPanCardVerificationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        panViewModel = ViewModelProvider(this, com.bosandroidapp.aopaykit.data.viewModelFactory.PanViewModelFactory(PanRepository(RetrofitClient.apiInterfacePAN)))[PanViewModel::class.java]

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

        preference = SharedPreference(this)

        OnClickListner()
        setView()


    }

    override fun onResume() {
        super.onResume()

        hitApiForLogin()
    }

    fun setView(){
        binding.pannumber.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.pannumber.error = "Emoji not allowed"
                        checkPanNumber= false
                    }
                    else{
                        checkPanNumber= true
                    }
                }

            }

        })
    }


    fun OnClickListner() {

        if (ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.online)) {
            binding.photolayout.visibility = View.GONE
        }
        else {
            binding.photolayout.visibility = View.VISIBLE
        }

        if(ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.kit)){
            binding.skipbutton.visibility = View.VISIBLE
        }else{
            binding.skipbutton.visibility = View.GONE
        }

        binding.back.setOnClickListener {
           onBackPressed()
        }

        binding.verifybuttonlayout.setOnClickListener {

                var panNumber = binding.pannumber.text.toString()
                // PAN validation (Regex: 5 letters, 4 digits, 1 letter)
                val panRegex = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}")

            if (panNumber.isBlank() && CheckOnlineOrOffline.equals(ConstantClass.kit)) {
                PanNumber = ""
                PanNumberVerified = "no"
                PanFrontImageUri = null
                startActivity(
                    Intent(
                        this@PanCardVerificationPage,
                        AadharCardVerificationPage::class.java
                    )
                )
                return@setOnClickListener
            }

                if (panNumber.isBlank() || !panRegex.matches(panNumber.uppercase())) {
                    Toast.makeText(this, "Enter a valid PAN number (e.g., ABCDE1234F)", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if(!checkPanNumber){
                    Toast.makeText(this, "Enter a valid PAN number ", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (CheckOnlineOrOffline.equals(ConstantClass.offline)||CheckOnlineOrOffline.equals(ConstantClass.kit)) {
                    // Image URI validation
                    if (photoFrontUri == null || photoFrontUri == null) {
                        Toast.makeText(this, "Please upload  Pan image", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                }

                hitApiForCheckIsEligibleOrNotForLoan(panNumber)



        }


        binding.uploadtextfront.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }

        binding.skipbutton.setOnClickListener {
            startActivity(Intent(this@PanCardVerificationPage, AadharCardVerificationPage::class.java))
        }

    }


    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            clickCameraForUploadDocument()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE_FRONT)
        }
    }



    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }


    fun clickCameraForUploadDocument() {
        val photoFile = createImageFile()
        photoFrontUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
        cameraLauncher.launch(photoFrontUri!!)
    }



    fun hitApiForPanVerification(pannumber: String) {
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        var panverificationreq = PanVerificationReq(
            panNumber = pannumber,
           /* firstName = firstName,*/
            registrationId = ConstantClass.PAN_VERIFICATION_REGISTRATION_ID,
            clientCode = preference.getStringValue(ConstantClass.ClientCode, "")
        )
        Log.d("PanVerificationreq", Gson().toJson(panverificationreq))

        panViewModel.getPanVerificationReq(panverificationreq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        val response = it.data?.body()
                        if (it.data?.isSuccessful == true && response != null) {
                            ConstantClass.dialog.dismiss()
                            Log.d("PanVerificationResp", Gson().toJson(response))
                            if (response!!.httpResponseCode == 203) {
                                Toast.makeText(this@PanCardVerificationPage, "Please enter valid pan number!!", Toast.LENGTH_SHORT).show()
                            }
                            if (response!!.httpResponseCode == 205) {
                                Toast.makeText(this@PanCardVerificationPage, "Please enter valid pan number!!", Toast.LENGTH_SHORT).show()
                            }

                            if (response!!.httpResponseCode == 0) {
                                Toast.makeText(this@PanCardVerificationPage, response.message, Toast.LENGTH_SHORT).show()
                            }

                            if (response!!.httpResponseCode == 200) {
                                var  DOB = response.result!!.dob!! // 10/07/1997 dd/mm/yyyy
                                val apiSdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                val apiDate: Date = apiSdf.parse(DOB)!!

                                val formattedApiDob = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(apiDate)

                                if (formattedApiDob != ENTEREDCUSTOMERDOB) {
                                    Toast.makeText(this@PanCardVerificationPage,"DOB does not match",Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                else {
                                    PanNumber = response.result!!.pan!!
                                    PanDOB = formattedApiDob
                                    PanFirstName = response.result!!.firstName!!
                                    PanMiddleName = response.result!!.middleName!!
                                    PanLastName = response.result!!.lastName!!
                                    PanMobileNumber = response.result!!.mobile!!
                                    PanEmailId = response.result!!.email!!
                                    PanState = response.result!!.address!!.state!!
                                    PanCity = response.result!!.address!!.city!!
                                    PanCountry = response.result!!.address!!.country!!
                                    PanBuilding = "${response.result!!. address!!.buildingName!!}"
                                    PanAddress = "${response.result!!.address!!.streetName!!}${response.result!!. address!!.locality!!}".trim()
                                    PanNumberVerified = "yes"
                                    PanFrontImageUri = null
                                    PanResponse = Gson().toJson(response)
                                    Log.d("lastname",response.result!!.lastName!!)
                                    finish()
                                }

                            }
                            else {
                                Toast.makeText(this@PanCardVerificationPage, "Please enter valid pan number!!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            ConstantClass.handleApiError(this@PanCardVerificationPage, it.data?.code() ?: 0)
                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.handleApiFailure(this@PanCardVerificationPage, it.message)
                    }

                    ApiStatus.LOADING -> {
                       // ConstantClass.OpenLoader(this)
                    }

                }

            }

        }

    }


    fun hitApiForCheckIsEligibleOrNotForLoan(pannumber: String){

        var eligiblereq = GetIsEligibleLoanReq(
            panNumber = pannumber,
            aadharNumber = "",
            clientCode = preference.getStringValue(ConstantClass.ClientCode, "")
        )
        Log.d("IsEligibleReq", Gson().toJson(eligiblereq))

        viewModel.getLoanEligibleReq(eligiblereq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        val response = it.data?.body()
                        if (it.data?.isSuccessful == true && response != null) {
                            Log.d("CheckEligibleResp", Gson().toJson(response))

                            if(response!!.statuss.equals("True")){
                                if (CheckOnlineOrOffline.equals(ConstantClass.online)) {
                                    hitApiForPanVerification(pannumber)
                                }
                                else {
                                    ConstantClass.dialog.dismiss()
                                    PanNumber = pannumber
                                    PanNumberVerified = "no"
                                    PanFrontImageUri = photoFrontUri
                                    if(CheckOnlineOrOffline.equals(ConstantClass.kit)){
                                        startActivity(Intent(this@PanCardVerificationPage, AadharCardVerificationPage::class.java))
                                    }else{
                                        finish()
                                    }

                                }

                            }
                            else{
                                ConstantClass.dialog.dismiss()
                                OpenPopUpForVAlert()
                            }
                        } else {
                            ConstantClass.handleApiError(this@PanCardVerificationPage, it.data?.code() ?: 0)
                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.handleApiFailure(this@PanCardVerificationPage, it.message)
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }
                }
            }
        }

    }


    @SuppressLint("SetTextI18n")
    fun OpenPopUpForVAlert(){
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

        txt.text="Your loan application was created, but you must validate your PAN  again since 90 days have passed"

        cancel.setOnClickListener {
            finish()
        }

        dialog.show()

    }


    override fun onBackPressed() {
        PanNumber=""
        PanNumberVerified=""
        PanFrontImageUri=null
        super.onBackPressed()
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



    fun hitApiForLogin() {

        var sessionOutReq = SessionOutReq(
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
            clientCode = preference.getStringValue(ConstantClass.ClientCode, "")
        )

        Log.d("SessionOutReq", Gson().toJson(sessionOutReq))

        viewModel.getSessionReq(sessionOutReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        val response = it.data?.body()
                        if (it.data?.isSuccessful == true && response != null) {
                            Log.d("SessionOutResponse", Gson().toJson(response))
                            if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                                ConstantClass.dialog.dismiss()
                            }
                            ConstantClass.checkActiveStatusAndLogout(this@PanCardVerificationPage, response.status, preference)
                        } else {
                            ConstantClass.handleApiError(this@PanCardVerificationPage, it.data?.code() ?: 0)
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.handleApiFailure(this@PanCardVerificationPage, it.message)
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
                        val response = it.data?.body()
                        if (it.data?.isSuccessful == true && response != null) {
                            Log.d("validateresp", Gson().toJson(response))
                            if(response.status==0){
                                hitApiForRetailerLogout()
                            }
                        } else {
                            ConstantClass.handleApiError(this@PanCardVerificationPage, it.data?.code() ?: 0)
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.handleApiFailure(this@PanCardVerificationPage, it.message)
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
                        val response = it.data?.body()
                        if (it.data?.isSuccessful == true && response != null) {
                            Log.d("LogoutResponse", Gson().toJson(response))
                            preference.setBooleanValue(ConstantClass.LoggedIn, false)
                            preference.setStringValue(ConstantClass.LoginType, "")
                            ConstantClass.ClickOnCardDashboard = ""
                            val intent = Intent(this@PanCardVerificationPage, ChooseYourRolePage::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            ConstantClass.handleApiError(this@PanCardVerificationPage, it.data?.code() ?: 0)
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.handleApiFailure(this@PanCardVerificationPage, it.message)
                    }

                    ApiStatus.LOADING -> {

                    }
                }
            }
        }

    }


}