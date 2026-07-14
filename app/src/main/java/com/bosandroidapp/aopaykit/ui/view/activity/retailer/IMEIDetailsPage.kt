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
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.bosandroidapp.aopaykit.databinding.ActivityImeidetailsPageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.BrandName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustFirstName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustLastName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustMiddleName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustPrimaryMobileNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.ImeiNumber1
import com.bosandroidapp.aopaykit.constant.ConstantClass.ImeiNumber1SealPhotoPath
import com.bosandroidapp.aopaykit.constant.ConstantClass.ImeiNumber2
import com.bosandroidapp.aopaykit.constant.ConstantClass.ImeiNumber2SealPhotoPath
import com.bosandroidapp.aopaykit.constant.ConstantClass.ImeiNumberPhotoPath
import com.bosandroidapp.aopaykit.constant.ConstantClass.Invoive_Path
import com.bosandroidapp.aopaykit.constant.ConstantClass.IsRetailerAggrementVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.ModelName
import com.bosandroidapp.aopaykit.constant.ConstantClass.iisAggrementVerified
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.slideshow.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IMEIDetailsPage : AppCompatActivity() {
    lateinit var binding : ActivityImeidetailsPageBinding
    private val CAMERA_REQUEST_CODE_FRONT = 1001
    private  var imei1photoUri: Uri? = null
    private  var imei2photoUri: Uri?=null
    private  var invoicePhotoUri: Uri? = null
    private  var ImeiPhotoUri: Uri?=null
    var imei1photo: Boolean = false
    var imei2photo: Boolean = false
    var invoicePhoto: Boolean = false
    var ImeiPhoto: Boolean = false
    var back: Boolean = false
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference: SharedPreference


    companion object{
        lateinit var dialog: Dialog
    }


    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            if(imei1photo){
                // Handle the photoUri, e.g., show image in ImageView
                binding.imei1photo.visibility= View.VISIBLE
                binding.imei1photoicon.visibility= View.GONE
                // binding.imei1photo.setImageURI(imei1photoUri)
                Glide.with(this)
                    .load(imei1photoUri)
                    .centerCrop()
                    .into(binding.imei1photo)
                binding.clicktosealphoto1.text= "Re- Upload"
                resetFlag()
            }
            else if(imei2photo){
                // Handle the photoUri, e.g., show image in ImageView
                binding.imei2photo.visibility= View.VISIBLE
                binding.imei2photoicon.visibility= View.GONE
                //binding.imei2photo.setImageURI(imei2photoUri)
                Glide.with(this)
                    .load(imei2photoUri)
                    .centerCrop()
                    .into(binding.imei2photo)
                binding.clicktosealphoto2.text= "Re- Upload"
                resetFlag()
            }

            else if(ImeiPhoto){
                // Handle the photoUri, e.g., show image in ImageView
                binding.ImeiPhoto.visibility= View.VISIBLE
                binding.imeiicon.visibility= View.GONE
                // binding.ImeiPhoto.setImageURI(ImeiPhotoUri)
                Glide.with(this)
                    .load(ImeiPhotoUri)
                    .centerCrop()
                    .into(binding.ImeiPhoto)
                binding.clicktoimeiphoto.text= "Re- Upload"
                resetFlag()
            }

            else if(invoicePhoto){
                // Handle the photoUri, e.g., show image in ImageView
                binding.invoicePhoto.visibility= View.VISIBLE
                binding.invoiceicon.visibility= View.GONE
                // binding.invoicePhoto.setImageURI(invoicePhotoUri)
                Glide.with(this)
                    .load(invoicePhotoUri)
                    .centerCrop()
                    .into(binding.invoicePhoto)
                binding.clicktoinvoice.text= "Re- Upload"
                resetFlag()
            }



        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImeidetailsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }
      
        viewModel = ViewModelProvider(
            this,
            CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterfacePAN))
        )[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)
        seonClickListner()

    }

    override fun onResume() {
        super.onResume()
        hitApiForLogin()
    }


    fun seonClickListner(){

        binding.home.setOnClickListener {
            val intent = Intent(this, DashBoard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            onBackPressed()
        }


        binding.back.setOnClickListener {
            OpenPopUpForVAlert()
        }


        binding.nextlayout.setOnClickListener {
            if(ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.kit)){
                val (isValid, errorMessage) = isValidKitForm(
                    IMEINumber1 = binding.IMEINumber1.text.toString().trim(),
                    IMEINumber2 = binding.IMEInumber2.text.toString().trim()
                )
                if (!isValid) {
                    Toast.makeText(this@IMEIDetailsPage, errorMessage, Toast.LENGTH_SHORT).show()
                }
                else{
                    ImeiNumber1 = binding.IMEINumber1.text.toString().trim()
                    ImeiNumber2 = binding.IMEInumber2.text.toString().trim()
                    ImeiNumber1SealPhotoPath = null
                    ImeiNumber2SealPhotoPath = null
                    Invoive_Path = null
                    ImeiNumberPhotoPath = null
                    OpenPopUpForTermCondition()
                }
            }
            else {
                val (isValid, errorMessage) = isValidForm(
                    IMEINumber1 = binding.IMEINumber1.text.toString().trim(),
                    IMEINumber2 = binding.IMEInumber2.text.toString().trim(),
                    imei1photoUri = imei1photoUri,
                    imei2photoUri = imei2photoUri,
                    invoicePhotoUri = invoicePhotoUri,
                    ImeiPhotoUri = ImeiPhotoUri
                )
                if (!isValid) {
                    Toast.makeText(this@IMEIDetailsPage, errorMessage, Toast.LENGTH_SHORT).show()
                }
                else{
                    ImeiNumber1 = binding.IMEINumber1.text.toString().trim()
                    ImeiNumber2 = binding.IMEInumber2.text.toString().trim()
                    ImeiNumber1SealPhotoPath = imei1photoUri
                    ImeiNumber2SealPhotoPath = imei2photoUri
                    Invoive_Path = invoicePhotoUri
                    ImeiNumberPhotoPath = ImeiPhotoUri
                    OpenPopUpForTermCondition()
                    //startActivity(Intent(this@IMEIDetailsPage, QRCodePage::class.java))

                }
            }


        }


        binding.clicktosealphoto1.setOnClickListener {
            resetFlag()
            imei1photo=true
            checkCameraPermissionAndOpenCamera()
        }


        binding.clicktosealphoto2.setOnClickListener {
            resetFlag()
            imei2photo=true
            checkCameraPermissionAndOpenCamera()

        }

        binding.clicktoimeiphoto.setOnClickListener {
            resetFlag()
            ImeiPhoto=true
            checkCameraPermissionAndOpenCamera()

        }

        binding.clicktoinvoice.setOnClickListener {
            resetFlag()
            invoicePhoto=true
            checkCameraPermissionAndOpenCamera()

        }



    }


    fun resetFlag(){
        imei1photo = false
        imei2photo = false
        ImeiPhoto = false
        invoicePhoto = false
    }


    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }


    fun clickCameraForUploadDocument(){
        val photoFile = createImageFile()
        if(imei1photo){
            imei1photoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            cameraLauncher.launch(imei1photoUri!!)
        }

        if(imei2photo){
            imei2photoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            cameraLauncher.launch(imei2photoUri!!)
        }

        if(ImeiPhoto){
            ImeiPhotoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            cameraLauncher.launch(ImeiPhotoUri!!)
        }

        if(invoicePhoto){
            invoicePhotoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            cameraLauncher.launch(invoicePhotoUri!!)
        }

    }


    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            clickCameraForUploadDocument()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE_FRONT)
        }
    }


    fun isValidForm(
        IMEINumber1: String,
        IMEINumber2: String,
        imei1photoUri: Uri?,
        imei2photoUri: Uri?,
        invoicePhotoUri: Uri?,
        ImeiPhotoUri: Uri?
    ): Pair<Boolean, String?> {

        if (IMEINumber1.isBlank() || IMEINumber1.length != 15 || !IMEINumber1.all { it.isDigit() }) {
            return Pair(false, "Enter a valid 15-digit IMEI Number 1")
        }

        if (IMEINumber2.isBlank() || IMEINumber2.length != 15 || !IMEINumber2.all { it.isDigit() }) {
            return Pair(false, "Enter a valid 15-digit IMEI Number 2")
        }

        if (imei1photoUri == null) {
            return Pair(false, "Upload IMEI 1 seal photo")
        }

        if (imei2photoUri == null) {
            return Pair(false, "Upload IMEI 2 seal photo")
        }

        if (invoicePhotoUri == null) {
            return Pair(false, "Upload invoice photo")
        }

        if (ImeiPhotoUri == null) {
            return Pair(false, "Upload combined IMEI photo")
        }

        return Pair(true, null)
    }


    fun isValidKitForm(
        IMEINumber1: String,
        IMEINumber2: String
    ): Pair<Boolean, String?> {

        if (IMEINumber1.isBlank() || IMEINumber1.length != 15 || !IMEINumber1.all { it.isDigit() }) {
            return Pair(false, "Enter a valid 15-digit IMEI Number 1")
        }

        if (IMEINumber2.isBlank() || IMEINumber2.length != 15 || !IMEINumber2.all { it.isDigit() }) {
            return Pair(false, "Enter a valid 15-digit IMEI Number 2")
        }


        return Pair(true, null)
    }


    // popup for aggreement..................................................


    fun OpenPopUpForTermCondition() {
        dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.retailer_customer_aggrement)

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        dialog.setCanceledOnTouchOutside(false)

        val verifyButton = dialog.findViewById<LinearLayout>(R.id.btnAccept)
        val termconditiontxt = dialog.findViewById<TextView>(R.id.termconditiontxt)
        val customername = dialog.findViewById<TextView>(R.id.customerName)
        val customername1 = dialog.findViewById<TextView>(R.id.customername)
        val brandname = dialog.findViewById<TextView>(R.id.brandname)
        val modelName = dialog.findViewById<TextView>(R.id.modelName)
        val imieno1 = dialog.findViewById<TextView>(R.id.imieno1)
        val imieno2 = dialog.findViewById<TextView>(R.id.imieno2)
        val mobnumber = dialog.findViewById<TextView>(R.id.mobnumber)
        val currentdate = dialog.findViewById<TextView>(R.id.currentdate)
        val retailerName = dialog.findViewById<TextView>(R.id.retailerName)


        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val safeLastName = if (!lastName.isNullOrBlank() && lastName != "null") lastName else ""

        val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        currentdate.text="Date : ".plus(currentDate)
        customername.text = "Hello ".plus(CustFirstName)
        customername1.text = CustFirstName .plus(" ").plus(CustMiddleName).plus(" ").plus(CustLastName)
        brandname.text = BrandName
        modelName.text = ModelName
        imieno1.text = ImeiNumber1
        imieno2.text = ImeiNumber2
        mobnumber.text = CustPrimaryMobileNumber
        retailerName.text = "To: ". plus(firstName) .plus(" ").plus(safeLastName)

        termconditiontxt.text = Html.fromHtml(getString(R.string.delivery_terms_condition), Html.FROM_HTML_MODE_LEGACY)

        verifyButton.setOnClickListener {
            iisAggrementVerified = true
            IsRetailerAggrementVerified = "yes"
            startActivity(Intent(this@IMEIDetailsPage, QRCodePage::class.java))
        }

        dialog.setOnDismissListener {
            // Called when dialog is dismissed by back press or programmatically
            if (iisAggrementVerified) {

            } else {
                iisAggrementVerified = false
                IsRetailerAggrementVerified = ""
            }


        }

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
                                ConstantClass.checkActiveStatusAndLogout(this@IMEIDetailsPage, response.status, preference)
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
                                val intent = Intent(this@IMEIDetailsPage, ChooseYourRolePage::class.java)
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