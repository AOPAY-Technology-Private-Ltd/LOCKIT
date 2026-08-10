package com.bosandroidapp.aopaykit.ui.view.adapter

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.CategoriesItem
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.databinding.ItemCustomerActionBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson

class CustomerActionAdapter(var context: Context, var lifecycleOwner: LifecycleOwner, var viewModel: AuthenticationViewModel, var actionList: List<CategoriesItem?>, private val onActionClick: (CategoriesItem) -> Unit,
    private val onSwitchToggle: (CategoriesItem, Boolean) -> Unit
) : RecyclerView.Adapter<CustomerActionAdapter.ViewHolder>() {

    private val preference: SharedPreference by lazy { SharedPreference(context) }

    inner class ViewHolder(val binding: ItemCustomerActionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCustomerActionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val action = actionList[position]
        Log.d("AdapterListAction", Gson().toJson(action))

        with(holder.binding) {
            tvActionTitle.text = action!!.actionName
            
            // Set icons based on action name
            val iconRes = when (action.actionName) {
                "Social" -> R.drawable.ic_user
                "Gaming" -> R.drawable.ic_tag
                "Image" -> R.drawable.ic_image
                "Map" -> R.drawable.locationicon
                "Video" -> R.drawable.ic_video
                "News" -> R.drawable.ic_video
                "Productivity" -> R.drawable.walleticon
                "Disable Call" -> R.drawable.callicon
                "Disable Settings" -> R.drawable.menuicon
                "KIOSK Mode" -> R.drawable.lockscreenicon
                "Disable Camera" -> R.drawable.cameraicon
                "Reboot" -> R.drawable.synchicon
                "Airplane Mode" -> R.drawable.baseline_calendar_month_24
                "App Hide" -> R.drawable.close
                "Sim Remove Lock" -> R.drawable.ic_card
                "Sim Tracking Online" -> R.drawable.locationicon
                "Sim Tracking Offline" -> R.drawable.locationicon
                else -> R.drawable.baseline_calendar_month_24
            }

            ivActionIcon.setImageResource(iconRes)

            // Show Track button for specific actions, else show Switch
            if (action.actionName!!.contains("Tracking", ignoreCase = true)) {
                btnTrackAction.visibility = View.VISIBLE
                switchAction.visibility = View.GONE
            }
            else {
                btnTrackAction.visibility = View.GONE
                switchAction.visibility = View.VISIBLE
                
                // Avoid listener triggering during binding
                switchAction.setOnCheckedChangeListener(null)
                switchAction.isChecked = action.check!!

                switchAction.setOnCheckedChangeListener { _, isChecked ->
                    hitApiForLogin {
                        onSwitchToggle(action, isChecked)
                    }
                }
            }

            btnTrackAction.setOnClickListener {
                hitApiForLogin {
                    onActionClick(action)
                }
            }

            root.setOnClickListener {
                hitApiForLogin {
                    onActionClick(action)
                }
            }
        }
    }

    override fun getItemCount(): Int = actionList.size

    fun updateList(newList: MutableList<CategoriesItem?>) {
        actionList = newList
        notifyDataSetChanged()
    }


    fun hitApiForLogin(onApproved: () -> Unit) {
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        preference.setStringValue(ConstantClass.DEVICEID, deviceId)

        val sessionOutReq = SessionOutReq(
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
        )

        Log.d("SessionOutReq", Gson().toJson(sessionOutReq))

        viewModel.getSessionReq(sessionOutReq).observe(lifecycleOwner) { resources ->
            if (resources.apiStatus == ApiStatus.SUCCESS) {
                resources.data?.body()?.let { response ->
                    Log.d("SessionOutResponse", Gson().toJson(response))
                    if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                        ConstantClass.dialog.dismiss()
                    }
                    if (response.status == "Approved") {
                        // First check passed, now check session expiration
                        val request = ValidateSessionRequest(
                            preference.getStringValue(ConstantClass.RetailerCode, ""),
                            deviceId,
                            preference.getStringValue(ConstantClass.FCMTOKEN, "")
                        )

                        Log.d("validaterequest", Gson().toJson(request))
                        viewModel.getSessionExpiredReq(request).observe(lifecycleOwner) { validateResources ->
                            if (validateResources.apiStatus == ApiStatus.SUCCESS) {
                                validateResources.data?.body()?.let { validateResponse ->
                                    Log.d("validateresp", Gson().toJson(validateResponse))
                                    if (validateResponse.status == 1) {
                                        onApproved()
                                    }
                                    else if (validateResponse.status == 0) {
                                        hitApiForRetailerLogout()
                                    }
                                }
                            }
                        }
                    } else {
                        ConstantClass.checkActiveStatusAndLogout(context, response.status, preference)
                    }
                }
            }
        }
    }


    fun hitApiForRetailerLogout() {
        val loginRequest = LogoutReq(
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
        )

        Log.d("LogoutReq", Gson().toJson(loginRequest))

        viewModel.getLogout(loginRequest).observe(lifecycleOwner) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("LogoutResponse", Gson().toJson(response))
                                preference.setBooleanValue(ConstantClass.LoggedIn, false)
                                preference.setStringValue(ConstantClass.LoginType, "")
                                ConstantClass.ClickOnCardDashboard = ""
                                val intent = Intent(context, ChooseYourRolePage::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent)
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

}
