package com.bosandroidapp.aopaykit.ui.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.data.model.kitplan.PurchaseHistory
import com.bosandroidapp.aopaykit.databinding.KitPlanHistoryItemlayoutBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class PurchaseHistoryAdapter(private val purchaseList: List<PurchaseHistory>) : RecyclerView.Adapter<PurchaseHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: KitPlanHistoryItemlayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = KitPlanHistoryItemlayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = purchaseList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(purchaseList[position])
    }

    @SuppressLint("SetTextI18n")
    private fun ViewHolder.bind(item: PurchaseHistory) = with(binding) {

        tvPurchaseCode.text = item.purchaseCode
        tvPurchaseDate.text = formatDate(item.purchaseDate)

        tvPlanName.text = item.planName
        tvPlanCode.text = "Plan Code : ${item.planCode}"

        tvPlanAmount.text = formatAmount(item.planAmount)
        tvDiscount.text = "- ${formatAmount(item.discountAmount)}"
        tvGST.text = formatAmount(item.gstAmount)
        tvNetAmount.text = formatAmount(item.netAmount)

        tvPaymentMode.text = "Payment Mode : ${item.paymentMode}"
        tvTransactionNo.text = "Transaction No : ${item.transactionNo}"

        tvValidity.text =
            "Valid : ${formatDate(item.planStartDate)} - ${formatDate(item.planEndDate)}"

        tvStatus.text = item.paymentStatus

        when (item.paymentStatus?.uppercase()) {
            "SUCCESS" -> {
                tvStatus.setBackgroundResource(R.drawable.bg_status_success)
                tvStatus.setTextColor(
                    ContextCompat.getColor(root.context, android.R.color.white)
                )
            }

            "FAILED" -> {
                tvStatus.setBackgroundResource(R.drawable.bg_status_failed)
                tvStatus.setTextColor(
                    ContextCompat.getColor(root.context, android.R.color.white)
                )
            }

            "PENDING" -> {
                tvStatus.setBackgroundResource(R.drawable.bg_status_pending)
                tvStatus.setTextColor(
                    ContextCompat.getColor(root.context, android.R.color.black)
                )
            }

            else -> {
                tvStatus.setBackgroundResource(R.drawable.bg_status_pending)
            }
        }

        if (item.isActive == true) {
            tvValidity.append(" (Active)")
        } else {
            tvValidity.append(" (Expired)")
        }
    }



    private fun formatAmount(amount: Double?): String {
        val value = amount ?: 0.0
        return NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(value)
    }

    private fun formatDate(date: String?): String {

        if (date.isNullOrEmpty()) return "-"

        return try {
            val input =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val output =
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

            output.format(input.parse(date)!!)
        } catch (e: Exception) {
            date
        }
    }
}