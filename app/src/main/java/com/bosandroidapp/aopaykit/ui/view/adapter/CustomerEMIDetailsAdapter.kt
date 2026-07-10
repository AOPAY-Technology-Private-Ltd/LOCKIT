package com.bosandroidapp.aopaykit.ui.slideshow.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.databinding.LoandetailesItemlayoutBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.formatIndianAmount

import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.customer.CustomerEMIStatusReport
import com.bosandroidapp.aopaykit.ui.view.activity.customer.EmiLoanDetailPage
import com.bosandroidapp.aopaykit.ui.view.activity.customer.EmiLoanDetailPage.Companion.LoanId
import com.bosandroidapp.aopaykit.ui.view.activity.customer.EmiLoanDetailPage.Companion.customerCode

class CustomerEMIDetailsAdapter( var context:Context,var customerLoanEmiDetailsList : MutableList<com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerDataItem?>?): RecyclerView.Adapter<CustomerEMIDetailsAdapter.ViewHolder>() {

    lateinit var preference : SharedPreference

    class ViewHolder (private val binding: LoandetailesItemlayoutBinding): RecyclerView.ViewHolder(binding.root) {
        var viewdetailscard = binding.viewdetailscard
        var brandName = binding.brandName
        var loanid = binding.loanid
        var emiamount = binding.emiamount
        var customercode = binding.customercode
        var customername = binding.customername
        var loanstatuscard = binding.loanstatuscard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LoandetailesItemlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = customerLoanEmiDetailsList!!.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        preference = SharedPreference(context)
        holder.brandName.text= customerLoanEmiDetailsList!![position]!!.brandName
        holder.loanid.text= customerLoanEmiDetailsList!![position]!!.loanCode
        holder.emiamount.text= formatIndianAmount(customerLoanEmiDetailsList!![position]!!.emiAmount?.toString().takeIf { !it.isNullOrBlank() } ?: "0")
        holder.customercode.text= preference.getStringValue(ConstantClass.CustomerCode,"")
        holder.customername.text= preference.getStringValue(ConstantClass.FirstName,"") .plus(" ") .plus(preference.getStringValue(
            ConstantClass.LastName,""))

       holder.viewdetailscard.setOnClickListener {
           LoanId= customerLoanEmiDetailsList!![position]!!.loanCode!!
           customerCode= customerLoanEmiDetailsList!![position]!!.customerCode!!
           context.startActivity(Intent(context, EmiLoanDetailPage::class.java))

       }

       holder.loanstatuscard.setOnClickListener {
           CustomerEMIStatusReport.loanCode= customerLoanEmiDetailsList!![position]!!.loanCode!!
           context.startActivity(Intent(context, CustomerEMIStatusReport::class.java))
       }


    }

}