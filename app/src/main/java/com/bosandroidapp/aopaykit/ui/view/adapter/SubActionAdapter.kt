package com.bosandroidapp.aopaykit.ui.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.data.SubactionListItem
import com.bosandroidapp.aopaykit.databinding.ItemSubActionBinding
import com.google.gson.Gson
import java.util.Locale

class SubActionAdapter(
    private var originalList: List<SubactionListItem?>?,
    private val onAllItemsSelected: (Boolean) -> Unit
) : RecyclerView.Adapter<SubActionAdapter.ViewHolder>() {

    private var filteredList: MutableList<SubactionListItem?> = mutableListOf()

    init {
        originalList?.let { filteredList.addAll(it) }
    }

    inner class ViewHolder(val binding: ItemSubActionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubActionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subAction = filteredList[position] ?: return
        Log.d("AdapterListSubAction", Gson().toJson(subAction))

        with(holder.binding) {
            tvSubActionName.text = subAction.subactionName
            cbSubAction.setOnCheckedChangeListener(null)
            cbSubAction.isChecked = subAction.active ?: false
            
            cbSubAction.setOnCheckedChangeListener { _, isChecked ->
                subAction.active = isChecked
                checkIfAllSelected()
            }

            root.setOnClickListener {
                cbSubAction.isChecked = !cbSubAction.isChecked
            }
        }
    }

    override fun getItemCount(): Int = filteredList.size

    fun filter(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())
        filteredList.clear()
        if (lowerCaseQuery.isEmpty()) {
            originalList?.let { filteredList.addAll(it) }
        } else {
            originalList?.forEach { item ->
                if (item != null) {
                    val appName = item.subactionName?.lowercase(Locale.getDefault()) ?: ""
                    if (appName.contains(lowerCaseQuery)) {
                        filteredList.add(item)
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    fun selectAll(isSelected: Boolean) {
        // If we want "Select All" to only affect the currently filtered items:
        filteredList.forEach { it?.active = isSelected }
        
        // OR if it should always affect the whole list:
        // originalList!!.forEach { it!!.active = isSelected }
        
        notifyDataSetChanged()
        checkIfAllSelected()
    }

    private fun checkIfAllSelected() {
        val allSelected = originalList?.all { it?.active == true } ?: false
        onAllItemsSelected(allSelected)
    }
}
