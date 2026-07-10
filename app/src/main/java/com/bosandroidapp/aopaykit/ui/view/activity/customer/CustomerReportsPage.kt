package com.bosandroidapp.aopaykit.ui.view.activity.customer

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityCustomerReportsPageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.GetReportsReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.slideshow.adapter.RetailerReportListAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson

class CustomerReportsPage : AppCompatActivity() {
    lateinit var binding: ActivityCustomerReportsPageBinding
    lateinit var preference: SharedPreference
    lateinit var viewModel: AuthenticationViewModel
    var ReportDataList: MutableList<com.bosandroidapp.aopaykit.data.model.loginsignup.reports.ReportsDataItem> =
        mutableListOf()
    lateinit var reportAdapter: RetailerReportListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerReportsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBarsInsets.left,
                0,
                systemBarsInsets.right,
                systemBarsInsets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)

        setview()
        setclicklistner()

    }

    override fun onResume() {
        super.onResume()
        hitApiForGetReports(binding.reporttype.selectedItem.toString())
    }


    fun setview() {
        val adapter = ArrayAdapter.createFromResource(this, R.array.customerreporttype, R.layout.mobilenamelayout)
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
                if (selectedItem.equals("Approved")) {
                    hitApiForGetReports("Disbursed")
                } else {
                    hitApiForGetReports(selectedItem)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // nothing
            }
        }


    }


    fun setclicklistner() {

        binding.back.setOnClickListener {
            finish()
        }

    }


    fun hitApiForGetReports(reporttype: String) {
        var customerCode = preference.getStringValue(ConstantClass.CustomerCode, "")
        var recordStatus = reporttype

        var reportreq = GetReportsReq(
            retailercode = "",
            recordStatus = recordStatus,
            customercode = customerCode,
            fromDate = null,
            toDate = null
        )
        Log.d("ReportReq", Gson().toJson(reportreq))
        viewModel.getReportsReq(reportreq).observe(this) { resources ->

            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                ConstantClass.dialog.dismiss()
                                ReportDataList = response.data!!.toMutableList()
                                Log.d("ReportResponse", Gson().toJson(ReportDataList))
                                if (ReportDataList.size > 0) {
                                    binding.showreports.visibility = View.VISIBLE
                                    binding.notfoundimage.visibility = View.GONE
                                    reportAdapter = RetailerReportListAdapter(ReportDataList, this, ConstantClass.Customer)
                                    binding.showreports.adapter = reportAdapter
                                    reportAdapter.notifyDataSetChanged()

                                } else {
                                    binding.showreports.visibility = View.GONE
                                    binding.notfoundimage.visibility = View.VISIBLE
                                }
                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(this)
                    }

                }

            }

        }


    }


}