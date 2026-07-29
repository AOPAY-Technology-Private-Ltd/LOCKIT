package com.bosandroidapp.aopaykit.ui.view.activity.customer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.databinding.ActivityCustomerEmipageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerDataItem
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.slideshow.adapter.CustomerEMIDetailsAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson

class CustomerEMIPage : BaseActivity() {
    lateinit var binding : ActivityCustomerEmipageBinding
    lateinit var adapter : CustomerEMIDetailsAdapter
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference
    var customerLoanEmiDetailsList : MutableList<CustomerDataItem?>? = mutableListOf()
    var FilterDataList : MutableList<com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerDataItem?>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerEmipageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }
      

        preference = SharedPreference(this)
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]


        setOnClickListner()

    }


    override fun onResume() {
        super.onResume()
        if(isInternetAvailable(this@CustomerEMIPage)) {
            HitApiForEmiList()
        }
    }



    fun setDataOnView(customerLoanEmiDetailsList : MutableList<com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerDataItem?>?){
        adapter = CustomerEMIDetailsAdapter(this,customerLoanEmiDetailsList)
        binding.showingLoanList.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    fun setOnClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.searcMobile.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val search = s.toString().lowercase().trim()
                    val result = customerLoanEmiDetailsList!!.filter {
                        it!!.brandName!!.lowercase().contains(search) || it.loanCode!!.lowercase().contains(search)
                    }
                    FilterDataList!!.clear()
                    FilterDataList!!.addAll(result)
                    setDataOnView(FilterDataList)
                    adapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            }
        )


    }

    fun HitApiForEmiList(){
        var loanemireq = GetCustomerLoanDetailsReq(
            loancode = "",
            customercode = preference.getStringValue(ConstantClass.CustomerCode,"")
        )
        Log.d("customerloanEmireq",Gson().toJson(loanemireq))

        viewModel.getCustomerLoanEmiDetailsReq(loanemireq).observe(this) { resources ->
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
                                    customerLoanEmiDetailsList = LoanEmiList as MutableList<CustomerDataItem?>?
                                    setDataOnView(customerLoanEmiDetailsList)
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


}