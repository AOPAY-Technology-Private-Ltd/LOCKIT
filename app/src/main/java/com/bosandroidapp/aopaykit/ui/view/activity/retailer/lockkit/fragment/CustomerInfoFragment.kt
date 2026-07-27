package com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bosandroidapp.aopaykit.databinding.FragmentCustomerInfoDetailsBinding
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.LockKitCustomerDetailsInfoPage.Companion.kitcustomerData

class CustomerInfoFragment : Fragment() {
    private var _binding: FragmentCustomerInfoDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCustomerInfoDetailsBinding.inflate(inflater, container, false)
        setDataOnView()
        return binding.root
    }


    fun setDataOnView(){
        binding.tvFullName.text = kitcustomerData.firstName+" "+kitcustomerData.lastName
        binding.tvEmail.text = kitcustomerData.eMailID
        binding.tvMobile.text = kitcustomerData.primaryMobileNumber
        binding.tvImei.text = kitcustomerData.imeiNumber1
        binding.tvCode.text = kitcustomerData.customerCodes
        binding.tvAddress.text = kitcustomerData.currentAddress
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
