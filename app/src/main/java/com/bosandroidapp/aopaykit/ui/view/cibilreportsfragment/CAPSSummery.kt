package com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bosandroidapp.aopaykit.databinding.FragmentCAPSSummeryBinding


class CAPSSummery : Fragment() {

   lateinit var binding : FragmentCAPSSummeryBinding


   companion object{
       lateinit var  Last7Days :String
       lateinit var  Last30Days :String
       lateinit var  Last90Days :String
       lateinit var  Last180Days :String
   }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentCAPSSummeryBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    fun init(){
       binding.Last7Days.text  =   Last7Days
       binding.Last30Days.text =  Last30Days
       binding.Last90Days.text =  Last90Days
       binding.Last180Days.text = Last180Days

    }


}