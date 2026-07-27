package com.bosandroidapp.aopaykit.ui.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.databinding.ItemSubActionBinding
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment.CustomerActionFragment
import com.google.gson.Gson

class SubActionAdapter(
    private var subActionList: List<CustomerActionFragment.SubAction>,
    private val onAllItemsSelected: (Boolean) -> Unit) : RecyclerView.Adapter<SubActionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemSubActionBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubActionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subAction = subActionList[position]
        Log.d("AdapterListSubAction", Gson().toJson(subAction))

        with(holder.binding) {
            tvSubActionName.text = subAction.subactionName
            cbSubAction.setOnCheckedChangeListener(null)
            cbSubAction.isChecked = subAction.active
            
            cbSubAction.setOnCheckedChangeListener { _, isChecked ->
                subAction.active = isChecked
                checkIfAllSelected()
            }

            root.setOnClickListener {
                cbSubAction.isChecked = !cbSubAction.isChecked
            }

        }
    }


    override fun getItemCount(): Int = subActionList.size


    fun selectAll(isSelected: Boolean) {
        subActionList.forEach { it.active = isSelected }
        notifyDataSetChanged()
    }


    private fun checkIfAllSelected() {
        val allSelected = subActionList.all { it.active }
        onAllItemsSelected(allSelected)
    }

}
