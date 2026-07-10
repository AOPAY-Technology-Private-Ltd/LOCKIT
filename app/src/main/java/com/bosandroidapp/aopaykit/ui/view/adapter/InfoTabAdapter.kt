package com.bosandroidapp.aopaykit.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ItemInfoTabBinding

class InfoTabAdapter(private val tabs: List<String>, private val onTabSelected: (String) -> Unit) : RecyclerView.Adapter<InfoTabAdapter.TabViewHolder>() {

    private var selectedPosition = 0

    inner class TabViewHolder(val binding: ItemInfoTabBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val binding = ItemInfoTabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        val tabName = tabs[position]
        holder.binding.tvTabName.text = tabName

        if (position == selectedPosition) {
            holder.binding.tvTabName.setBackgroundResource(R.drawable.bg_tab_selected)
            holder.binding.tvTabName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else {
            holder.binding.tvTabName.setBackgroundResource(R.drawable.bg_tab_unselected)
            holder.binding.tvTabName.setTextColor(android.graphics.Color.parseColor("#3B82F6"))
        }

        holder.itemView.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
            onTabSelected(tabName)
        }
    }

    override fun getItemCount(): Int = tabs.size

}
