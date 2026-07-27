package com.bosandroidapp.aopaykit.ui.view.activity.customer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.data.model.CustomerEMIDataItem
import com.bosandroidapp.aopaykit.data.model.CustomerEmiStatusReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityCustomerEmistatusReportBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.adapter.CustomerEmiStatusAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson

class CustomerEMIStatusReport : AppCompatActivity() {

    lateinit var binding: ActivityCustomerEmistatusReportBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference
    var customerLoanEmiDetailsList : MutableList<CustomerEMIDataItem?>? = mutableListOf()
    lateinit var adapter : CustomerEmiStatusAdapter


    companion object{
        var loanCode : String = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerEmistatusReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        preference = SharedPreference(this)
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

        setonClickListner()

    }


    fun  setonClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

    }


    override fun onResume() {
        super.onResume()
        if(isInternetAvailable(this@CustomerEMIStatusReport)) {
            HitApiForEmiList()
        }
    }


    fun HitApiForEmiList(){
        var loanemireq = CustomerEmiStatusReq(
            loanCode = loanCode
        )
        Log.d("customerloanEmireq",Gson().toJson(loanemireq))

        viewModel.LoanEmIScheduleWithStatusReq(loanemireq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let {
                                response ->
                                Log.d("customerLoanemiresp", Gson().toJson(response))

                                if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                                    ConstantClass.dialog.dismiss()
                                    var LoanEmiList = response.data
                                    customerLoanEmiDetailsList = LoanEmiList as MutableList<CustomerEMIDataItem?>?
                                    if(!customerLoanEmiDetailsList.isNullOrEmpty() && customerLoanEmiDetailsList!!.size>0){
                                        binding.showingLoanList.visibility=View.VISIBLE
                                        binding.notfoundimage.visibility= View.GONE
                                        setDataOnView(customerLoanEmiDetailsList)
                                    }else{
                                       binding.showingLoanList.visibility=View.GONE
                                       binding.notfoundimage.visibility= View.VISIBLE
                                    }

                                }

                            }
                        }

                    }

                    ApiStatus.ERROR -> {
                        if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                            ConstantClass.dialog.dismiss()
                        }

                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(this)
                    }

                }
            }
        }

    }


    fun setDataOnView(customerLoanEmiDetailsList : MutableList<CustomerEMIDataItem?>?){
        adapter = CustomerEmiStatusAdapter(this,customerLoanEmiDetailsList!!)
        binding.showingLoanList.adapter = adapter
        adapter.notifyDataSetChanged()
    }


}