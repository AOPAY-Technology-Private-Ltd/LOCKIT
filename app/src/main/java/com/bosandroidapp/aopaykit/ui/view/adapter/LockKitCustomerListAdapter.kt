package com.bosandroidapp.aopaykit.ui.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.data.model.BankDataItem
import com.bosandroidapp.aopaykit.data.model.kitoption.CustomerListItem
import com.bosandroidapp.aopaykit.databinding.BankDetailsCardLayoutBinding
import com.bosandroidapp.aopaykit.databinding.LockkitcustomerItemLayoutBinding
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.LockKitCustomerDetailsInfoPage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.LockKitCustomerDetailsInfoPage.Companion.kitcustomerData
import com.bosandroidapp.aopaykit.ui.view.adapter.BankDetailsListAdapter.ViewHolder
import com.bumptech.glide.Glide

class LockKitCustomerListAdapter (var context: Context,var kitcustomerlist:List<CustomerListItem?> ?): RecyclerView.Adapter<LockKitCustomerListAdapter.ViewHolder>(){


    class ViewHolder (var  binding: LockkitcustomerItemLayoutBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LockkitcustomerItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvCustomerName.text = "${kitcustomerlist!![position]!!.firstName} ${kitcustomerlist!![position]!!.lastName} (${kitcustomerlist!![position]!!.customerCodes})"
        holder.binding.tvCustomerEmail.text = kitcustomerlist!![position]!!.eMailID
        holder.binding.tvMobileValue.text = kitcustomerlist!![position]!!.primaryMobileNumber
        holder.binding.tvImei1Value.text = kitcustomerlist!![position]!!.imeiNumber1
        holder.binding.tvImei2Value.text = kitcustomerlist!![position]!!.imeiNumber2

        if(kitcustomerlist!![position]!!.isDeviceLocked==true){
            holder.binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_lock_button)
            holder.binding.tvStatusBadge.text = "Locked"
            holder.binding.tvStatusBadge.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.phonelocked, // start drawable
                0,                      // top
                0,                      // end
                0                       // bottom
            )
        }
        else{
            holder.binding.tvStatusBadge.setBackgroundResource(R.drawable.bg_status_green_dark)
            holder.binding.tvStatusBadge.text = "UnLocked"
            holder.binding.tvStatusBadge.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.phonelocked, // start drawable
                0,                      // top
                0,                      // end
                0                       // bottom
            )
        }


        if(kitcustomerlist!![position]!!.custPhotoFile!=null){
            Glide.with(context).load(kitcustomerlist!![position]!!.custPhotoFile).into(holder.binding.ivProfile)
        }
        else{
            holder.binding.ivProfile.setImageResource(R.drawable.usericon)
        }


        holder.binding.root.setOnClickListener {
            kitcustomerData = kitcustomerlist!![position]!!
            context.startActivity(Intent(context, LockKitCustomerDetailsInfoPage::class.java))
        }

    }




    override fun getItemCount(): Int {
       return kitcustomerlist!!.size
    }


}