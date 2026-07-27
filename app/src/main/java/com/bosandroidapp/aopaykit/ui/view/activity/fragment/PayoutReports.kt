package com.bosandroidapp.aopaykit.ui.view.activity.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.FragmentPayoutReportsBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.DataItem
import com.bosandroidapp.aopaykit.data.model.RetailerWalletReportReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.adapter.RetailerWalletAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson


class PayoutReports : Fragment() {
    lateinit var binding: FragmentPayoutReportsBinding
    lateinit var preference : SharedPreference
    lateinit var viewModel: AuthenticationViewModel
    var reportDataList: List<DataItem?>? = arrayListOf()


    companion object{
        var reportType : String = ""
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPayoutReportsBinding.inflate(layoutInflater, container, false)

        preference = SharedPreference(requireContext())
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
       // hitApiForReports(reportType)
        return binding.root
    }


    override fun onResume() {
        super.onResume()

        hitApiForReports(reportType)
    }


    fun setview( ){
        // for report.........................................................................................
        val adapter = ArrayAdapter.createFromResource(requireContext(),  R.array.cibilreporttype, R.layout.mobilenamelayout)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.reporttype.adapter = adapter
        var isSpinnerFirstCall = true // declare outside the listener
        binding.reporttype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (isSpinnerFirstCall) {
                    isSpinnerFirstCall = false
                    return // skip the first auto-call
                }

                val selectedItem = parent.getItemAtPosition(position).toString()
                setDataOnView(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // nothing
            }
        }

        setDataOnView(binding.reporttype.selectedItem.toString().trim())

    }


    fun setDataOnView(status: String){

        if(!reportDataList.isNullOrEmpty()){

            val filteredList = if (status.equals("All", ignoreCase = true)) {
                reportDataList ?: emptyList()
            }
            else {
                reportDataList?.filter {
                    it?.transactionStatus.equals(status, ignoreCase = true)
                } ?: emptyList()
            }

            if(!filteredList.isNullOrEmpty()){
                binding.notfoundimage.visibility= View.GONE
                binding.showreports.visibility = View.VISIBLE
                var list = filteredList.reversed()
                Log.d("reversereportList", Gson().toJson( filteredList.reversed()))
                var adapter = RetailerWalletAdapter(requireContext(), list)
                binding.showreports.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            else{
                binding.notfoundimage.visibility= View.VISIBLE
                binding.showreports.visibility = View.GONE
            }

        }
        else{
            binding.notfoundimage.visibility= View.VISIBLE
            binding.showreports.visibility = View.GONE
        }

    }


    fun hitApiForReports(reportType:String){

        var request = RetailerWalletReportReq(
            retailerID = preference.getStringValue(ConstantClass.RetailerCode,""),
            reportType = reportType,
            fromDate = null,
            toDate = null
        )

        Log.d("payoutreportreq", Gson().toJson(request))

        viewModel.getRetailerWalletReport(request).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let {
                                    response ->
                                ConstantClass.dialog.dismiss()
                                Log.d("payoutreportres",Gson().toJson(response))
                                if(response!!.status.equals("True")){
                                    if(!response.data.isNullOrEmpty()){
                                        reportDataList = response.data
                                    }
                                    setview()
                                }
                                else{
                                    binding.notfoundimage.visibility= View.VISIBLE
                                    binding.showreports.visibility = View.GONE
                                    setview()
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


}