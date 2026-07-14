package com.bosandroidapp.aopaykit.ui.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.data.model.BankDataItem
import com.bosandroidapp.aopaykit.databinding.BankDetailsCardLayoutBinding
import com.bosandroidapp.aopaykit.databinding.LockkitcustomerItemLayoutBinding
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.LockKitCustomerDetailsInfoPage
import com.bosandroidapp.aopaykit.ui.view.adapter.BankDetailsListAdapter.ViewHolder

class LockKitCustomerListAdapter (var context: Context, /*var listSize : List<Int>*/): RecyclerView.Adapter<LockKitCustomerListAdapter.ViewHolder>(){


    class ViewHolder (var  binding: LockkitcustomerItemLayoutBinding): RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LockkitcustomerItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.root.setOnClickListener {
            context.startActivity(Intent(context, LockKitCustomerDetailsInfoPage::class.java))
        }

    }



    override fun getItemCount(): Int {
       return 5
    }

}