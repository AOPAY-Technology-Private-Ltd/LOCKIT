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
import com.bosandroidapp.aopaykit.data.model.GetRetailerLedgerReq
import com.bosandroidapp.aopaykit.data.model.LedgerReportDataItem
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityLedgerreportBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.adapter.LedgerAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class Ledgerreport : AppCompatActivity() {
    lateinit var binding : ActivityLedgerreportBinding
    var FromDate: String= ""
    var ToDate: String =""
    private val myCalender = Calendar.getInstance()
    private val myCalender1 = Calendar.getInstance()
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference: SharedPreference
    var LedgerReportList : List<LedgerReportDataItem?> = listOf()
    lateinit var ledgerAdapter : LedgerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLedgerreportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface))
        )[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)

        clickListner()

    }

    fun clickListner() {

        binding.back.setOnClickListener {
            finish()
        }


        binding.fromDate.setOnClickListener {

            DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->

                // Set selected date (month is 0-based, do NOT add +1 here)
                myCalender.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
                myCalender.set(Calendar.MILLISECOND, 0)

                // Show date in UI (dd/MM/yyyy)
                binding.fromDate.text = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year)

                // Convert to UTC ISO format
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                sdf.timeZone = TimeZone.getTimeZone("UTC")

                FromDate = sdf.format(myCalender.time)

                Log.d("FromDate", FromDate)

                // Call API only when both dates are selected
                if (FromDate.isNotBlank() && ToDate.isNotBlank()) {
                    hitApiForGeetingReports()
                }
            },
                myCalender.get(Calendar.YEAR),
                myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH)).show()

        }


        binding.toDate.setOnClickListener {

            DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->

                    // Set calendar (monthOfYear is 0-based)
                    myCalender1.set(
                        year,
                        monthOfYear,
                        dayOfMonth,
                        23,
                        59,
                        59
                    )
                    myCalender1.set(Calendar.MILLISECOND, 999)

                    // Show date in UI (dd/MM/yyyy)
                    val displayMonth = monthOfYear + 1
                    binding.toDate.text = String.format("%02d/%02d/%04d", dayOfMonth, displayMonth, year)

                    // Convert to ISO UTC format
                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    sdf.timeZone = TimeZone.getTimeZone("UTC")

                    ToDate = sdf.format(myCalender1.time)

                    Log.d("ToDate", ToDate)

                    // Call API only when both dates are selected
                    if (FromDate.isNotBlank() && ToDate.isNotBlank()) {
                        hitApiForGeetingReports()
                    }
                },
                myCalender1.get(Calendar.YEAR),
                myCalender1.get(Calendar.MONTH),
                myCalender1.get(Calendar.DAY_OF_MONTH)
            ).show()


        }

    }


    fun hitApiForGeetingReports() {
        var retailercode = preference.getStringValue(ConstantClass.RetailerCode, "")
        var gettingreportsreq = GetRetailerLedgerReq(
            dealerCode = retailercode ,
            fromDate = FromDate,
            toDate = ToDate,
        )

        Log.d("ledgerreq", Gson().toJson(gettingreportsreq))

        viewModel.getRetailerLedgerReq(gettingreportsreq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("ledgerresponse", Gson().toJson(response))

                                var LedgerReportList = response.data

                                if(response.status.equals("True" ,ignoreCase = true)){
                                    ConstantClass.dialog.dismiss()
                                    setDataOnUI(LedgerReportList!!)
                                }
                                else{
                                    ConstantClass.dialog.dismiss()
                                    binding.ledgerreports.visibility = View.GONE
                                    binding.notfoundimage.visibility = View.VISIBLE
                                    Toast.makeText(this,response.message, Toast.LENGTH_SHORT).show()
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


    fun setDataOnUI(LedgerReportList:List<LedgerReportDataItem?>){
        Log.d("LedgerReport",Gson().toJson(LedgerReportList))

        if(LedgerReportList.size>0){
            binding.ledgerreports.visibility = View.VISIBLE
            binding.notfoundimage.visibility = View.GONE
            ledgerAdapter = LedgerAdapter(this,LedgerReportList)
            binding.ledgerreports.adapter=ledgerAdapter
            ledgerAdapter.notifyDataSetChanged()
        }
        else{
            binding.ledgerreports.visibility = View.GONE
            binding.notfoundimage.visibility = View.VISIBLE
        }

    }

}