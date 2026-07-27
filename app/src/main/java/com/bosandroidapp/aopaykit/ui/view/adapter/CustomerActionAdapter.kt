package com.bosandroidapp.aopaykit.ui.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ItemCustomerActionBinding
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment.CustomerActionFragment
import com.google.gson.Gson

class CustomerActionAdapter(private var actionList: List<CustomerActionFragment.CustomerAction>,
    private val onActionClick: (CustomerActionFragment.CustomerAction) -> Unit,
    private val onSwitchToggle: (CustomerActionFragment.CustomerAction, Boolean) -> Unit
) : RecyclerView.Adapter<CustomerActionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCustomerActionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCustomerActionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val action = actionList[position]
        Log.d("AdapterListAction", Gson().toJson(action))

        with(holder.binding) {

            tvActionTitle.text = action.actionName
            
            // Set icons based on action name
            val iconRes = when (action.actionName) {
                "Social Apps" -> R.drawable.ic_user
                "Gaming Apps" -> R.drawable.ic_tag
                "UPI Apps" -> R.drawable.walleticon
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
            if (action.actionName.contains("Tracking", ignoreCase = true)) {
                btnTrackAction.visibility = View.VISIBLE
                switchAction.visibility = View.GONE
            }
            else {
                btnTrackAction.visibility = View.GONE
                switchAction.visibility = View.VISIBLE
                
                // Avoid listener triggering during binding
                switchAction.setOnCheckedChangeListener(null)
                switchAction.isChecked = action.check

                switchAction.setOnCheckedChangeListener { _, isChecked ->
                    action.check = isChecked
                    onSwitchToggle(action, isChecked)
                }
            }

            btnTrackAction.setOnClickListener {
                onActionClick(action)
            }

            root.setOnClickListener {
                onActionClick(action)
            }
        }
    }

    override fun getItemCount(): Int = actionList.size

    fun updateList(newList: List<CustomerActionFragment.CustomerAction>) {
        actionList = newList
        notifyDataSetChanged()
    }
}
