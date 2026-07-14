package com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bosandroidapp.aopaykit.databinding.FragmentCustomerActionBinding

class CustomerActionFragment : Fragment() {
    private var _binding: FragmentCustomerActionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCustomerActionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView setup would go here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
