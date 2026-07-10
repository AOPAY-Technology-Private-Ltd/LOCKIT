package com.bosandroidapp.aopaykit.ui.view.activity.retailer.reports

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
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.PayoutDataItem
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.PayoutReportReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityPayoutReportBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.adapter.PayoutReportAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson

class PayoutReport : AppCompatActivity() {
    lateinit var binding: ActivityPayoutReportBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference: SharedPreference
    var payoutReportList: List<PayoutDataItem?>? = mutableListOf()
    lateinit var adapter : PayoutReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayoutReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)


        setOnClickListner()
    }

    override fun onResume() {
        super.onResume()

        setSpinner()
    }


    fun setOnClickListner() {
        binding.back.setOnClickListener {
            finish()
        }

        binding.statusspinner.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var selected = binding.statusspinner.selectedItem.toString()
                 hitApiForPayoutReport(selected)

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {


            }


        }

    }

    private fun setSpinner(){
        val adapter = ArrayAdapter.createFromResource(this,  R.array.payoutreportstatustype, R.layout.mobilenamelayout)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.statusspinner.adapter = adapter

    }


    fun hitApiForPayoutReport(status:String){

        var payoutreq = PayoutReportReq(
            registrationId = preference.getStringValue(ConstantClass.RetailerCode, ""),
            status = status
        )

        Log.d("payoutreq", Gson().toJson(payoutreq))

        viewModel.getPayoutReportReq(payoutreq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("payoutresp", Gson().toJson(response))
                                ConstantClass.dialog.dismiss()
                                payoutReportList = response.data!!

                                if(payoutReportList!!.isNotEmpty()){
                                    binding.notfoundimage.visibility=View.GONE
                                    binding.showreports.visibility=View.VISIBLE
                                    adapter=PayoutReportAdapter(this@PayoutReport,payoutReportList)
                                    binding.showreports.adapter=adapter
                                    adapter.notifyDataSetChanged()

                                }
                                else{
                                    binding.notfoundimage.visibility=View.VISIBLE
                                    binding.showreports.visibility=View.GONE
                                }

                            }

                        }

                    }


                    ApiStatus.ERROR -> {
                        binding.notfoundimage.visibility=View.VISIBLE
                        binding.showreports.visibility=View.GONE
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