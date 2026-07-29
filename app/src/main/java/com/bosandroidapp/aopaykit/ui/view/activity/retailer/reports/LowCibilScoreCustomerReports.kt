package com.bosandroidapp.aopaykit.ui.view.activity.retailer.reports

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.CibilDataItem
import com.bosandroidapp.aopaykit.data.model.LowCibilCustomerReportReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityLowCibilScoreCustomerReportsBinding
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.slideshow.adapter.LowCibilScoreCustomerReportListAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class LowCibilScoreCustomerReports : BaseActivity(){
    lateinit var binding: ActivityLowCibilScoreCustomerReportsBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference: SharedPreference
    var FromDate: String= ""
    var ToDate: String =""
    private val myCalender = Calendar.getInstance()
    private val myCalender1 = Calendar.getInstance()
    lateinit var lowcibilreportadapter: LowCibilScoreCustomerReportListAdapter
    var LowCibilReportList : List<CibilDataItem?> = listOf()
    var FilterLowCibilReportList : List<CibilDataItem?> = listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLowCibilScoreCustomerReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        viewModel = ViewModelProvider(
            this,
            CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface))
        )[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)


        clickListner()

    }


    fun setview(){
        val adapter = ArrayAdapter.createFromResource(this,  R.array.cibilreporttype, R.layout.mobilenamelayout)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.reporttype.adapter = adapter

        var isSpinnerFirstCall = true // declare outside the listener

        binding.reporttype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if(selectedItem.equals("All")){
                    if(LowCibilReportList.size>0){
                        setDataOnUI(LowCibilReportList)
                    }
                }
                else{
                    FilterLowCibilReportList = LowCibilReportList.filter { it!!.activeStatus.equals(selectedItem,ignoreCase = true) }
                    setDataOnUI(FilterLowCibilReportList)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // nothing
            }
        }


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
        var gettingreportsreq = LowCibilCustomerReportReq(
            reportType = "CustByCredit",
            fromDate = FromDate,
            toDate = ToDate,
            retailerCode = retailercode
        )

        Log.d("cibilreportsreq", Gson().toJson(gettingreportsreq))

        viewModel.getLowCibilReports(gettingreportsreq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("cibilreportsresponse", Gson().toJson(response))
                                if(response.status.equals("True" ,ignoreCase = true)){
                                    ConstantClass.dialog.dismiss()
                                    if(response.data!!.size>0){
                                        LowCibilReportList = response.data!!
                                        binding.showreports.visibility = View.VISIBLE
                                        binding.spinnerlayout.visibility = View.VISIBLE
                                        binding.notfoundimage.visibility = View.GONE
                                        setDataOnUI(LowCibilReportList)
                                        setview()
                                    }else{
                                        ConstantClass.dialog.dismiss()
                                        binding.showreports.visibility = View.GONE
                                        binding.spinnerlayout.visibility = View.GONE
                                        binding.notfoundimage.visibility = View.VISIBLE
                                        Toast.makeText(this,response.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                else{
                                    ConstantClass.dialog.dismiss()
                                    binding.showreports.visibility = View.GONE
                                    binding.spinnerlayout.visibility = View.GONE
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


    fun setDataOnUI(LowCibilReportList:List<CibilDataItem?>){
        Log.d("LowCibilScore",Gson().toJson(LowCibilReportList))
        if(LowCibilReportList.size>0){
            binding.showreports.visibility = View.VISIBLE
            binding.notfoundimage.visibility = View.GONE
        }else{
            binding.showreports.visibility = View.GONE
            binding.notfoundimage.visibility = View.VISIBLE
        }
        lowcibilreportadapter = LowCibilScoreCustomerReportListAdapter(LowCibilReportList,this)
        binding.showreports.adapter=lowcibilreportadapter
        lowcibilreportadapter.notifyDataSetChanged()
    }


}