package com.bosandroidapp.aopaykit.kioskmode

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.R

class AppAdapter(
    private val context: Context,
    private val appList: List<PaymentApp>
) : RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.app_icon)
        val name: TextView = itemView.findViewById(R.id.app_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.app_item, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = appList[position]
        holder.icon.setImageDrawable(app.icon)
        holder.name.text = app.appName

        holder.itemView.setOnClickListener {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(app.packageName)
            if (launchIntent != null) {
                context.startActivity(launchIntent)
            } else {
                Toast.makeText(context, "App not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int = appList.size
}