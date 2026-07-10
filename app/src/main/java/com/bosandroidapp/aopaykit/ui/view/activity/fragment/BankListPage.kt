package com.bosandroidapp.aopaykit.ui.view.activity.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.databinding.FragmentBankListBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.BankDataItem
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.adapter.BankDetailsListAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson


class BankListPage : Fragment() {
    lateinit var binding: FragmentBankListBinding
    lateinit var preference : SharedPreference
    lateinit var viewModel: AuthenticationViewModel


    companion object{
        var bankDataList: List<BankDataItem?>? = arrayListOf()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentBankListBinding.inflate(layoutInflater, container, false)

        preference = SharedPreference(requireContext())
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

        hitApiForReports()
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        if(!bankDataList.isNullOrEmpty()){
            var adapter = BankDetailsListAdapter(requireContext(), bankDataList)
            binding.banklistview.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }


    fun hitApiForReports(){

        var req = com.bosandroidapp.aopaykit.data.model.AddBankAccountReq(
            action = "GET",
            retailerID = preference.getStringValue(ConstantClass.RetailerCode, ""),
            accountNumber = "",
            accountName = "",
            bankName = "",
            ifscCode = "",
            branchName = "",
            branchAddress = "",
            mobilenumber = "",
            emailID = ""
        )

        Log.d("GetBankListReq", Gson().toJson(req))

        viewModel.getAddBankAccountReq(req).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let {
                                    response ->
                                ConstantClass.dialog.dismiss()
                                if(response!!.statuss.equals("True")){
                                    Log.d("BankListRes",Gson().toJson(response))
                                    bankDataList = response?.data!!
                                    if(!bankDataList.isNullOrEmpty()){
                                        var adapter = BankDetailsListAdapter(requireContext(), bankDataList)
                                        binding.banklistview.adapter = adapter
                                        adapter.notifyDataSetChanged()
                                        binding.notfoundimage.visibility= View.GONE
                                        binding.banklistview.visibility = View.VISIBLE
                                    }else{
                                        binding.notfoundimage.visibility= View.VISIBLE
                                        binding.banklistview.visibility = View.GONE
                                    }

                                }
                                else{
                                    binding.notfoundimage.visibility= View.VISIBLE
                                    binding.banklistview.visibility = View.GONE
                                }

                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(requireContext())
                    }

                }
            }
        }

    }




}