package com.bosandroidapp.aopaykit.ui.slideshow.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.formatDateTime
import com.bosandroidapp.aopaykit.constant.ConstantClass.formatDateToDDMMYYYY
import com.bosandroidapp.aopaykit.data.model.OverdueDataItem
import com.bosandroidapp.aopaykit.databinding.DuesEmiReportLayoutBinding

import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.MapActivity
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.MapActivity.Companion.lattitude
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.MapActivity.Companion.longitude

class DueOverdueCustomerReports(var context:Context, var dueoverDueList : MutableList<OverdueDataItem?>?): RecyclerView.Adapter<DueOverdueCustomerReports.ViewHolder>() {

    lateinit var preference : SharedPreference


    class ViewHolder (private val binding: DuesEmiReportLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        var custMob = binding.custMob
        var custAlterMob = binding.custalterMob
        var custRefMob = binding.cusRefMob
        var loanid = binding.loanid
        var emiamount = binding.emiAmount
        var customercode = binding.customercode
        var customername = binding.customername
        var locationlayout = binding.locationlayout
        var duedate = binding.duedate
        var loanamount = binding.loanamount
        var downpayment = binding.downpayment
        var tenure = binding.tenure
        var paidEmi = binding.paidEmi
        var duesEmi = binding.duesEmi
        var status = binding.status
        var image = binding.customerImage
        var synctime = binding.synctime

        var customermobImage = binding.customermobtxt
        var customeraltremobImag = binding.customeraltremobtxt
        var customerRefmobImag = binding.customerRefmobtxt
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DuesEmiReportLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int = dueoverDueList!!.size



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        preference = SharedPreference(context)
        holder.status.text = dueoverDueList!![position]!!.loanStatus
        holder.loanid.text = dueoverDueList!![position]!!.loanCode
        holder.emiamount.text = "₹ %.2f".format(dueoverDueList!![position]!!.emiAmount)
        holder.customername.text = dueoverDueList!![position]!!.customerName
        holder.custMob.text = "${dueoverDueList!![position]!!.primaryMobileNumber}(Main)"
        holder.custRefMob.text = "${dueoverDueList!![position]!!.refmobileNo}(Ref)"
        holder.custAlterMob.text = "${dueoverDueList!![position]!!.alternateMobileNumber}(Alter)"
        holder.customercode.text = dueoverDueList!![position]!!.customerCode
        holder.duedate.text = formatDateToDDMMYYYY(dueoverDueList!![position]!!.dueDate!!)

        if(dueoverDueList!![position]!!.lastLocationDate!= null){
            holder.synctime.text = "Sync at ${formatDateTime(dueoverDueList!![position]!!.lastLocationDate!!)}"
        }

        holder.loanamount.text = "LoanAmount : ₹ %.2f".format(dueoverDueList!![position]!!.loanAmount)
        holder.downpayment.text = "DownPayment : ₹ %.2f".format(dueoverDueList!![position]!!.downPayment)
        holder.tenure.text =  "Tenure : ${dueoverDueList!![position]!!.tenure}"
        holder.paidEmi.text = "Paid emi : ${dueoverDueList!![position]!!.paidEMI}"
        holder.duesEmi.text = "Due emi : ${dueoverDueList!![position]!!.dueEMI}"
        var imageurl = ConstantClass.BASE_URL_IMAGE + dueoverDueList!![position]!!.customerPhoto
        Glide.with(context).load(imageurl).into(holder.image)

        holder.locationlayout.setOnClickListener {
            if(dueoverDueList!![position]!!.latitude!=null&& dueoverDueList!![position]!!.longitude!=null){
                lattitude = dueoverDueList!![position]!!.latitude!!
                longitude = dueoverDueList!![position]!!.longitude!!
                context.startActivity(Intent(context,MapActivity::class.java))
            }
       }

        holder.custMob.setOnLongClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text",  holder.custMob.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
            true
        }

        holder.custAlterMob.setOnLongClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text",  holder.custAlterMob.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
            true
        }

        holder.custRefMob.setOnLongClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text",  holder.custRefMob.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "Text Copied", Toast.LENGTH_SHORT).show()
            true
        }


        holder.customermobImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${holder.custMob.text.toString()}")
            }
            context.startActivity(intent)
        }


        holder.customeraltremobImag.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${holder.custAlterMob.text.toString()}")
            }
            context.startActivity(intent)
        }


        holder.customerRefmobImag.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${holder.custRefMob.text.toString()}")
            }
            context.startActivity(intent)
        }


    }



}