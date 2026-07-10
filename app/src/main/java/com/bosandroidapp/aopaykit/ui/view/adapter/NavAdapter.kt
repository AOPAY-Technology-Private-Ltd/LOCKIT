package com.bosandroidapp.aopaykit.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.data.model.NavParentItem

class NavAdapter(
    private val context: Context,
    private val items: List<NavParentItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        var count = 0
        items.forEach {
            count++ // for parent
            if (it.isExpanded) count += it.children.size
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        var index = 0
        for (parent in items) {
            if (index == position) return 0 // parent
            index++
            if (parent.isExpanded) {
                for (child in parent.children) {
                    if (index == position) return 1 // child
                    index++
                }
            }
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_nav_parent, parent, false)
            ParentViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_nav_child, parent, false)
            ChildViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var index = 0
        for (pIndex in items.indices) {
            val parent = items[pIndex]
            if (index == position && holder is ParentViewHolder) {
                holder.bind(parent)
                holder.itemView.setOnClickListener {
                    parent.isExpanded = !parent.isExpanded
                    notifyDataSetChanged()
                }
                return
            }
            index++
            if (parent.isExpanded) {
                for (child in parent.children) {
                    if (index == position && holder is ChildViewHolder) {
                        holder.bind(child)
                        holder.itemView.setOnClickListener {
                            onItemClick(child)
                        }
                        return
                    }
                    index++
                }
            }
        }
    }

    inner class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.parentTitle)
        fun bind(item: NavParentItem) {
            title.text = if (item.isExpanded) "${item.title}" else "${item.title}"
        }
    }

    inner class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.childTitle)
        fun bind(text: String) {
            title.text = text
        }
    }
}
