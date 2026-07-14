package com.bosandroidapp.aopaykit.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.constant.ConstantClass.twoDecimal
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.DataItem
import com.bosandroidapp.aopaykit.databinding.LoanSettlementItemLayoutBinding


class LoanSettlementAdapter(var context:Context,var settlementLoanList: MutableList<DataItem?>) : RecyclerView.Adapter<LoanSettlementAdapter.ViewHolder>() {


    class ViewHolder (var binding:LoanSettlementItemLayoutBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanSettlementAdapter.ViewHolder {
        val binding = LoanSettlementItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     holder.binding.tvProductName.text = settlementLoanList[position]!!.productName
     holder.binding.tvCustomerName.text = settlementLoanList[position]!!.customerName
     holder.binding.tvCustomerCode.text = "Customer Code : ${settlementLoanList[position]!!.customerCode}"
     holder.binding.tvLoanAmount.text = "₹ ${settlementLoanList[position]!!.loanAmount!!.twoDecimal()}"
     holder.binding.tvProcessingFees.text = "Processing Fees : ₹${settlementLoanList[position]!!.processingFees!!.twoDecimal()}"
     holder.binding.tvHoldingAmount.text = "Holding Amount : ₹${settlementLoanList[position]!!.holdingAmount!!.twoDecimal()}"
     holder.binding.tvDownPayment.text = "Down Payment : ₹${settlementLoanList[position]!!.downPayment!!.twoDecimal()}"
     holder.binding.tvMembership.text = "Membership : ₹${settlementLoanList[position]!!.memberShip!!.twoDecimal()}"
     holder.binding.tvSettlementAmount.text = "Settlement Amount : ₹${settlementLoanList[position]!!.settlementAmount!!.twoDecimal()}"
    }


    override fun getItemCount(): Int {
     return  settlementLoanList.size
    }


}