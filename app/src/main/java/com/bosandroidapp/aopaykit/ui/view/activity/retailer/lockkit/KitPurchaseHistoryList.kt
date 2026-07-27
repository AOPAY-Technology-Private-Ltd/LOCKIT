package com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPurchaseHistoryRequest
import com.bosandroidapp.aopaykit.data.model.kitplan.PurchaseHistory
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.TransactionHistoryReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityKitPurchaseHistoryListBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.adapter.PurchaseHistoryAdapter
import com.bosandroidapp.aopaykit.ui.view.adapter.TransactionReportAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson

class KitPurchaseHistoryList : AppCompatActivity() {
    lateinit var binding : ActivityKitPurchaseHistoryListBinding
    private lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference
    var historyList : List<PurchaseHistory> = listOf()
    var planKitAdapter : PurchaseHistoryAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKitPurchaseHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        hitApiForGetTransactionReport()
        setOnClickListner()

    }


    fun setOnClickListner(){
        binding.back.setOnClickListener {
            finish()
        }

    }


    fun hitApiForGetTransactionReport() {
        var retailerCode = preference.getStringValue(ConstantClass.RetailerCode,"")

        var reportreq = KitPurchaseHistoryRequest(
            companyCode = ConstantClass.ClientCode,
            retailerCode = retailerCode,

        )
        Log.d("kitHistoryReq", Gson().toJson(reportreq))

        viewModel.getPurchaseHistoryRequest(reportreq).observe(this){
                resources->resources.let {
            when(it.apiStatus){
                ApiStatus.SUCCESS -> {
                    it.data?.let { users ->
                        users.body()?.let { response ->
                            ConstantClass.dialog.dismiss()
                            Log.d("MobileRes", Gson().toJson(response) )
                            historyList = response.data as List<PurchaseHistory>
                            if(historyList!!.size>0){
                                binding.historyListShow.visibility= View.VISIBLE
                                binding.notfoundimage.visibility= View.GONE
                                planKitAdapter = PurchaseHistoryAdapter(historyList)
                                binding.historyListShow.adapter = planKitAdapter
                                planKitAdapter!!.notifyDataSetChanged()

                            }
                            else{
                                binding.historyListShow.visibility= View.GONE
                                binding.notfoundimage.visibility= View.VISIBLE
                            }
                        }
                    }
                }

                ApiStatus.ERROR -> {
                    ConstantClass.dialog.dismiss()
                }

                ApiStatus.LOADING -> {
                    ConstantClass.OpenLoader(this)
                }

            }
        }

        }

    }



}