package com.bosandroidapp.aopaykit.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.RetailerWalletReportsBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass.formatDateToReport
import com.bosandroidapp.aopaykit.data.model.DataItem
import com.bosandroidapp.aopaykit.localdb.SharedPreference

class RetailerWalletAdapter (var context: Context, var retailerWalletReportList :List<DataItem?>?): RecyclerView.Adapter<RetailerWalletAdapter.ViewHolder>() {

    lateinit var preference : SharedPreference

    class ViewHolder (private val binding: RetailerWalletReportsBinding): RecyclerView.ViewHolder(binding.root) {
        var status = binding.status
        var transactionid = binding.transactionid
        var withdrawAmount = binding.withdrawAmount
        var retailercode = binding.retailercode
        var transactiondate = binding.transactiondate
        var remarksmsg = binding.remarksmsg
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RetailerWalletReportsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int = retailerWalletReportList!!.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(retailerWalletReportList!![position]!!.transactionStatus!!.toLowerCase().equals("pending")){
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.orange))
        }

        if(retailerWalletReportList!![position]!!.transactionStatus!!.toLowerCase().equals("approved")){
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.green))
        }

        if(retailerWalletReportList!![position]!!.transactionStatus!!.toLowerCase().equals("rejected")){
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.red))
        }

        preference = SharedPreference(context)
        holder.status.text = retailerWalletReportList!![position]!!.transactionStatus!!.substring(0, 1).toUpperCase()+retailerWalletReportList!![position]!!.transactionStatus!!.substring(1).toLowerCase()
        holder.transactionid.text = retailerWalletReportList!![position]!!.transactionID
        holder.withdrawAmount.text = "₹ " .plus(retailerWalletReportList!![position]!!.amount   )
        holder.retailercode.text = retailerWalletReportList!![position]!!.retailerID
        holder.transactiondate.text = formatDateToReport(retailerWalletReportList!![position]!!.transactionDate!!)
        holder.remarksmsg.text = retailerWalletReportList!![position]!!.remarks
    }



}