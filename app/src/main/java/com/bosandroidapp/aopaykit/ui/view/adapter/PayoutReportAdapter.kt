package com.bosandroidapp.aopaykit.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.twoDecimal
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.PayoutDataItem
import com.bosandroidapp.aopaykit.databinding.PayoutReportItemLayoutBinding


class PayoutReportAdapter (var context: Context, var payoutReportList : List<PayoutDataItem?>?): RecyclerView.Adapter<PayoutReportAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayoutReportAdapter.ViewHolder {
        val binding = PayoutReportItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    class ViewHolder (var binding : PayoutReportItemLayoutBinding): RecyclerView.ViewHolder(binding.root)


    override fun onBindViewHolder(holder: PayoutReportAdapter.ViewHolder, position: Int) {

       if(payoutReportList!![position]!!.transactionStatus!!.toLowerCase().equals("rejected",ignoreCase = true)) {

           holder.binding.tvStatus.setTextColor(
               ContextCompat.getColor(context, R.color.red)
           )
       }
       else {
           holder.binding.tvStatus.setTextColor(
               ContextCompat.getColor(context,R.color.green))
       }
       holder.binding.tvStatus.text = payoutReportList!![position]!!.transactionStatus
       holder.binding.tvAmount.text = "₹ ${payoutReportList!![position]!!.transferAmt!!.twoDecimal()}"
       holder.binding.tvDateTime.text = ConstantClass.formatRecordDateTime(payoutReportList!![position]!!.recordDate.toString(), payoutReportList!![position]!!.recordTime.toString())
       holder.binding.tvMessage.text = payoutReportList!![position]!!.transferToMsg
       holder.binding.tvServiceCharge.text = "Service Charge : ₹${payoutReportList!![position]!!.servicesChargeAmt!!.twoDecimal()}"
       holder.binding.tvGST.text = "GST (18%) : ₹ ${payoutReportList!![position]!!.gstAmt!!.twoDecimal()}"
       holder.binding.tvTotal.text = "Req Amt : ₹${payoutReportList!![position]!!.transferAmt!!.twoDecimal()}"
       holder.binding.tvTxnId.text = "Txn ID : ${payoutReportList!![position]!!.transactionID}"
    }



    override fun getItemCount(): Int {
        return payoutReportList!!.size
    }


}