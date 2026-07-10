package com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bosandroidapp.aopaykit.databinding.FragmentBureauScoreBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class BureauScore : Fragment() {
     lateinit var binding : FragmentBureauScoreBinding


     companion object{
          var userScore : Float = 0f

     }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentBureauScoreBinding.inflate(layoutInflater,container,false)
        initView()
        return binding.root
    }



    fun  initView(){

        // Standard CIBIL ranges
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(250f, "300–549 Poor"))       // Poor
        entries.add(PieEntry(100f, "550–649 Fair"))       // Fair
        entries.add(PieEntry(100f, "650–749 Good"))       // Good
        entries.add(PieEntry(151f, "750–900 Excellent"))  // Excellent

        // Colors for each range
        val colors = listOf(
            Color.RED,        // Poor
            Color.parseColor("#FF9800"), // Good (Orange)
            Color.parseColor("#FFC107"), // Very Good (Yellow)
            Color.parseColor("#4CAF50")  // Excellent (Green)
        )


        val dataSet = PieDataSet(entries, "CIBIL Score Ranges")
        dataSet.colors = colors
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        dataSet.setDrawValues(false)

        val data = PieData(dataSet)

        binding.cibilPieChart.data = data


        binding.cibilPieChart.centerText = "Score\n$userScore"
        binding.cibilPieChart.setCenterTextSize(18f)

        // Remove description & legend
        binding.cibilPieChart.description.isEnabled = false
        binding.cibilPieChart.legend.isEnabled = true
        binding.cibilPieChart.setUsePercentValues(false)
        binding.cibilPieChart.setEntryLabelTextSize(10f)
        binding.cibilPieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD)

        // Animate
        binding.cibilPieChart.animateY(1000)
        binding.cibilPieChart.invalidate()


    }

}