package com.bosandroidapp.aopaykit.ui.view.adapter

import android.R
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bosandroidapp.aopaykit.data.model.AdminBankDataItem
import com.bosandroidapp.aopaykit.databinding.BankdetailsitemlayoutBinding
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment.MakePaymentRequestPage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment.MakePaymentRequestPage.Companion.BankAccountNumber
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment.MakePaymentRequestPage.Companion.BankHolderName
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment.MakePaymentRequestPage.Companion.BankIFSCCODE
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment.MakePaymentRequestPage.Companion.BankName
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment.MakePaymentRequestPage.Companion.BranchName
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment.MakePaymentRequestPage.Companion.checkQR
import com.bumptech.glide.Glide

class BankListAdapter(var context: Context, var bankDataList: List<AdminBankDataItem?>?) : RecyclerView.Adapter<BankListAdapter.ViewHolder>() {

    lateinit var dialog: Dialog


    class ViewHolder(var binding: BankdetailsitemlayoutBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankListAdapter.ViewHolder {
        val binding = BankdetailsitemlayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: BankListAdapter.ViewHolder, position: Int) {
        holder.binding.accountHolderName.text = bankDataList!![position]!!.accountName.toString()

        if(bankDataList!![position]!!.qrCodePath.isNullOrBlank()){
            holder.binding.qrview.visibility= View.INVISIBLE
        }
        else{
            holder.binding.qrview.visibility= View.VISIBLE
        }

        holder.binding.status.text = "Active"
        holder.binding.status.setTextColor(context.getColor(R.color.holo_green_dark))

        holder.binding.bankName.text = bankDataList!![position]!!.bankName.toString()
        holder.binding.accountNo.text = bankDataList!![position]!!.accountNumber.toString()
        holder.binding.ifscCode.text = bankDataList!![position]!!.ifscCode.toString()
        holder.binding.branchName.text = bankDataList!![position]!!.branchName.toString()

        /*holder.binding.accountType.text = bankDataList!![position]!!.accountType.toString()
        holder.binding.panNo.text = bankDataList!![position]!!.panNo.toString()
        holder.binding.adminCode.text = bankDataList!![position]!!.adminCode.toString()*/

        holder.binding.makePaymentBtn.setOnClickListener {
            BankName = bankDataList!![position]!!.bankName.toString()
            BankAccountNumber = bankDataList!![position]!!.accountNumber.toString()
            BranchName = bankDataList!![position]!!.branchName.toString()
            BankIFSCCODE = bankDataList!![position]!!.ifscCode.toString()
            BankHolderName = bankDataList!![position]!!.accountName.toString()
            checkQR = !bankDataList!![position]!!.qrCodePath.isNullOrBlank()
            context.startActivity(Intent(context, MakePaymentRequestPage::class.java))
        }

        holder.binding.qrview.setOnClickListener{
            if(bankDataList!![position]!!.qrCodePath.isNullOrBlank() || bankDataList!![position]!!.qrCodePath!!.isBlank() ){

            }else{
                var item = bankDataList!![position]!!
                popupforshowingraiseticketstatus(item)
            }

        }
    }



    override fun getItemCount(): Int {
        return bankDataList!!.size
    }


    fun popupforshowingraiseticketstatus(item:AdminBankDataItem){
        dialog = Dialog(context, R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(com.bosandroidapp.aopaykit.R.layout.qrlayout)

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        dialog.setCanceledOnTouchOutside(false)


        val qrimage = dialog.findViewById<ImageView>(com.bosandroidapp.aopaykit.R.id.qrimage)
        val crossicon = dialog.findViewById<ImageView>(com.bosandroidapp.aopaykit.R.id.crossicon)
        val bankName = dialog.findViewById<TextView>(com.bosandroidapp.aopaykit.R.id.bankName)
        val accountNo = dialog.findViewById<TextView>(com.bosandroidapp.aopaykit.R.id.accountNo)
        val ifsc = dialog.findViewById<TextView>(com.bosandroidapp.aopaykit.R.id.ifsc)
        val branch = dialog.findViewById<TextView>(com.bosandroidapp.aopaykit.R.id.branch)


        bankName.text = item.bankName
        accountNo.text = item.accountNumber
        ifsc.text = item.ifscCode
        branch.text = item.branchName

        crossicon.setOnClickListener {
            dialog.dismiss()
        }

        Glide.with(context).load(item.qrCodePath).into(qrimage)

        dialog.show()

    }

}