package com.bosandroidapp.aopaykit.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bosandroidapp.aopaykit.constant.ConstantClass.formatIndianAmount
import com.bosandroidapp.aopaykit.data.model.LedgerReportDataItem
import com.bosandroidapp.aopaykit.databinding.LedgerLayoutBinding


class LedgerAdapter(var context: Context, var ledgerReportList: List<LedgerReportDataItem?>) : Adapter<LedgerAdapter.ViewHolder>() {


    class ViewHolder(var binding: LedgerLayoutBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LedgerAdapter.ViewHolder {
        val binding = LedgerLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvCustomerName.text = ledgerReportList[position]!!.customerName
        holder.binding.tvCustomerCode.text = "Customer Code : ${ledgerReportList[position]!!.customerCode}"
        holder.binding.tvEmiAmount.text = formatIndianAmount(ledgerReportList[position]!!.emiAmount.toString())
        holder.binding.tvPreviousHold.text = formatIndianAmount(ledgerReportList[position]!!.previousHoldBalance.toString())
        holder.binding.tvDeductedAmount.text = formatIndianAmount(ledgerReportList[position]!!.deductedHoldAmount.toString())
        holder.binding.tvRemainingBalance.text = formatIndianAmount(ledgerReportList[position]!!.remainingHoldBalance.toString())
        holder.binding.tvPaymentStatus.text = ledgerReportList[position]!!.paymentStatus
        holder.binding.tvDateTime.text = ledgerReportList[position]!!.transactionDate
        holder.binding.tvPayMode.text = ledgerReportList[position]!!.paymentMode
        holder.binding.tvPhone.text = ledgerReportList[position]!!.mobileNo
        holder.binding.tvLoanCode.text = "Loan Code : ${ledgerReportList[position]!!.loanCode}"
        holder.binding.duedate.text = "Due Date : ${ledgerReportList[position]!!.dueDate}"

        holder.binding.tvFineAmount.text = formatIndianAmount(ledgerReportList[position]?.lateFine?.toString().takeIf { !it.isNullOrBlank() } ?: "0")

        holder.binding.tvBounceAmount.text = formatIndianAmount(ledgerReportList[position]?.bouncingCharge?.toString().takeIf { !it.isNullOrBlank() } ?: "0")

        holder.binding.tvOtherAmount.text = formatIndianAmount(ledgerReportList[position]?.otherCharge?.toString().takeIf { !it.isNullOrBlank() } ?: "0")

        holder.binding.tvWaiveAmount.text = formatIndianAmount(ledgerReportList[position]?.waiveOffAmount?.toString().takeIf { !it.isNullOrBlank() } ?: "0")

        //sum of fine,bounce,other
        val totalAmount = (ledgerReportList[position]?.lateFine?.toString()?.toDoubleOrNull() ?: 0.0) +
                (ledgerReportList[position]?.bouncingCharge?.toString()?.toDoubleOrNull() ?: 0.0) +
                (ledgerReportList[position]?.otherCharge?.toString()?.toDoubleOrNull() ?: 0.0)

        holder.binding.tvTotalChargesAmount.text = formatIndianAmount(totalAmount.toString())

    }


    override fun getItemCount(): Int {
        return ledgerReportList.size
    }


}