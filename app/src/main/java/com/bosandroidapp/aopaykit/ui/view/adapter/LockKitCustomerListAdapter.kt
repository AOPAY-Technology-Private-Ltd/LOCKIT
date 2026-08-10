package com.bosandroidapp.aopaykit.ui.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass.CUSTOMERDYNAMICACTIVESTATUS
import com.bosandroidapp.aopaykit.data.model.kitoption.CustomerListItem
import com.bosandroidapp.aopaykit.databinding.LockkitcustomerItemLayoutBinding
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.LockKitCustomerDetailsInfoPage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.LockKitCustomerDetailsInfoPage.Companion.kitcustomerData
import com.bumptech.glide.Glide
import java.util.Locale

class LockKitCustomerListAdapter(var context: Context, private var kitcustomerlist: List<CustomerListItem?>?) : RecyclerView.Adapter<LockKitCustomerListAdapter.ViewHolder>() {

    private var filteredList: MutableList<CustomerListItem?> = mutableListOf()

    init {
        kitcustomerlist?.let { filteredList.addAll(it) }
    }

    class ViewHolder(var binding: LockkitcustomerItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LockkitcustomerItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList[position] ?: return
        
        holder.binding.tvCustomerName.text = "${item.firstName} ${item.lastName} (${item.customerCodes})"
        holder.binding.tvCustomerEmail.text = item.eMailID
        holder.binding.tvMobileValue.text = item.primaryMobileNumber
        holder.binding.tvImei1Value.text = item.imeiNumber1
        holder.binding.tvImei2Value.text = item.imeiNumber2

        if (item.isDeviceLocked == true) {
            holder.binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_lock_button)
            holder.binding.tvStatusBadge.text = "Locked"
        } else {
            holder.binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_status_green_dark)
            holder.binding.tvStatusBadge.text = "UnLocked"
        }
        
        holder.binding.tvStatusBadge.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.phonelocked, 0, 0, 0
        )

        if (item.custPhotoFile != null) {
            Glide.with(context).load(item.custPhotoFile).into(holder.binding.ivProfile)
        } else {
            holder.binding.ivProfile.setImageResource(R.drawable.usericon)
        }

        holder.binding.root.setOnClickListener {
            kitcustomerData = item
            CUSTOMERDYNAMICACTIVESTATUS = kitcustomerData.customerActiveStatus!!
            context.startActivity(Intent(context, LockKitCustomerDetailsInfoPage::class.java))
        }
    }


    override fun getItemCount(): Int {
        return filteredList.size
    }


    fun filter(query: String): Int {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())
        filteredList.clear()
        if (lowerCaseQuery.isEmpty()) {
            kitcustomerlist?.let { filteredList.addAll(it) }
        } else {
            kitcustomerlist?.forEach { item ->
                if (item != null) {
                    val customerCode = item.customerCodes?.lowercase(Locale.getDefault()) ?: ""
                    val mobile = item.primaryMobileNumber?.lowercase(Locale.getDefault()) ?: ""
                    
                    val matchesCodeOrMobile = customerCode.contains(lowerCaseQuery) || 
                            mobile.contains(lowerCaseQuery)
                    
                    // Special handling for lock/unlock to ensure "lock" doesn't match "unlock"
                    val matchesStatus = when (lowerCaseQuery) {
                        "lock" -> item.isDeviceLocked == true
                        "unlock" -> item.isDeviceLocked != true
                        else -> {
                            val statusText = if (item.isDeviceLocked == true) "locked" else "unlocked"
                            statusText.contains(lowerCaseQuery)
                        }
                    }
                    
                    if (matchesCodeOrMobile || matchesStatus) {
                        filteredList.add(item)
                    }

                }
            }
        }
        notifyDataSetChanged()
        return filteredList.size
    }

    fun updateList(newList: List<CustomerListItem?>?) {
        kitcustomerlist = newList
        filteredList.clear()
        newList?.let { filteredList.addAll(it) }
        notifyDataSetChanged()
    }

}
