package com.bosandroidapp.aopaykit.ui.view.activity.retailer.reports

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.TransactionHistoryDataItem
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.TransactionHistoryReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityAllTransactionHistoryBinding
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.adapter.TransactionReportAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class AllTransactionHistory : BaseActivity() {
    lateinit var binding: ActivityAllTransactionHistoryBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference: SharedPreference
    var transactionHistory: MutableList<TransactionHistoryDataItem?>? = mutableListOf()
    lateinit var transactionAdapter : TransactionReportAdapter
    private val myCalender = Calendar.getInstance()
    private val myCalender1 = Calendar.getInstance()
    var FromDate: String= ""
    var ToDate: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAllTransactionHistoryBinding.inflate(layoutInflater)
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


    fun setOnClickListner(){

        binding.back.setOnClickListener {
            finish()
        }


        binding.fromDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    val actualMonth = monthOfYear + 1 // Fix zero-based month
                    myCalender.set(year, monthOfYear, dayOfMonth)
                    binding.fromDate.text = "$dayOfMonth/$actualMonth/$year"

                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
                    calendar.set(Calendar.MILLISECOND, 0)

                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


                    FromDate = sdf.format(calendar.time)
                    Log.d("FromDate", FromDate!!)

                    // If both dates are selected, validate and call API
                    if(ToDate==null|| FromDate==null){
                        return@DatePickerDialog
                    }
                    if ( !ToDate!!.isNullOrBlank() && !FromDate!!.isNullOrBlank()) {
                        val from = sdf.parse(FromDate)
                        val to = sdf.parse(ToDate)

                        when {
                            to.before(from) -> {
                                Toast.makeText(this, "To date cannot be before From date", Toast.LENGTH_SHORT).show()
                            }
                            /*to.equals(from) -> {
                                Toast.makeText(this, "From and To dates cannot be the same", Toast.LENGTH_SHORT).show()
                            }*/
                            else -> {
                                hitApiForGetTransactionReport()
                            }
                        }
                    }
                },
                myCalender.get(Calendar.YEAR),
                myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        binding.toDate.setOnClickListener { DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->

                    val actualMonth = monthOfYear + 1 // Fix zero-based month
                    myCalender1.set(year, monthOfYear, dayOfMonth)
                    binding.toDate.text = "$dayOfMonth/$actualMonth/$year"

                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
                    calendar.set(Calendar.MILLISECOND, 0)

                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                    ToDate = sdf.format(calendar.time)
                    Log.d("ToDate", ToDate!!)

                    if(ToDate==null|| FromDate==null){
                        return@DatePickerDialog
                    }

                    // If both dates are selected, validate and call API
                    if ( !ToDate!!.isNullOrBlank() && !FromDate!!.isNullOrBlank()) {
                        val from = sdf.parse(FromDate)
                        val to = sdf.parse(ToDate)

                        when {
                            to.before(from) -> {
                                Toast.makeText(this, "To date cannot be before From date", Toast.LENGTH_SHORT).show()
                            }
                           /* to.equals(from) -> {
                                Toast.makeText(this, "From and To dates cannot be the same", Toast.LENGTH_SHORT).show()
                            }*/
                            else -> {
                                hitApiForGetTransactionReport()
                            }
                        }
                    }
                },
                myCalender1.get(Calendar.YEAR),
                myCalender1.get(Calendar.MONTH),
                myCalender1.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


    }



    fun hitApiForGetTransactionReport() {
        var retailerCode = preference.getStringValue(ConstantClass.RetailerCode,"")

        var reportreq = TransactionHistoryReq(
            fromDate = FromDate,
            toDate = ToDate,
            registrationId = retailerCode,
            status = null, //Approved
        )
        Log.d("ReportReq", Gson().toJson(reportreq))

        viewModel.getTransactionHistoryList(reportreq).observe(this){
                resources->resources.let {
            when(it.apiStatus){
                ApiStatus.SUCCESS -> {
                    it.data?.let { users ->
                        users.body()?.let { response ->
                            ConstantClass.dialog.dismiss()
                            Log.d("MobileRes", Gson().toJson(response) )
                            transactionHistory = response.data!!.toMutableList()
                            if(transactionHistory!!.size>0){
                                binding.showtransaction.visibility= View.VISIBLE
                                binding.notfoundimage.visibility= View.GONE
                                transactionAdapter = TransactionReportAdapter(this,transactionHistory)
                                binding.showtransaction.adapter = transactionAdapter
                                transactionAdapter.notifyDataSetChanged()

                            }
                            else{
                                binding.showtransaction.visibility= View.GONE
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