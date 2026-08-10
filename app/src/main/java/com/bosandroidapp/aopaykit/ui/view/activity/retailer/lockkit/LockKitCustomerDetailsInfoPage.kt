package com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.CUSTOMERDYNAMICACTIVESTATUS
import com.bosandroidapp.aopaykit.constant.ConstantClass.ClientCode
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustomerDevicePin
import com.bosandroidapp.aopaykit.data.customeraction.GetKitCustomerLocation
import com.bosandroidapp.aopaykit.data.customeraction.GetPendingDeviceActionReq
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSaveDeviceActionRequest
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSendNotificationToCustomer
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSendNotificationToCustomerReq
import com.bosandroidapp.aopaykit.data.model.CustomerKitRequest
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.kitoption.CustomerListItem
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityLockKitCustomerDetailsInfoPageBinding
import com.bosandroidapp.aopaykit.databinding.DialogInactiveCustomerBinding
import com.bosandroidapp.aopaykit.databinding.DialogPinBinding
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.slideshow.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.MapActivity
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.MapActivity.Companion.lattitude
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.MapActivity.Companion.longitude
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment.CustomerActionFragment
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment.CustomerActionFragment.Companion.CustomerCode
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment.CustomerDeviceFragment
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment.CustomerInfoFragment
import com.bosandroidapp.aopaykit.ui.view.adapter.InfoTabAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LockKitCustomerDetailsInfoPage : BaseActivity() {
    lateinit var binding : ActivityLockKitCustomerDetailsInfoPageBinding
    private lateinit var tabAdapter: InfoTabAdapter
    lateinit var preference: SharedPreference
    lateinit var viewModel: AuthenticationViewModel
    var clickLocation = false
    var devicePin: String =""


    companion object{
        lateinit var kitcustomerData : CustomerListItem
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLockKitCustomerDetailsInfoPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBarsInsets.left,
                0,
                systemBarsInsets.right,
                systemBarsInsets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)

        setupTabs()
        setOnClickListner()
        setDataOnView()

        // Load initial fragment
        replaceFragment(CustomerInfoFragment())

       /* for(i in 217 ..363){
            hitApiForKitUpdateActionStatus(i)
        }*/

        // Observe global refresh events
        lifecycleScope.launch {
            AuthRepository.customerListUpdate.collectLatest {
                getKitCustomerList()
            }
        }

    }


    /*   private val authRepository by lazy { AuthRepository(RetrofitClient.apiInterface) }
    fun hitApiForKitUpdateActionStatus(rid:Int) {
        val updaterequest = UpdateCustomerDeviceActionRequest(
            rid = rid,
            updatedBy = preference.getStringValue(ConstantClass.CustomerCode, ""),
            executionStatus = "Success",
            failureReason = "")
        runBlocking {
            try {
                val response = authRepository.updateActionFromCustomerDevice(updaterequest)
                if (response?.isSuccessful == true) {
                    Log.d("responseUpdate", Gson().toJson(response.body()))
                }
            } catch (e: Exception) {
                Log.e("hitApiForUpdateStatus", "Error: ${e.message}")
            }
            Unit
        }
    }*/



    override fun onResume() {
        super.onResume()
        clickLocation= false
        hitApiForUpdateActionStatus()
        hitApiForLogin()
    }


    private fun setDataOnView(){
        binding.tvCustName.text = kitcustomerData.firstName+" "+kitcustomerData.lastName
        CustomerCode = kitcustomerData.customerCodes.toString().trim()
        binding.tvCustId.text = kitcustomerData.customerCodes
        binding.tvActiveBadge.text = kitcustomerData.customerActiveStatus
        if(kitcustomerData.custPhotoFile!=null){
            Glide.with(this).load(kitcustomerData.custPhotoFile).into(binding.ivProfilePic)
        }
        else{
           binding.ivProfilePic.setImageResource(R.drawable.usericon)
        }
    }

    private fun setupTabs() {
        val tabList = listOf("Info", "Device", "Action")
        
        tabAdapter = InfoTabAdapter(tabList) { selectedTab ->
            when (selectedTab) {
                "Info" -> replaceFragment(CustomerInfoFragment())
                "Device" -> replaceFragment(CustomerDeviceFragment())
                "Action" -> {
                    if (CUSTOMERDYNAMICACTIVESTATUS.lowercase(Locale.getDefault()) == ConstantClass.ISCUSTOMERACTIONPERFORM) {
                        showInactiveCustomerDialog()
                    }else{
                        replaceFragment(CustomerActionFragment())
                    }
                    return@InfoTabAdapter
                }
            }
        }

        binding.rvTabs.apply {
            layoutManager = LinearLayoutManager(this@LockKitCustomerDetailsInfoPage, LinearLayoutManager.HORIZONTAL, false)
            adapter = tabAdapter
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }


    fun setOnClickListner(){

        binding.back.setOnClickListener {
            finish()
        }


        binding.home.setOnClickListener {
            val intent = Intent(this, DashBoard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            onBackPressed()
        }


        binding.locklayout.setOnClickListener {
            if (CUSTOMERDYNAMICACTIVESTATUS.lowercase(Locale.getDefault()) == ConstantClass.ISCUSTOMERACTIONPERFORM) {
                showInactiveCustomerDialog()
            } else {
                clickLocation = false
                showSetPinDialog()
            }
        }

        binding.unlocklayout.setOnClickListener {
            if (CUSTOMERDYNAMICACTIVESTATUS.lowercase(Locale.getDefault()) == ConstantClass.ISCUSTOMERACTIONPERFORM) {
                showInactiveCustomerDialog()
            } else {
                clickLocation = false
                hitApiForDoActionNotification(ConstantClass.UnLock, false)
            }
        }

        binding.getlocation.setOnClickListener {
            if (CUSTOMERDYNAMICACTIVESTATUS.lowercase(Locale.getDefault()) == ConstantClass.ISCUSTOMERACTIONPERFORM) {
                showInactiveCustomerDialog()
            } else {
                clickLocation = true
                hitApiForDoActionNotification(ConstantClass.GETLOCATION, true)
            }
        }


    }


    private fun showInactiveCustomerDialog() {
        val dialog = Dialog(this)
        val dialogBinding = DialogInactiveCustomerBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        dialogBinding.btnOk.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showSetPinDialog() {
        val dialog = Dialog(this)
        val dialogBinding = DialogPinBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnOk.setOnClickListener {
            val pin = dialogBinding.pinView.text.toString()
            if (pin.length == 4) {
                // Handle PIN setting logic here
                devicePin = pin
                if(devicePin.isNotEmpty()){
                    CustomerDevicePin= devicePin
                    var subApp: MutableList<RetailerSendNotificationToCustomer> = mutableListOf()
                    subApp.add(RetailerSendNotificationToCustomer(ConstantClass.DevicePin,true))
                    //hitApiForDoActionNotification(ConstantClass.Lock,true)
                    hitApiForDoActionNotification(ConstantClass.DevicePin,true)

                    // hitApiForDoActionNotification(ConstantClass.Lock,true)

                    dialog.dismiss()
                }
            } else {
                Toast.makeText(this, "Please enter 4-digit PIN", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    fun hitApiForDoActionNotification(action: String,actionStatus: Boolean){
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val safeLastName = if (!lastName.isNullOrBlank() && lastName != "null") lastName else ""
        var createdBy = firstName.plus(" ").plus(safeLastName)
        var retailercode = preference.getStringValue(ConstantClass.RetailerCode, "")


        var subApp: MutableList<RetailerSendNotificationToCustomer> = mutableListOf()

        subApp.add(RetailerSendNotificationToCustomer(action,actionStatus))

        var actionRequest = RetailerSaveDeviceActionRequest(
            notificationCode = action,
            selectedApps = subApp,
            createdBy = createdBy,
            customerCode = CustomerCode,
            clientCode = ClientCode,
            retailerCode = retailercode,
            actionStatus = actionStatus
        )

        Log.d("customerLoanemirequest", Gson().toJson(actionRequest))

        viewModel.getRetailerDeviceActionToCustomerRequest(actionRequest).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("customerLoanemiresp", Gson().toJson(response))

                                if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                                    ConstantClass.dialog.dismiss()
                                }

                                if(response.status==true){
                                    hitApiForSendNotificationToCustomer(action,subApp)
                                }
                                else {
                                    Toast.makeText(this,response.message,Toast.LENGTH_SHORT).show()
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

    fun hitApiForSendNotificationToCustomer(notificationCode : String,subApp: MutableList<RetailerSendNotificationToCustomer>){

        var sendNotificationReq = RetailerSendNotificationToCustomerReq(
            clientCode = ClientCode,
            customerCode = CustomerCode,
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
            title = "",
            message = "",
            notificationCode = notificationCode,
            devicePin = CustomerDevicePin,
            selectedApps = subApp
        )
        Log.d("notificationReq",Gson().toJson(sendNotificationReq))

        viewModel.sendRetailerNotificationToCustomerRequest(sendNotificationReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                Log.d("notificationResponse",Gson().toJson(response))

                                if (response!!.status == true) {
                                    if(notificationCode.equals(ConstantClass.DevicePin)){
                                         ConstantClass.dialog.dismiss()
                                         hitApiForDoActionNotification(ConstantClass.Lock,true)
                                         return@observe
                                    }
                                    lifecycleScope.launch {
                                        delay(3000)
                                        ConstantClass.dialog.dismiss()
                                        hitApiForUpdateActionStatus()
                                        AuthRepository.notifyCustomerListChanged()
                                    }

                                   // Toast.makeText(this, response!!.message, Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    ConstantClass.dialog.dismiss()
                                    Toast.makeText(this, response!!.message, Toast.LENGTH_SHORT).show()
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

    fun hitApiForUpdateActionStatus() {
        val request = GetPendingDeviceActionReq(customerCode = kitcustomerData.customerCodes.toString().trim())

        viewModel.getActiveDeviceActionRequest(request).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->

                                Log.d("notificationResponse", Gson().toJson(response))
                                if (response.status == true && response.data != null) {

                                response.data.let { it->

                                    it.forEach { item ->

                                        var notificationCode = item!!.notificationCode
                                        var actionStatus = item!!.actionStatus
                                        var selectedapps = item!!.selectedApps

                                        if(notificationCode==ConstantClass.Lock){
                                            selectedapps!!.forEach { item->
                                                if(item!!.action.equals("disable", ignoreCase = true)){
                                                    binding.locklayout.setBackgroundDrawable(null)
                                                    binding.unlocklayout.setBackgroundResource(R.drawable.bg_lock_button)
                                                }else{
                                                    binding.locklayout.setBackgroundResource(R.drawable.bg_lock_button)
                                                    binding.unlocklayout.setBackgroundDrawable(null)
                                                }
                                            }

                                         }

                                        Log.d("notificationCodeCheck","${notificationCode}  ${ConstantClass.GETLOCATION}  ${clickLocation}")

                                        if(notificationCode==ConstantClass.GETLOCATION && clickLocation){
                                           getKitCustomerLocation()
                                         }


                                      }

                                   }
                                }
                            }
                        }
                    }
                    ApiStatus.ERROR -> {}

                    ApiStatus.LOADING -> {}
                }
            }
        }
    }

    fun getKitCustomerLocation(){
        var request = GetKitCustomerLocation(
            clientCode = ClientCode,
            customerCode = kitcustomerData.customerCodes.toString().trim(),
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, "")
        )

        viewModel.getKitCustomerLocation(request).observe(this){resources->
            resources.let {
                when(it.apiStatus){
                    ApiStatus.SUCCESS->{
                        var getData = it.data?.body()
                        Log.d("getKitCustomerLocation",Gson().toJson(getData))
                        if(getData!!.status==true && getData.data!=null){
                            lattitude = getData!!.data?.latitude!!
                            longitude = getData!!.data?.longitude!!
                             startActivity(Intent(this,MapActivity::class.java))
                        }
                        else{

                        }

                    }
                    ApiStatus.ERROR->{
                        // ✅ Print the full error details
                        Log.e("API_ERROR", "Status: ERROR")
                        Log.e("API_ERROR_CODE", resources.data?.code().toString())
                        Log.e("API_ERROR_MSG", resources.message ?: "Unknown Error")
                    }

                    ApiStatus.LOADING -> {

                    }
                }
            }
        }
    }


    fun getKitCustomerList(){
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val safeLastName = if (!lastName.isNullOrBlank() && lastName != "null") lastName else ""
        var createdBy = firstName.plus(" ").plus(safeLastName)
        var retailercode = preference.getStringValue(ConstantClass.RetailerCode, "")

        var registationRequest = CustomerKitRequest(
            mode = "GET",
            firstName =  "",
            middleName =  "",
            lastName =  "",
            primaryMobileNumber =  "",
            primaryOTP =  "",
            primaryMobileVerified =  "",
            alternateMobileNumber = "",
            alternateMobileOTP =  "",
            pAlternateMobileVerified =  "",
            eMailID =  "",
            flatNo =  "",
            aearSector =  "",
            pinCode = "",
            currentAddress =  "",
            stateName =  "",
            cityName =  "",
            country =  "India",
            aadharNumber =  "",
            aadharNumberVerified =  "",
            panNumber = "",
            panNumberVerified = "",
            brandName = "",
            modelName = "",
            modelVariant = "",
            color = "",
            sellingPrice =  "",
            downPayment = "",
            tenure =  "",
            emiAmount = "",
            imeiNumber1 = "",
            imeiNumber2 =  "",
            accountNumber = "",
            bankIFSCCode = "",
            bankName =  "",
            accountType =  "",
            branchName =  "",
            refName =  "",
            refRelationShip = "",
            refmobileNo = "",
            refAddress = "",
            debitOrCreditCard = "",
            upiMandate = "yes",
            createdBy = createdBy,
            membershipfees = "",
            retailercode = retailercode,
            cibilScore = "",
            isAggrementVerified = "",
            IsRetailerAggrementVerified = "",
            custPhoto_File =  null,
            imeiNumber1_SealPhotoPath = null,
            imeiNumber2_SealPhotoPath =  null,
            imeiNumber_PhotoPath =  null,
            invoive_Path =  null,
            aadharFront_Path =  null,
            aadharBack_Path =  null,
            panFront_Path =  null
        )
        Log.d("RegistationRequest", Gson().toJson(registationRequest))
        viewModel.getCustomerKitRequest(registationRequest).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                               // ConstantClass.dialog.dismiss()
                                // Toast.makeText(this, response!!.message, Toast.LENGTH_SHORT).show() // Optional: remove or keep
                                if (response!!.statuss!!.toLowerCase().equals("success", ignoreCase = true)) {

                                    if(!response.customerList.isNullOrEmpty()){
                                        val updatedItem = response.customerList.find { it?.customerCodes == kitcustomerData.customerCodes }
                                        if (updatedItem != null) {
                                            kitcustomerData = updatedItem
                                            CUSTOMERDYNAMICACTIVESTATUS = kitcustomerData.customerActiveStatus!!

                                            val currentFragment = supportFragmentManager
                                                .findFragmentById(R.id.fragmentContainer)

                                            if (currentFragment is CustomerDeviceFragment) {
                                                // Current fragment is HomeFragment
                                                currentFragment.refreshData()
                                            }
                                            setDataOnView()
                                            // Optional: notify current fragment if needed, but since fragments use companion object it might be okay
                                        }
                                    }
                                }
                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                       // ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        //ConstantClass.OpenLoader(this)
                    }

                }
            }
        }
    }


    fun hitApiForLogin() {

        var deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        preference.setStringValue(ConstantClass.DEVICEID,deviceId)

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
                                ConstantClass.checkActiveStatusAndLogout(this@LockKitCustomerDetailsInfoPage, response.status, preference)
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
            deviceId,
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
                                val intent = Intent(this@LockKitCustomerDetailsInfoPage, ChooseYourRolePage::class.java)
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