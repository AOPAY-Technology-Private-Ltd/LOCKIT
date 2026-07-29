package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
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
import com.bosandroidapp.aopaykit.databinding.ActivityAadharCardVerificationPageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadharBackImageUri
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadharFrontImageUri
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadharNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.CheckOnlineOrOffline
import com.bosandroidapp.aopaykit.constant.ConstantClass.saveImageToCache
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetIsEligibleLoanReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import java.io.File

class AadharCardVerificationPage : BaseActivity() {
    lateinit var binding : ActivityAadharCardVerificationPageBinding
    private val CAMERA_REQUEST_CODE_FRONT = 1001
    private  var photoFrontUri: Uri? = null
    private  var photoBackUri: Uri?=null
    var front: Boolean = false
    var back: Boolean = false
    lateinit var dialog : Dialog
    lateinit var countDownTimer: CountDownTimer

    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference


    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            if(front){
                // Handle the photoUri, e.g., show image in ImageView
                binding.frontcardimage.visibility= View.VISIBLE
                binding.frontcardicon.visibility= View.GONE
                binding.frontcardimage.setImageURI(photoFrontUri)
                binding.uploadtextfront.text= "Re- Upload"
                // binding.frontcardimage.setImageURI(Uri.fromFile(saveImageToCache(this@AadharCardVerificationPage,photoFrontUri!!,"AadharFrontImage")))
                Log.d("ImageCache", "File saved at: ${saveImageToCache(this@AadharCardVerificationPage,photoFrontUri!!,"AadharFrontImage")!!.absolutePath}, size: ${saveImageToCache(this@AadharCardVerificationPage,photoFrontUri!!,"AadharFrontImage")!!.length()} bytes")

            }
            else{
                // Handle the photoUri, e.g., show image in ImageView
                binding.backcardimage.visibility= View.VISIBLE
                binding.camerabackicon.visibility= View.GONE
                binding.backcardimage.setImageURI(photoBackUri)
                binding.uploadtextback.text= "Re- Upload"
            }

        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAadharCardVerificationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)

        setOnClickListner()

        setDataOnUI()

    }

    override fun onResume() {
        super.onResume()

        hitApiForLogin()
    }

    fun setDataOnUI(){

    }


    fun setOnClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.uploadtextfront.setOnClickListener {
            front= true
            back = false
            checkCameraPermissionAndOpenCamera()
        }

        binding.uploadtextback.setOnClickListener {
            front= false
            back = true
            checkCameraPermissionAndOpenCamera()
        }

        binding.verifybuttonlayout.setOnClickListener {

            if(CheckOnlineOrOffline.equals(ConstantClass.kit)){
                AadharFrontImageUri = null
                AadharBackImageUri = null
                AadharNumber = ""
                ConstantClass.AadharVerified = "no"
                val intent = Intent(this, NewCustomerRegistrationPage::class.java)
                startActivity(intent)
            }
            else{
                val aadharNumber = binding.aadharnumberEdittxt.text.toString().trim()

                // Aadhaar validation
                if (aadharNumber.isBlank() || aadharNumber.length != 12 || !aadharNumber.all { it.isDigit() }) {
                    Toast.makeText(this, "Enter a valid 12-digit Aadhaar number", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Image URI validation
                if (photoFrontUri == null || photoBackUri == null) {
                    Toast.makeText(this, "Please upload both front and back Aadhaar images", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                hitApiForCheckIsEligibleOrNotForLoan(aadharNumber)
            }

        }



    }


    fun hitApiForCheckIsEligibleOrNotForLoan(aadharNumber: String){

        var eligiblereq = GetIsEligibleLoanReq(
            panNumber = "",
            aadharNumber = aadharNumber
        )
        Log.d("IsEligibleReq", Gson().toJson(eligiblereq))

        viewModel.getLoanEligibleReq(eligiblereq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                Log.d("CheckEligibleResp", Gson().toJson(response))
                                if(response!!.statuss.equals("True")){
                                    AadharFrontImageUri = photoFrontUri
                                    AadharBackImageUri = photoBackUri
                                    AadharNumber = aadharNumber
                                    ConstantClass.AadharVerified = "no"
                                    val intent = Intent(this, NewCustomerRegistrationPage::class.java)
                                    startActivity(intent)
                                }
                                else{
                                    ConstantClass.dialog.dismiss()
                                    OpenPopUpForVAlert()
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

        txt.text="Your loan application was created, but you must validate your Aadhaar  again since 90 days have passed"

        cancel.setOnClickListener {
            finish()
        }

        dialog.show()

    }


    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }


    fun clickCameraForUploadDocument(){
        val photoFile = createImageFile()
        if(front){
            photoFrontUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            cameraLauncher.launch(photoFrontUri!!)
        }else{
            photoBackUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            cameraLauncher.launch(photoBackUri!!)
        }

    }


    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            clickCameraForUploadDocument()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE_FRONT)
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
                                ConstantClass.checkActiveStatusAndLogout(this@AadharCardVerificationPage, response.status, preference)
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
            preference.getStringValue(ConstantClass.DEVICEID,""),
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
                                val intent = Intent(this@AadharCardVerificationPage, ChooseYourRolePage::class.java)
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