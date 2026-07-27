package com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.AdminBankDataItem
import com.bosandroidapp.aopaykit.data.model.AdminBankDetailsReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.FragmentBankListPageBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.adapter.BankListAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson


class AdminBankListActivity : Fragment() {

    lateinit var binding: FragmentBankListPageBinding
    lateinit var preference: SharedPreference
    lateinit var viewModel: AuthenticationViewModel
    lateinit var adapter: BankListAdapter
    var bankList : MutableList<AdminBankDataItem?> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentBankListPageBinding.inflate(layoutInflater, container, false)
        preference = SharedPreference(requireContext())
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        hitApiForAdminBankList()
        return binding.root

    }

    fun hitApiForAdminBankList(){
        var  adminBankReq = AdminBankDetailsReq(
            adminCode = ConstantClass.Admin
        )

        Log.d("AdminBankListReq", Gson().toJson(adminBankReq))

        viewModel.GetAdminBankDetailsReq(adminBankReq).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                ConstantClass.dialog.dismiss()
                                if(response!!.status!!.toLowerCase().equals("true",ignoreCase = true)){
                                    Log.d("BankListRes",Gson().toJson(response))
                                    bankList= response.data!!

                                    if(bankList!!.isNotEmpty()){
                                        setAdapterData(bankList)
                                    }
                                    else{
                                        binding.banklist.visibility= View.GONE
                                        binding.notfoundlayout.visibility= View.VISIBLE
                                    }

                                }
                                else{
                                    binding.banklist.visibility= View.GONE
                                    binding.notfoundlayout.visibility= View.VISIBLE
                                }

                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(requireContext())
                    }

                }
            }
        }

    }


    private fun setAdapterData( bankList : List<AdminBankDataItem?>?){
        if(bankList!!.isNotEmpty()){
            binding.banklist.visibility= View.VISIBLE
            binding.notfoundlayout.visibility= View.GONE
            adapter = BankListAdapter(requireActivity(), bankList)
            binding.banklist.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        else {
            binding.banklist.visibility= View.GONE
            binding.notfoundlayout.visibility= View.VISIBLE
        }

    }


}