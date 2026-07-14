package com.bosandroidapp.aopaykit.ui.slideshow.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.RetailersReportsLayoutBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.formatDateToFullMonth
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.customer.EmiLoanDetailPage
import com.bosandroidapp.aopaykit.ui.view.activity.customer.EmiLoanDetailPage.Companion.LoanId
import com.bosandroidapp.aopaykit.ui.view.activity.customer.EmiLoanDetailPage.Companion.customerCode
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.reports.RetailerCustomerReportsPage
import kotlin.text.equals

class RetailerReportListAdapter(private val ReportsDataList: MutableList<com.bosandroidapp.aopaykit.data.model.loginsignup.reports.ReportsDataItem> = mutableListOf(), var context: Context, var loginType : String
) : RecyclerView.Adapter<RetailerReportListAdapter.ViewHolder>() {

    lateinit var preference: SharedPreference

    class ViewHolder(private val binding: RetailersReportsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var productDetails = binding.productDetails
        var emiAmount = binding.emiAmount
        var loancode = binding.loanCode
        var customername = binding.customername
        var customercode = binding.customercode
        var loanamount = binding.loanamount
        var downpayment = binding.downpayment
        var tenure = binding.tenure
        var paidemi = binding.paidEmi
        var dueemi = binding.duesEmi
        var duedate = binding.duedate
        var startdate = binding.loanstartdate
        var enddate = binding.loanenddate
        var dueMob = binding.custMob
        var customerImage = binding.customerImage
        var reportstatuslayout = binding.reportstatuslayout
        var reportstatus = binding.reportstatus
        var duedatelayout = binding.duedatelayout
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RetailersReportsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }



    override fun getItemCount(): Int = ReportsDataList.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val changeStatus = ReportsDataList[position].recordStatus
        var status = ""

        preference = SharedPreference(context)
        holder.reportstatuslayout.visibility= View.VISIBLE

        if(loginType.equals(ConstantClass.Customer)){
            if(changeStatus.equals("Disbursed")){
                status = "Approved"
            }else{
                status = changeStatus
            }
        }
        else{
            status = changeStatus
        }


        holder.reportstatus.text = status.replaceFirstChar { it.uppercase() }


        if(status.toLowerCase().equals("approved" ,ignoreCase = true)){
            holder.reportstatuslayout.backgroundTintList= context.resources.getColorStateList(R.color.green)
        }

        if(status.toLowerCase().equals("disbursed",ignoreCase = true)){
            holder.reportstatuslayout.backgroundTintList= context.resources.getColorStateList(R.color.blue)
        }

        if(status.toLowerCase().equals("pending",ignoreCase = true)){
            holder.reportstatuslayout.backgroundTintList= context.resources.getColorStateList(R.color.orange)
        }

        if(status.toLowerCase().equals("rejected",ignoreCase = true)||status.toLowerCase().equals("disbursed rejected",ignoreCase = true)){
            holder.reportstatuslayout.backgroundTintList= context.resources.getColorStateList(R.color.red)
        }

        if(status.toLowerCase().equals("close",ignoreCase = true)){
            holder.reportstatuslayout.backgroundTintList= context.resources.getColorStateList(R.color.darkpurple)
        }

        if(status.toLowerCase().equals("settlement",ignoreCase = true)){
            holder.reportstatuslayout.backgroundTintList= context.resources.getColorStateList(R.color.darkgrey)
        }


        holder.productDetails.text = ReportsDataList[position].productDetails
        holder.emiAmount.text = "₹".plus(ReportsDataList[position].emiAmount)
        holder.loancode.text = ReportsDataList[position].loanCode
        holder.customername.text = ReportsDataList[position].customerName
        holder.customercode.text = ReportsDataList[position].customerCode
        holder.loanamount.text = "Loan amount : ₹ ".plus(ReportsDataList[position].loanAmount)
        holder.downpayment.text = "Down payment : ₹ ".plus(ReportsDataList[position].downPayment)
        holder.tenure.text = "Tenure : ".plus(ReportsDataList[position].tenure)
        holder.paidemi.text = "Paid emi : ".plus(ReportsDataList[position].paidEmi)
        holder.dueemi.text = "Due emi : ".plus(ReportsDataList[position].duesEmi)
        holder.duedate.text = safeFormatDate(ReportsDataList[position].dueDate)
        holder.startdate.text = "Start Date : ${safeFormatDate(ReportsDataList[position].startDate)}"
        holder.enddate.text = "End Date : ${safeFormatDate(ReportsDataList[position].endDate)}"
        holder.dueMob.text = ReportsDataList[position].custerMob
        Glide.with(context).load(ReportsDataList[position].customerImage).into(holder.customerImage)


        if(ReportsDataList[position].paidEmi.equals(ReportsDataList[position].tenure)){
            holder.duedatelayout.visibility = View.GONE
        }
        else {
            holder.duedatelayout.visibility = View.VISIBLE
        }


        holder.itemView.setOnClickListener {
            if (context is RetailerCustomerReportsPage) {
                if (status.equals("Disbursed")) {
                    preference.setStringValue(ConstantClass.CustomerCode, ReportsDataList[position].customerCode)
                    LoanId = ReportsDataList[position].loanCode
                    customerCode = ReportsDataList[position].customerCode
                    context.startActivity(Intent(context, EmiLoanDetailPage::class.java))
                }


            }

        }


    }


    fun safeFormatDate(date: String?): String {
        return date?.let { formatDateToFullMonth(it) } ?: "N/A"
    }



}