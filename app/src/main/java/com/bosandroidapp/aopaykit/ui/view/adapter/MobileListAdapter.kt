package com.bosandroidapp.aopaykit.ui.slideshow.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.MobilelistitemlayoutBinding
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.EMICalculationDetailsPage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.EMICalculationDetailsPage.Companion.MobileData
import com.bosandroidapp.aopaykit.data.model.loginsignup.DataItem
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.EMICalculationDetailsPage.Companion.EmiSplitDataModel
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.EMICalculationDetailsPage.Companion.FilterDataEmiSplitDataModel

class MobileListAdapter(private val  MobileDataList : MutableList<DataItem> = mutableListOf(), var context:Context): RecyclerView.Adapter<MobileListAdapter.ViewHolder>() {
    var  selectPosition = -1
    var MobileColorList : MutableList<String> = mutableListOf()


    class ViewHolder (private val binding: MobilelistitemlayoutBinding): RecyclerView.ViewHolder(binding.root) {
      var mobileicon = binding.mobileicon
      var mobilename = binding.mobilename
      var mobileprice = binding.mobileprice
      var selectborder = binding.selectcard
      var colorlayout = binding.colorlayout
     // var colorcard = binding.colorcard
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MobilelistitemlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int = MobileDataList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(MobileDataList[position].imagePath).placeholder(R.drawable.samsung).error(R.drawable.samsung).into(holder.mobileicon)
        holder.mobilename.setText(MobileDataList[position].brandName+" "+MobileDataList[position].modelName)
        if(!MobileDataList[position].mrpPrice.isNullOrBlank() && !MobileDataList[position].variantName.isNullOrBlank()){
            holder.mobileprice.setText("₹ "+MobileDataList[position].mrpPrice+" ("+ MobileDataList[position].variantName +")")
        }
        if(!MobileDataList[position].avlbColors.isNullOrBlank()) {
            holder.colorlayout.removeAllViews()
            // Step 1: Convert comma-separated colors into list
            val colorList = MobileDataList[position].avlbColors
            val MobileColorList = colorList.split(",").map { it.trim() }

            // Step 2: Create one horizontal layout for all colors
            val context = holder.itemView.context
            val subLayout = LinearLayout(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 4, 8, 4)
                }
                orientation = LinearLayout.HORIZONTAL
            }

            // Step 3: Loop through colors and add CardViews
            MobileColorList.forEach { cardItem ->
                val cardView = CardView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._20sdp),
                        context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._20sdp)
                    ).apply {
                        setMargins(8, 0, 0, 0)
                    }

                    radius = context.resources.getDimension(com.intuit.sdp.R.dimen._10sdp)

                    // Try to parse color safely
                    val safeColor = try {
                        Color.parseColor(cardItem)
                    } catch (e: IllegalArgumentException) {
                        Color.TRANSPARENT
                    }

                    setCardBackgroundColor(safeColor)


                }

                subLayout.addView(cardView)
            }

            // Step 4: Add the whole row once
            holder.colorlayout.addView(subLayout)
        }


        if(selectPosition==position){
            holder.selectborder.setBackgroundResource(R.drawable.bg_black_border)
        }
        else{
            holder.selectborder.background = null
        }

        holder.itemView.setOnClickListener{
            selectPosition = position
            notifyDataSetChanged()
            MobileData=MobileDataList[position]
            EmiSplitDataModel.clear()
            FilterDataEmiSplitDataModel.clear()
            context.startActivity(Intent(context, EMICalculationDetailsPage::class.java))

        }

    }

}