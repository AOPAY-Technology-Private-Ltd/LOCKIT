package com.bos.payment.appName.ui.view.travel.airfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bosandroidapp.aopaykit.databinding.FragmentPersonalInfoBinding


class PersonalInfo : Fragment() {

    lateinit var binding : FragmentPersonalInfoBinding

    companion object{
      lateinit var firstname : String
      lateinit var panNumber : String
      lateinit var mobNumber : String
      lateinit var DOB : String
      lateinit var EMAILID : String
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentPersonalInfoBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }

    fun initView(){
        binding.name.text = firstname
        binding.pan.text = panNumber
        binding.mob.text = mobNumber
        binding.dob.text = DOB
        binding.email.text = EMAILID
    }

}