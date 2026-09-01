package com.bosandroidapp.aopaykit.ui.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPlanListDataItem
import com.bosandroidapp.aopaykit.databinding.ItemKitPlanBinding

class KitPlanAdapter(private var plans: List<KitPlanListDataItem?>?, private val onPlanSelected: (KitPlanListDataItem) -> Unit) :
    RecyclerView.Adapter<KitPlanAdapter.ViewHolder>() {

    init {
        plans = plans?.sortedByDescending { it?.isMostPopular == true }
    }

    inner class ViewHolder(val binding: ItemKitPlanBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKitPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plan = plans!![position]
        if (plan == null) return

        with(holder.binding) {
            kitname.text = plan.planName ?: ""
            tvKitCount.text = plan.noOfKits.toString()
            tvTotalPrice.text = "₹${String.format("%,.2f", plan.planAmount)}"
            tvPerKitPrice.text = "₹${String.format("%.2f", plan.pricePerKit ?: 0.0)} / kits"
            
            tvSaveTag.text = "Save ${plan.discountPercent?.toInt() ?: 0}%"
            tvSaveTag.visibility = if ((plan.discountPercent ?: 0.0) > 0) View.VISIBLE else View.GONE
            
            tvPopularTag.visibility = if (plan.isMostPopular == true) View.VISIBLE else View.GONE

            if (plan.isSelected) {
                clMain.setBackgroundResource(R.drawable.bg_plan_item_selected)
            } else {
                clMain.setBackgroundResource(R.drawable.bg_plan_item)
            }

            root.setOnClickListener {
                plans!!.forEach { it?.isSelected = false }
                plan.isSelected = true
                notifyDataSetChanged()
                onPlanSelected(plan)
            }
        }
    }

    override fun getItemCount() = plans?.size ?: 0

}
