package com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.dialog
import com.bosandroidapp.aopaykit.constant.ConstantClass.ClientCode
import com.bosandroidapp.aopaykit.data.customeraction.GetPendingDeviceActionReq
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSaveDeviceActionRequest
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSendNotificationToCustomer
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSendNotificationToCustomerReq
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.DialogPinBinding
import com.bosandroidapp.aopaykit.databinding.FragmentCustomerDeviceDetailsBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.LockKitCustomerDetailsInfoPage.Companion.kitcustomerData
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment.CustomerActionFragment.Companion.CustomerCode
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson

class CustomerDeviceFragment : Fragment() {
    private var _binding: FragmentCustomerDeviceDetailsBinding? = null
    private val binding get() = _binding!!
    lateinit var preference: SharedPreference
    lateinit var viewModel: AuthenticationViewModel
    var devicePin: String =""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCustomerDeviceDetailsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(requireContext())

        setDataOnView()
        //setOnClickListner()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        hitApiForLogin()
    }


    override fun onPause() {
        super.onPause()

        if(dialog!=null && dialog.isShowing)
        {
            dialog.dismiss()
        }

    }


    fun setOnClickListner(){

        binding.btnSetPin.setOnClickListener {
            hitApiForLogin()
            showSetPinDialog()
        }
    }


     fun refreshData() {
        // Call API
        // Update RecyclerView
        // Reload data

        setDataOnView()
    }


    fun setDataOnView(){
        binding.tvBrand.text= kitcustomerData.brand
        binding.tvManufacturer.text= kitcustomerData.manufacturer
        binding.tvFrp.text = ""
        binding.tvSerial.text = kitcustomerData.serialNumber ?: ""
        binding.btnSetPin.text = kitcustomerData.devicePin
        binding.tvImei1.text= kitcustomerData.imeiNumber
        binding.tvImei2.text= kitcustomerData.imeiNumber2
        binding.tvModel.text= kitcustomerData.model
    }

    private fun showSetPinDialog() {
        val dialog = Dialog(requireContext())
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
                    hitApiForDoActionNotification(ConstantClass.DevicePin)
                    dialog.dismiss()
                }

            } else {
                Toast.makeText(requireContext(), "Please enter 4-digit PIN", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    fun hitApiForDoActionNotification(action: String){
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val safeLastName = if (!lastName.isNullOrBlank() && lastName != "null") lastName else ""
        var createdBy = firstName.plus(" ").plus(safeLastName)
        var retailercode = preference.getStringValue(ConstantClass.RetailerCode, "")

        var subApp: MutableList<RetailerSendNotificationToCustomer> = mutableListOf()
        subApp.clear()
        subApp.add(RetailerSendNotificationToCustomer(action, true))

        var actionRequest = RetailerSaveDeviceActionRequest(
            notificationCode = action,
            selectedApps = subApp,
            createdBy = createdBy,
            customerCode = CustomerCode,
            clientCode = ClientCode,
            retailerCode = retailercode,
            actionStatus = true
        )
        Log.d("customerLoanemirequest", Gson().toJson(actionRequest))
        viewModel.getRetailerDeviceActionToCustomerRequest(actionRequest).observe(requireActivity()) { resources ->
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
                                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
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
                        ConstantClass.OpenLoader(requireContext())
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
            devicePin = devicePin,
            selectedApps = subApp
        )
        Log.d("notificationReq",Gson().toJson(sendNotificationReq))

        viewModel.sendRetailerNotificationToCustomerRequest(sendNotificationReq).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                ConstantClass.dialog.dismiss()
                                Log.d("notificationResponse",Gson().toJson(response))
                                if (response!!.status == true) {
                                    hitApiForUpdateActionStatus()
                                    Toast.makeText(requireContext(), response!!.message, Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    Toast.makeText(requireContext(), response!!.message, Toast.LENGTH_SHORT).show()
                                }

                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(requireContext())
                    }

                }
            }
        }

    }

    fun hitApiForUpdateActionStatus() {
        val request = GetPendingDeviceActionReq(customerCode = kitcustomerData.customerCodes.toString().trim())
        viewModel.getPendingDeviceActionRequest(request).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                ConstantClass.dialog.dismiss()
                                if (response.status == true && response.data != null) {

                                }
                            }
                        }
                    }
                    ApiStatus.ERROR -> ConstantClass.dialog.dismiss()
                    ApiStatus.LOADING -> ConstantClass.OpenLoader(requireContext())
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun hitApiForLogin() {

        var deviceId = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
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
                                ConstantClass.checkActiveStatusAndLogout(requireContext(), response.status, preference)
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
                                val intent = Intent(requireContext(), ChooseYourRolePage::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                requireActivity().finish()
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
