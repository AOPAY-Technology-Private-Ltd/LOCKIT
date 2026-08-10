package com.bosandroidapp.aopaykit.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.data.customeraction.kitinventory.GetKitInventoryListResponse
import com.bosandroidapp.aopaykit.data.customeraction.kitinventory.KitListItem
import com.bosandroidapp.aopaykit.databinding.ItemKitInventoryBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class KitInventoryAdapter(private var kitList: List<KitListItem>) : RecyclerView.Adapter<KitInventoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemKitInventoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKitInventoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = kitList[position]

        with(holder.binding) {
            tvKitCode.text = /*item.serialNumber ?:*/ item.imeiNumber ?: "N/A"
            tvCustomerName.text = item.customerName ?: "N/A"
            tvCustomerCode.text = item.customerCode ?: "N/A"
            
            // Format date
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
            val headerFormat = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())

            val itemDate = try {
                item.installedOn?.let { inputFormat.parse(it) }
            } catch (e: Exception) {
                null
            }

            tvInstalledDate.text = if (itemDate != null) outputFormat.format(itemDate) else item.installedOn ?: "N/A"

            // Handle Month Header
            if (itemDate != null) {
                val currentCal = Calendar.getInstance()
                val itemCal = Calendar.getInstance().apply { time = itemDate }

                val isCurrentMonth = currentCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
                        currentCal.get(Calendar.MONTH) == itemCal.get(Calendar.MONTH)

                currentCal.add(Calendar.MONTH, -1)
                val isLastMonth = currentCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
                        currentCal.get(Calendar.MONTH) == itemCal.get(Calendar.MONTH)

                val currentHeader = when {
                    isCurrentMonth -> "This Month"
                    isLastMonth -> "Last Month"
                    else -> headerFormat.format(itemDate)
                }

                // Logic to show header only if it changes
                var showHeader = false
                if (position == 0) {
                    showHeader = true
                } else {
                    val prevItem = kitList[position - 1]
                    val prevDate = try { prevItem.installedOn?.let { inputFormat.parse(it) } } catch (e: Exception) { null }

                    if (prevDate != null) {
                        val prevCal = Calendar.getInstance().apply { time = prevDate }
                        val isSameMonth = prevCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
                                prevCal.get(Calendar.MONTH) == itemCal.get(Calendar.MONTH)
                        showHeader = !isSameMonth
                    } else {
                        showHeader = true
                    }
                }
                tvMonthHeader.visibility = if (showHeader) View.VISIBLE else View.GONE
                tvMonthHeader.text = currentHeader

            } else {
                tvMonthHeader.visibility = View.GONE
            }

            // Set Initials
            val name = item.customerName ?: ""

            val initials = if (name.isNotEmpty()) {
                val parts = name.split(" ")
                if (parts.size >= 2) {
                    "${parts[0][0]}${parts[1][0]}".uppercase()
                } else {
                    parts[0].take(2).uppercase()
                }
            } else
{
                "NA"
            }
            tvInitials.text = initials
        }
    }

    override fun getItemCount(): Int = kitList.size

    fun updateList(newList: List<KitListItem>) {
        this.kitList = newList
        notifyDataSetChanged()
    }
}
