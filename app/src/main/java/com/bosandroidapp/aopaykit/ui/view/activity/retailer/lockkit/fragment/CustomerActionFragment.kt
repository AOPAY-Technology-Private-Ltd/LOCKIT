package com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.credentials.provider.Action
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.ClientCode
import com.bosandroidapp.aopaykit.constant.ConstantClass.subApp
import com.bosandroidapp.aopaykit.data.customeraction.GetPendingDeviceActionReq
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSaveDeviceActionRequest
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSendNotificationToCustomer
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSendNotificationToCustomerReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.DialogSubActionBinding
import com.bosandroidapp.aopaykit.databinding.FragmentCustomerActionBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.adapter.CustomerActionAdapter
import com.bosandroidapp.aopaykit.ui.view.adapter.SubActionAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CustomerActionFragment : Fragment() {
    private var _binding: FragmentCustomerActionBinding? = null
    private val binding get() = _binding!!
    private var customerActionList: MutableList<CustomerAction> = mutableListOf()
    private lateinit var adapter: CustomerActionAdapter
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference: SharedPreference



    companion object{
        var CustomerCode: String = ""
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCustomerActionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(requireContext())
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        setonclicklistner()
    }


    override fun onResume() {
        super.onResume()
        setDataOnView()
    }


    fun setonclicklistner(){

        binding.btnUninstall.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Confirm Uninstall")
                .setMessage("Are you sure you want to proceed with the uninstall action for this device?")
                .setPositiveButton("Yes") { _, _ ->
                    val actionList = CustomerAction("Uninstall", ConstantClass.UNINSTALL, true, emptyList())
                    hitApiForDoActionNotification(actionList, true)
                }
                .setNegativeButton("No", null)
                .show()
        }

    }


    private fun initAdapter() {
        adapter = CustomerActionAdapter(
            actionList = customerActionList,
            onActionClick = { action ->
                if (action.subactionList.isNotEmpty()) {
                    openAlertForSubAction(action)
                } else {
                    hitApiForDoActionNotification(action, !action.check)
                }
            },
            onSwitchToggle = { action, isChecked ->
                if (isChecked) {
                    if (action.subactionList.isNotEmpty()) {
                        openAlertForSubAction(action)
                    } else {
                        hitApiForDoActionNotification(action, true)
                    }
                } else {
                    if (action.subactionList.isNotEmpty()) {
                        action.subactionList.forEach { it.active = false }
                    }
                    hitApiForDoActionNotification(action, false)
                }
            }
        )
        binding.rvCustomerActions.adapter = adapter
    }



    fun setDataOnView() {
        getCustomerAction()
        adapter.updateList(customerActionList)
        hitApiForUpdateActionStatus()
    }


    fun getCustomerAction():List <CustomerAction>{

        customerActionList.clear()

        val socialApp = mutableListOf(
            SubAction(ConstantClass.FaceBook, false),
            SubAction(ConstantClass.WhatsApp, false),
            SubAction(ConstantClass.Instagram, false),
            SubAction(ConstantClass.Telegram, false),
            SubAction(ConstantClass.Snapchat, false),
            SubAction(ConstantClass.YouTube, false)
        )
        customerActionList.add(CustomerAction("Social Apps", ConstantClass.SocialApps,false,socialApp))

        val gamingApp = mutableListOf(
            SubAction(ConstantClass.CandyCrush, false),
            SubAction(ConstantClass.BattleGroundMobile, false),
            SubAction(ConstantClass.Chess, false),
            SubAction(ConstantClass.FreeFire, false),
            SubAction(ConstantClass.CallOfDuty, false),
            SubAction(ConstantClass.BallPool, false)
        )

        customerActionList.add(CustomerAction("Gaming Apps", ConstantClass.GamingApps,false,gamingApp))

        val upiApps = mutableListOf(
            SubAction(ConstantClass.Phonepe, false),
            SubAction(ConstantClass.Googlepay, false),
            SubAction(ConstantClass.Paytm, false),
            SubAction(ConstantClass.Cred, false),
            SubAction(ConstantClass.BHIM, false),
        )

        customerActionList.add(CustomerAction("UPI Apps", ConstantClass.UPIApps,false,upiApps))

        customerActionList.add(CustomerAction("Disable Call", ConstantClass.CALL_DISABLE,false,emptyList()))

        val disabledSetting = mutableListOf(
            SubAction(ConstantClass.Bluetooth, false),
            SubAction(ConstantClass.Wifi, false),
            SubAction(ConstantClass.Hotspot, false),
            SubAction(ConstantClass.USB, false),
        )

        customerActionList.add(CustomerAction("Disable Settings", ConstantClass.DisableSetting,false,disabledSetting))

        customerActionList.add(CustomerAction("KIOSK Mode", ConstantClass.Kisok,false,emptyList()))

        customerActionList.add(CustomerAction("Disable Camera", ConstantClass.CameraDisable,false,emptyList()))

        customerActionList.add(CustomerAction("Reboot", ConstantClass.Reboot,false,emptyList()))

        customerActionList.add(CustomerAction("Airplane Mode", ConstantClass.Airplane,false,emptyList()))

        val hideApps = mutableListOf(
            SubAction(ConstantClass.Gallery, false),
            SubAction(ConstantClass.Chrome, false),
            SubAction(ConstantClass.Gmail, false),
            SubAction(ConstantClass.GooglePhotos, false),
            SubAction(ConstantClass.GoogleDrive, false),
            SubAction(ConstantClass.PlayStore, false),
            SubAction(ConstantClass.GoogleMaps, false),
            SubAction(ConstantClass.Files, false),
            SubAction(ConstantClass.Calculator, false),
            SubAction(ConstantClass.Calendar, false),
            SubAction(ConstantClass.Contacts, false),
            SubAction(ConstantClass.Messages, false),
            SubAction(ConstantClass.Phone, false),
            SubAction(ConstantClass.XTwitter, false),
            SubAction(ConstantClass.Amazon, false),
            SubAction(ConstantClass.Flipkart, false),
            SubAction(ConstantClass.Netflix, false),
            SubAction(ConstantClass.Spotify, false)
        )

        customerActionList.add(CustomerAction("App Hide", ConstantClass.AppHide,false,hideApps))

        customerActionList.add(CustomerAction("Sim Remove Lock", ConstantClass.SIM_REMOVE_LOCK,false,emptyList()))

       /* customerActionList.add(CustomerAction("Sim Tracking Online",ConstantClass.SIM_TRACK_ONLINE,false,emptyList()))
          customerActionList.add(CustomerAction("Sim Tracking Offline",ConstantClass.SIM_TRACK_OFFLINE,false,emptyList()) )*/

        return  customerActionList

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun openAlertForSubAction(action: CustomerAction) {
        val dialogBinding = DialogSubActionBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Set title dynamically
        val title = if (action.actionName.contains("Apps", ignoreCase = true)) {
            "Select ${action.actionName.removeSuffix("s")}"
        } else {
            "Select ${action.actionName}"
        }
        dialogBinding.tvDialogTitle.text = title

        // Initialize Adapter
        val subAdapter = SubActionAdapter(action.subactionList) { allSelected ->
            dialogBinding.cbSelectAll.isChecked = allSelected
        }

        dialogBinding.rvSubActions.adapter = subAdapter

        // Check if all are already selected
        dialogBinding.cbSelectAll.isChecked = action.subactionList.all { it.active }

        // Select All Logic
        dialogBinding.cbSelectAll.setOnClickListener {
            val isChecked = dialogBinding.cbSelectAll.isChecked
            subAdapter.selectAll(isChecked)
        }


        dialogBinding.ivClose.setOnClickListener {
            dialog.dismiss()
            hitApiForUpdateActionStatus()
        }


        dialogBinding.btnSave.setOnClickListener {
            // Logic to handle saved actions
            Log.d("ActionSaved", Gson().toJson(action))
            val hasActiveSubActions = action.subactionList.any { it.active }
            hitApiForDoActionNotification(action, hasActiveSubActions)
            dialog.dismiss()
        }

        dialog.show()
    }


    fun hitApiForDoActionNotification(action: CustomerAction, actionStatus: Boolean = true) {
        var subApp: MutableList<RetailerSendNotificationToCustomer> = mutableListOf()
        subApp.clear()
        
        // convert Json data into string array list for sending enable disable feature for customer as per Naim sir discussion 22/07/2026

        action.subactionList.let { item ->
            item.forEach {
                subApp.add(RetailerSendNotificationToCustomer(it.subactionName, it.active))
            }
        }

        if(action.subactionList.isEmpty()){
            subApp.add(RetailerSendNotificationToCustomer(action.notificationCode, actionStatus))
        }


        Log.d("CheckBoxList", Gson().toJson(action.subactionList))


        Log.d("subApp", Gson().toJson(subApp))

        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val safeLastName = if (!lastName.isNullOrBlank() && lastName != "null") lastName else ""
        var createdBy = firstName.plus(" ").plus(safeLastName)
        var retailercode = preference.getStringValue(ConstantClass.RetailerCode, "")

        var actionRequest = RetailerSaveDeviceActionRequest(
            notificationCode = action.notificationCode,
            selectedApps = subApp,
            createdBy = createdBy,
            customerCode = CustomerCode,
            clientCode = ClientCode,
            retailerCode = retailercode,
            actionStatus = actionStatus
        )

        action.check = actionStatus
        adapter.notifyDataSetChanged()

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
                                    hitApiForSendNotificationToCustomer(action.notificationCode,subApp)
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


    fun hitApiForSendNotificationToCustomer(notificationCode : String, selectedApps: MutableList<RetailerSendNotificationToCustomer>){

        var sendNotificationReq = RetailerSendNotificationToCustomerReq(
            clientCode = ClientCode,
            customerCode = CustomerCode,
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
            title = "",
            message = "",
            notificationCode = notificationCode,
            devicePin= "",
            selectedApps = selectedApps
        )
        Log.d("notificationReq",Gson().toJson(sendNotificationReq))

        viewModel.sendRetailerNotificationToCustomerRequest(sendNotificationReq).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                Log.d("notificationResponse",Gson().toJson(response))
                                if (response!!.status == true) {
                                    Toast.makeText(requireContext(), response!!.message, Toast.LENGTH_SHORT).show()
                                }
                                else {
                                    Toast.makeText(requireContext(), response!!.message, Toast.LENGTH_SHORT).show()
                                }

                                lifecycleScope.launch {
                                    delay(3000)
                                    ConstantClass.dialog.dismiss()
                                    hitApiForUpdateActionStatus()
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
        val request = GetPendingDeviceActionReq(customerCode = CustomerCode)
        viewModel.getActiveDeviceActionRequest(request).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                ConstantClass.dialog.dismiss()
                                if (response.status == true && response.data != null) {

                                    Log.d("pendingStatusData", Gson().toJson(response))

                                    // 1. Reset all local actions before applying history
                                    customerActionList.forEach { local ->
                                        local.check = false
                                        local.subactionList.forEach { sub -> sub.active = false }
                                    }

                                    // 2. Map data by NotificationCode
                                    val latestActions = response.data!!.filterNotNull().sortedBy { it.rid }

                                    latestActions.forEach { action ->
                                        customerActionList.find { it.notificationCode == action.notificationCode }?.let { localAction ->
                                            if (localAction.subactionList.isNotEmpty()) {
                                                // Category with sub-actions (e.g., Social Apps)
                                                action.selectedApps?.forEach { appItem ->
                                                    val item = appItem as?  com.bosandroidapp.aopaykit.data.model.kitoption.SelectedAppsItem
                                                    if (item != null) {
                                                        localAction.subactionList.find { it.subactionName == item.packageName }?.active =
                                                            (item.action?.toLowerCase().equals("enable", ignoreCase = true) == true)
                                                    }
                                                }
                                                // Update master switch based on sub-actions
                                                localAction.check = localAction.subactionList.any { it.active }
                                            } else {
                                                // Simple category (e.g., Reboot)
                                                localAction.check = action.actionStatus ?: false
                                            }
                                        }
                                    }
                                    adapter.notifyDataSetChanged()
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


    // data class ........................................................................................................
    data class CustomerAction(
            var actionName: String,
            var notificationCode: String,
            var check : Boolean,
            var subactionList: List<SubAction>
            )

    data class SubAction(
            var subactionName: String,
            var active : Boolean
    )

    

}
