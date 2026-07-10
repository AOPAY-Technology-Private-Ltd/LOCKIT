package com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bosandroidapp.aopaykit.databinding.FragmentCAISBinding


class CAIS : Fragment() {

    lateinit var binding : FragmentCAISBinding

    companion object{
        lateinit var totalCreditAccount :String
        lateinit var ActiveAccount :String
        lateinit var DefaultAccount :String
        lateinit var ClosedAccount :String
        lateinit var outstandingBalance :String

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentCAISBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    fun initView(){
        binding.creditaccounts.text =  totalCreditAccount
        binding.activeaccount.text =  ActiveAccount
        binding.defaultaccount.text =  DefaultAccount
        binding.closedaccount.text =  ClosedAccount
        binding.outstandingbalance.text =  outstandingBalance

    }

}