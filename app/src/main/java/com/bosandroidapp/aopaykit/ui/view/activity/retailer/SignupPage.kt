package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.databinding.ActivitySignupPageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.Retailer
import com.bosandroidapp.aopaykit.constant.ConstantClass.disableCopyPaste
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.constant.ConstantClass.loginType
import com.bosandroidapp.aopaykit.constant.ConstantClass.saveImageToCache
import com.bosandroidapp.aopaykit.data.model.loginsignup.RegistrationReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.slideshow.activity.LoginPage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream

class SignupPage : AppCompatActivity() {
    lateinit var binding : ActivitySignupPageBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference
    lateinit var dialog: Dialog
    var checkFirstNameValidation : Boolean = false
    var checkLastNameValidation : Boolean = false
    private  val CAMERA_REQUEST_CODE_FRONT = 1001
    private  var profilePhotoUri: Uri? = null
    private  var aadhaarBackhotoUri: Uri? = null
    private  var aadhaarFronthotoUri: Uri? = null
    private  var pancardphotoUri: Uri?=null
    private  var storePhotoUri: Uri? = null
    private  var companyCodePhotoUri: Uri?=null
    private  var chequePhotoUri: Uri?=null
    var customerImagePath: String? = ""
    var aadhaarFrontImagePath: String? = ""
    var aadhaarBackImagePath: String? = ""
    var panImagePath: String? = ""
    var storeImagePath: String? = ""
    var companydocImagePath: String? = ""
    var cancelChequeImagePath: String? = ""
    private var currentCaptureMode: String? = null // "PROFILE", "AADHAAR_FRONT", etc.


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("capture_mode", currentCaptureMode)
        outState.putParcelable("profile_uri", profilePhotoUri)
        // Save other URIs as well
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentCaptureMode = savedInstanceState.getString("capture_mode")
        profilePhotoUri = savedInstanceState.getParcelable("profile_uri")
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // We use currentCaptureMode because booleans can reset to false if the OS kills the app in the background
            when (currentCaptureMode) {

                "PROFILE" -> {
                    profilePhotoUri?.let {
                        binding.profileimage.visibility = View.VISIBLE
                        binding.lockimage.visibility = View.GONE
                        binding.imagenotuploaded.visibility = View.GONE

                        Glide.with(this)
                            .load(profilePhotoUri)
                            .centerCrop()
                            .into(binding.profileimage)

                        profilePhotoUri?.let { uri ->
                            customerImagePath = compressImageAndGetPath(uri)
                        }
                    }
                }

                "AADHAAR_FRONT" -> {
                    aadhaarFronthotoUri?.let {
                        binding.aadhaarfront.visibility = View.VISIBLE
                        binding.aadhaarfronticon.visibility = View.GONE
                        binding.aadhaarfrontnotuploaded.visibility = View.GONE

                        Glide.with(this)
                            .load(aadhaarFronthotoUri)
                            .centerCrop()
                            .into(binding.aadhaarfront)

                        aadhaarFronthotoUri?.let { uri ->
                            aadhaarFrontImagePath = compressImageAndGetPath(uri)
                        }
                    }
                }

                "AADHAAR_BACK" -> {
                    aadhaarBackhotoUri?.let {
                        binding.aadhaarback.visibility = View.VISIBLE
                        binding.aadhaarbackicon.visibility = View.GONE
                        binding.aadhaarbanknotuploaded.visibility = View.GONE
                        Glide.with(this)
                            .load(aadhaarBackhotoUri)
                            .centerCrop()
                            .into(binding.aadhaarback)

                        aadhaarBackhotoUri?.let { uri ->
                            aadhaarBackImagePath = compressImageAndGetPath(uri)
                        }
                    }
                }

                "PANCARD" -> {
                    pancardphotoUri?.let {
                        binding.pancardPhoto.visibility = View.VISIBLE
                        binding.pancardicon.visibility = View.GONE
                        binding.pancardfrontnotuploaded.visibility = View.GONE
                        Glide.with(this)
                            .load(pancardphotoUri)
                            .centerCrop()
                            .into(binding.pancardPhoto)

                        pancardphotoUri?.let { uri ->
                            panImagePath = compressImageAndGetPath(uri)
                        }
                    }
                }

                "STORE" -> {
                    storePhotoUri?.let {
                        binding.storePhoto.visibility = View.VISIBLE
                        binding.storeicon.visibility = View.GONE
                        binding.storefrontnotuploaded.visibility = View.GONE
                        Glide.with(this)
                            .load(storePhotoUri)
                            .centerCrop()
                            .into(binding.storePhoto)
                        storePhotoUri?.let { uri ->
                            storeImagePath = compressImageAndGetPath(uri)
                        }
                    }
                }

                "COMPANYDOCUMENT" -> {
                    companyCodePhotoUri?.let {
                        binding.companydocumentPhoto.visibility = View.VISIBLE
                        binding.companydocumenticon.visibility = View.GONE
                        binding.companydocumentfrontnotuploaded.visibility = View.GONE
                        Glide.with(this)
                            .load(companyCodePhotoUri)
                            .centerCrop()
                            .into(binding.companydocumentPhoto)
                        companyCodePhotoUri?.let { uri ->
                            companydocImagePath = compressImageAndGetPath(uri)
                        }
                    }
                }

                "CANCELCHEQUE" -> {
                    chequePhotoUri?.let {
                        binding.cancelchequePhoto.visibility = View.VISIBLE
                        binding.cancelchequeicon.visibility = View.GONE
                        binding.cancelchequefrontnotuploaded.visibility = View.GONE
                        Glide.with(this)
                            .load(chequePhotoUri)
                            .centerCrop()
                            .into(binding.cancelchequePhoto)
                        chequePhotoUri?.let { uri ->
                            cancelChequeImagePath = compressImageAndGetPath(uri)
                        }
                    }
                }

            }

        } else {
            // Optional: Handle failure or cancellation
            Log.d("BOS_CAMERA", "Capture failed or cancelled for mode: $currentCaptureMode")
        }
    }

    private fun compressImageAndGetPath(uri: Uri): String {
        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }

        // Define destination for compressed file
        val compressedFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "compressed_${System.currentTimeMillis()}.jpg")
        val out = FileOutputStream(compressedFile)

        // 80 is a good balance between file size and readability for documents
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        out.flush()
        out.close()

        return compressedFile.absolutePath
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        setclickListner()

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)
        DisableCopyPaste()

    }


    fun setclickListner(){

        binding.cameraicon.setOnClickListener {
            openCameraFor("PROFILE")
        }

        binding.aadhaarFronttxt.setOnClickListener {
            openCameraFor("AADHAAR_FRONT")
        }


        binding.aadhaartxt.setOnClickListener {
            openCameraFor("AADHAAR_BACK")

        }

        binding.pancardtxt.setOnClickListener {
            openCameraFor("PANCARD")

        }

        binding.cancelchequetxt.setOnClickListener {
            openCameraFor("CANCELCHEQUE")

        }

        binding.storetxt.setOnClickListener {
            openCameraFor("STORE")

        }

        binding.companydocumenttxt.setOnClickListener {
            openCameraFor("COMPANYDOCUMENT")
        }

        binding.loginText.setOnClickListener {
            finish()
        }

        binding.createaccount.setOnClickListener {
            var firstName = binding.firstName.text.toString().trim()
            var lastName = binding.lastName.text.toString().trim()
            var mobilenumber = binding.mobileNumber.text.toString().trim()
            var emailId = binding.emailId.text.toString().trim()
            var password = binding.password.text.toString().trim()
            var confirmpass = binding.confirmpassword.text.toString().trim()
            var pannumber = binding.panEditText.text.toString().trim()
            var aadharnumber = binding.aadharnumber.text.toString().trim()
            var address = binding.address.text.toString().trim()
            var storeName = binding.storeName.text.toString().trim()
            var storeaddress = binding.storeaddresstxt.text.toString().trim()

            if(validateForm(firstName,lastName,aadharnumber,pannumber,mobilenumber,emailId,address,password,confirmpass,storeName,storeaddress,profilePhotoUri,aadhaarFronthotoUri,aadhaarBackhotoUri,pancardphotoUri,chequePhotoUri,storePhotoUri,companyCodePhotoUri,this@SignupPage)){
              if(isInternetAvailable(this@SignupPage))  {
                  hitApiForRegistration(firstName,lastName,mobilenumber,emailId,password,confirmpass,pannumber,aadharnumber,address,storeName,storeaddress)
              }
              else{
                  Toast.makeText(this,"Please check your internet connection!!",Toast.LENGTH_SHORT).show()
              }

            }

        }

    }


    private fun openCameraFor(mode: String) {
        currentCaptureMode = mode
        checkCameraPermissionAndOpenCamera()
    }


    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            clickCameraForUploadDocument(currentCaptureMode!!)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE_FRONT
            )
        }
    }


    fun clickCameraForUploadDocument(mode: String) {
        val photoFile = createImageFile()
        val providerAuthority = "${packageName}.fileprovider"

        // Initialize the specific URI based on the mode passed from the click listener
        when (mode) {
            "PROFILE" -> {
                profilePhotoUri = FileProvider.getUriForFile(this, providerAuthority, photoFile)
                cameraLauncher.launch(profilePhotoUri!!)
            }
            "AADHAAR_FRONT" -> {
                aadhaarFronthotoUri = FileProvider.getUriForFile(this, providerAuthority, photoFile)
                cameraLauncher.launch(aadhaarFronthotoUri!!)
            }
            "AADHAAR_BACK" -> {
                aadhaarBackhotoUri = FileProvider.getUriForFile(this, providerAuthority, photoFile)
                cameraLauncher.launch(aadhaarBackhotoUri!!)
            }
            "PANCARD" -> {
                pancardphotoUri = FileProvider.getUriForFile(this, providerAuthority, photoFile)
                cameraLauncher.launch(pancardphotoUri!!)
            }
            "CANCELCHEQUE" -> {
                chequePhotoUri = FileProvider.getUriForFile(this, providerAuthority, photoFile)
                cameraLauncher.launch(chequePhotoUri!!)
            }
            "STORE" -> {
                storePhotoUri = FileProvider.getUriForFile(this, providerAuthority, photoFile)
                cameraLauncher.launch(storePhotoUri!!)
            }
            "COMPANYDOCUMENT" -> {
                companyCodePhotoUri = FileProvider.getUriForFile(this, providerAuthority, photoFile)
                cameraLauncher.launch(companyCodePhotoUri!!)
            }
        }
    }


    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }


    fun DisableCopyPaste(){

        binding.firstName.disableCopyPaste()
        binding.lastName.disableCopyPaste()


        binding.firstName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.firstName.error = "Emoji not allowed"
                        checkFirstNameValidation= false
                    }else{
                        checkFirstNameValidation= true
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        binding.lastName.addTextChangedListener(object :TextWatcher{

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.lastName.error = "Emoji not allowed"
                        checkLastNameValidation= false
                    }
                    else{
                        checkLastNameValidation= true
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })


        binding.aadharnumber.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            val newText = StringBuilder(dest).apply {
                replace(dstart, dend, source?.substring(start, end) ?: "")
            }.toString()

            // Block if non-digit characters are present
            if (source != null && source.any { !it.isDigit() }) {
                return@InputFilter ""
            }

            // If user pastes while field is empty, only allow exactly 12 digits
            if (dest.isEmpty() && source != null && source.length > 1) {
                return@InputFilter if (source.length == 12 && source.all { it.isDigit() }) source else ""
            }

            // Prevent length > 12
            if (newText.length > 12) {
                return@InputFilter ""
            }

            null // Accept input
        })


        val panPattern = Regex("^[A-Z]{5}[0-9]{4}[A-Z]{1}$")

        binding.panEditText.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            val newText = StringBuilder(dest).apply {
                replace(dstart, dend, source?.substring(start, end) ?: "")
            }.toString().uppercase() // Always uppercase

            // Block invalid characters
            if (source != null && source.any { !(it.isDigit() || it.isLetter()) }) {
                return@InputFilter ""
            }

            // Force uppercase input
            if (source != null && source.any { it.isLowerCase() }) {
                return@InputFilter source.toString().uppercase()
            }

            // Limit to 10 chars
            if (newText.length > 10) {
                return@InputFilter ""
            }

            // Paste handling
            if (source != null && source.length > 1) {
                return@InputFilter if (panPattern.matches(newText)) newText else ""
            }

            null // Accept valid input
        })


        binding.address.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val allowedPattern = Regex("[A-Za-z0-9 ,./#\\-\\n]+") // \n = allow line breaks
            if (source.isNullOrEmpty()) {
                null // allow deletes
            } else if (allowedPattern.matches(source)) {
                null // allow valid input
            } else {
                "" // block invalid chars (including from paste)
            }
        })


    }


    fun hitApiForRegistration(firstName:String,lastName:String,mobNumber:String,emailId:String,password:String,confrmPassword:String,panNumber:String,aadharNumber:String,address:String,storename:String,storeaddress:String){
        binding.createaccount.isEnabled=false
        val profilePhoto = saveImageToCache(this, profilePhotoUri!!, "profile.jpg")
        val aadhaarfront = saveImageToCache(this, aadhaarFronthotoUri!!, "aadhaarfront.jpg")
        val aadhaarback = saveImageToCache(this,aadhaarBackhotoUri!!, "aadhaarback.jpg")
        val pancard =    saveImageToCache(this, pancardphotoUri!!, "pancard.jpg")
        val storePhoto = saveImageToCache(this, storePhotoUri!!, "storefront.jpg")
        val companydoc = saveImageToCache(this, companyCodePhotoUri!!, "companydoc.jpg")
        val cancelcheque = saveImageToCache(this, chequePhotoUri!!, "cancelcheque.jpg")


        var registationRequest = RegistrationReq(
            firstName = firstName,
            lastName = lastName,
            mobileNumber = mobNumber,
            emailId = emailId,
            password = password,
            confrmpassword = confrmPassword,
            address = address,
            aadharnumber = aadharNumber,
            panNumber = panNumber,
            storeName = storename,
            storeAddress = storeaddress,
            profilePhoto,
            aadhaarfront,
            aadhaarback,
            pancard,
            cancelcheque,
            storePhoto,
            companydoc
        )

        Log.d("RegisReq", Gson().toJson(registationRequest))

        viewModel.getRegistration(registationRequest).observe(this){
            resources->resources.let {
                when(it.apiStatus){
                    ApiStatus.SUCCESS ->{
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                ConstantClass.dialog.dismiss()
                                Log.d("AirPortListResponse", Gson().toJson(response))
                                if(response.statuss){
                                    preference.setStringValue(ConstantClass.CustomerCode, response.customerCode.toString())
                                    preference.setStringValue(ConstantClass.CustomerMobileNumber, response.mobileNumber.toString())
                                    preference.setStringValue(ConstantClass.CustomerEmailID, response.emailID.toString())
                                    Toast.makeText(this, response?.message, Toast.LENGTH_LONG).show()
                                    loginType = Retailer
                                    startActivity(Intent(this, LoginPage::class.java))
                                    finish()
                                    emptyAboveField()
                                }
                                else{
                                    binding.createaccount.isEnabled=true
                                    Toast.makeText(this, response?.message, Toast.LENGTH_LONG).show()
                                }

                            }
                        }
                    }
                    ApiStatus.ERROR -> {
                        binding.createaccount.isEnabled=true
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(this)
                    }

                }
         }
        }

    }


    fun validateForm(firstname:String, lastname:String, aadharNumber: String, panNumber: String, mobile: String, email: String, address: String, password: String, confirmPassword: String,
                     storeName: String, storeAddress: String, profilephotouri: Uri?, aadhaarfronturi: Uri?, aadhaarbackuri: Uri?, panuri: Uri?, cancelchequeuri: Uri?, storephotouri: Uri?, companydocuri: Uri? , context: Context): Boolean {

        if (firstname.isNullOrBlank() ) {
            binding.firstName.error= "Enter first name"
            scrollToView(binding.detaillayout,  binding.firstName)
            Toast.makeText(context, "Enter first name", Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            binding.firstName.error = null
        }

        if(!checkFirstNameValidation){
            Toast.makeText(context, "Enter Valid first name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (lastname.isNullOrBlank() ) {
            binding.lastName.error= "Enter last name"
            scrollToView(binding.detaillayout,  binding.lastName)
            Toast.makeText(context, "Enter last name", Toast.LENGTH_SHORT).show()
            return false
        }
        else{
            binding.lastName.error = null
        }

        if(!checkLastNameValidation){
            Toast.makeText(context, "Enter Valid last name", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!mobile.matches(Regex("^[6-9]\\d{9}$"))) {
            binding.mobileNumber.error = "Please enter a valid 10-digit mobile number"
            scrollToView(binding.detaillayout, binding.mobileNumber)
            return false
        } else {
            binding.mobileNumber.error = null
        }

        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailId.error= "Enter a valid email address"
            scrollToView(binding.detaillayout,  binding.emailId)
            Toast.makeText(context, "Enter a valid email address", Toast.LENGTH_SHORT).show()
            return false
        }
        else{

            binding.emailId.error = null
        }


        if (address.isNullOrBlank() ) {
            binding.address.error= "Please enter address"
            scrollToView(binding.detaillayout,  binding.address)
            Toast.makeText(context, "Please enter address", Toast.LENGTH_SHORT).show()
            return false
        }else{
            binding.address.error = null
        }


        // Aadhaar validation
        if (aadharNumber.isBlank() || aadharNumber.length != 12 || !aadharNumber.all { it.isDigit() }) {
            binding.aadharnumber.error= "Enter a valid 12-digit Aadhaar number"
            scrollToView(binding.detaillayout,  binding.aadharnumber)
            Toast.makeText(context, "Enter a valid 12-digit Aadhaar number", Toast.LENGTH_SHORT).show()
            return false
        }else{
            binding.aadharnumber.error = null
        }


        // PAN validation (Regex: 5 letters, 4 digits, 1 letter)
        val panRegex = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        if (panNumber.isBlank() || !panRegex.matches(panNumber.uppercase())) {
            binding.panEditText.error= "Enter a valid PAN number (e.g., ABCDE1234F)"
            scrollToView(binding.detaillayout,  binding.panEditText)
            Toast.makeText(context, "Enter a valid PAN number (e.g., ABCDE1234F)", Toast.LENGTH_SHORT).show()
            return false
        }else{
            binding.panEditText.error = null
        }


        if (storeName.isNullOrBlank() ) {
            binding.storeName.error= "Please enter store name"
            scrollToView(binding.detaillayout,  binding.storeName)
            Toast.makeText(context, "Please enter store name", Toast.LENGTH_SHORT).show()
            return false
        }else{
            binding.storeName.error = null
        }


        if (storeAddress.isNullOrBlank() ) {
            binding.storeaddresstxt.error= "Please enter store address"
            scrollToView(binding.detaillayout,  binding.storeaddresstxt)
            Toast.makeText(context, "Please enter store address", Toast.LENGTH_SHORT).show()
            return false
        }else{
            binding.storeaddresstxt.error = null
        }


        if (password.length < 6) {
            binding.password.error= "Password must be at least 6 characters"
            scrollToView(binding.detaillayout,  binding.password)
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        } else{
            binding.password.error = null
        }


        if (confirmPassword != password) {
            binding.confirmpassword.error= "Passwords do not match"
            scrollToView(binding.detaillayout,  binding.confirmpassword)
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }else{
            binding.confirmpassword.error = null
        }


        if(profilephotouri==null){
            Toast.makeText(context, "Upload profile photo", Toast.LENGTH_SHORT).show()
            binding.imagenotuploaded.visibility = View.VISIBLE
            scrollToView(binding.detaillayout, binding.imagenotuploaded)
            return false
        }
        else{
            binding.imagenotuploaded.visibility = View.GONE
        }


        if(aadhaarfronturi==null){
            Toast.makeText(context, "Upload aadhaar front photo", Toast.LENGTH_SHORT).show()
            binding.aadhaarfrontnotuploaded.visibility = View.VISIBLE
            scrollToView(binding.detaillayout, binding.aadhaarfrontlayout)
            return false
        }else{
            binding.aadhaarfrontnotuploaded.visibility = View.GONE
        }

        if(aadhaarbackuri==null){
            Toast.makeText(context, "Upload aadhaar back photo", Toast.LENGTH_SHORT).show()
            binding.aadhaarbanknotuploaded.visibility = View.VISIBLE
            scrollToView(binding.detaillayout, binding.aadhaarbacklayout)
            return false
        }else{
            binding.aadhaarbanknotuploaded.visibility = View.GONE
        }

        if(panuri==null){
            Toast.makeText(context, "Upload pan card photo", Toast.LENGTH_SHORT).show()
            binding.pancardfrontnotuploaded.visibility = View.VISIBLE
            scrollToView(binding.detaillayout, binding.pancardphotolayout)

            return false
        }else{
            binding.pancardfrontnotuploaded.visibility = View.GONE
        }

        if(cancelchequeuri==null){
            Toast.makeText(context, "Upload cancel cheque photo", Toast.LENGTH_SHORT).show()
            binding.cancelchequefrontnotuploaded.visibility = View.VISIBLE
            scrollToView(binding.detaillayout, binding.cancelchequelayout)

            return false
        }else{
            binding.cancelchequefrontnotuploaded.visibility = View.GONE
        }

        if(storephotouri==null){
            Toast.makeText(context, "Upload store front photo ", Toast.LENGTH_SHORT).show()
            binding.storefrontnotuploaded.visibility = View.VISIBLE
            scrollToView(binding.detaillayout, binding.storelayout)

            return false
        }else{
            binding.storefrontnotuploaded.visibility = View.GONE
        }


        if(companydocuri==null){
            Toast.makeText(context, "Upload company document photo", Toast.LENGTH_SHORT).show()
            binding.companydocumentfrontnotuploaded.visibility = View.VISIBLE
            scrollToView(binding.detaillayout, binding.companydocumentlayout)

            return false
        }else{
            binding.companydocumentfrontnotuploaded.visibility = View.GONE
        }

        return true // All fields are valid
    }


    fun emptyAboveField(){
        binding.firstName.text!!.clear()
        binding.lastName.text.clear()
        binding.mobileNumber.text!!.clear()
        binding.emailId.text.clear()
        binding.password.text.clear()
        binding.confirmpassword.text.clear()
        binding.address.text.clear()
        binding.panEditText.text.clear()
        binding.aadharnumber.text!!.clear()
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


    fun scrollToView(scrollView: NestedScrollView, targetView: View) {
        scrollView.post {
            /*scrollView.smoothScrollTo(0, targetView.top - offset)*/

            val location = IntArray(2)
            targetView.getLocationOnScreen(location)

            val scrollLocation = IntArray(2)
            scrollView.getLocationOnScreen(scrollLocation)

            val y = location[1] - scrollLocation[1]
            scrollView.smoothScrollBy(0, y)
        }
    }



}