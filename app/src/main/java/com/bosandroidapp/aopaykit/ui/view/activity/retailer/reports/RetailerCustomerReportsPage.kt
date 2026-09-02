package com.bosandroidapp.aopaykit.ui.view.activity.retailer.reports

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.bosandroidapp.aopaykit.databinding.ActivityRetailerCustomerReportsPageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.GetReportsReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.slideshow.adapter.RetailerReportListAdapter
import com.bosandroidapp.aopaykit.ui.view.activity.ChooseYourRolePage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class RetailerCustomerReportsPage :BaseActivity() {
    lateinit var binding : ActivityRetailerCustomerReportsPageBinding
    lateinit var preference : SharedPreference
    lateinit var viewModel: AuthenticationViewModel
    var ReportDataList : MutableList<com.bosandroidapp.aopaykit.data.model.loginsignup.reports.ReportsDataItem> = mutableListOf()
    var FilterReportDataList : MutableList<com.bosandroidapp.aopaykit.data.model.loginsignup.reports.ReportsDataItem> = mutableListOf()
    lateinit var reportAdapter : RetailerReportListAdapter
    private val myCalender = Calendar.getInstance()
    private val myCalender1 = Calendar.getInstance()
    var FromDate: String ? = null
    var ToDate: String ? = null
    var ReportType: String ="All"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRetailerCustomerReportsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
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
        hitApiForLogin()

    }


    fun setview(){
        val adapter = ArrayAdapter.createFromResource(this,  R.array.reporttype, R.layout.mobilenamelayout)
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
                ReportType = selectedItem

                hitApiForGetReports(selectedItem)


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // nothing
            }
        }


    }


    fun setclicklistner(){

        binding.back.setOnClickListener {
            finish()
        }


        binding.searcMobile.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val search = s.toString().lowercase().trim()
                    val result = ReportDataList.filter {
                        it.loanCode.lowercase().contains(search) || it.customerName.lowercase().contains(search)|| it.custerMob.lowercase().contains(search)||
                                it.customerCode.lowercase().contains(search)
                    }
                    FilterReportDataList.clear()
                    FilterReportDataList.addAll(result)
                    setDataInView(FilterReportDataList)
                    reportAdapter.notifyDataSetChanged()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })


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
                    if (!ReportType.isNullOrBlank() ) {
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
                                hitApiForGetReports(ReportType)
                            }
                        }
                    }
                },
                myCalender.get(Calendar.YEAR),
                myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        binding.toDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
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
                    if (!ReportType.isNullOrBlank() ) {
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
                                hitApiForGetReports(ReportType)
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


    fun hitApiForGetReports( reporttype:String) {
        var retailerCode = preference.getStringValue(ConstantClass.RetailerCode,"")
        var recordStatus = reporttype

        var reportreq = GetReportsReq(
            retailercode = retailerCode,
            recordStatus = recordStatus,
            customercode = "",
            fromDate = FromDate,
            toDate = ToDate
        )
        Log.d("RetailerCustomerLoanReq", Gson().toJson(reportreq))

        viewModel.getReportsReq(reportreq).observe(this){
        resources->resources.let {
            when(it.apiStatus){
                ApiStatus.SUCCESS -> {
                    it.data?.let { users ->
                        users.body()?.let { response ->
                            ConstantClass.dialog.dismiss()
                            Log.d("MobileRes",Gson().toJson(response) )
                            ReportDataList = response.data!!.toMutableList()
                            //ReportDataList =  allReportList.filter { !it.recordStatus.equals("Disbursed", ignoreCase = true) }.toMutableList()
                            binding.reportcount.text = "Total records : ${ReportDataList.size}"

                            if(ReportDataList.size>0){
                                binding.showreports.visibility= View.VISIBLE
                                binding.notfoundimage.visibility=View.GONE
                                reportAdapter = RetailerReportListAdapter(ReportDataList,this,ConstantClass.Retailer)
                                binding.showreports.adapter = reportAdapter
                                reportAdapter.notifyDataSetChanged()
                            }
                            else{
                                binding.showreports.visibility= View.GONE
                                binding.notfoundimage.visibility=View.VISIBLE
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



    fun setDataInView (ReportDataList:MutableList<com.bosandroidapp.aopaykit.data.model.loginsignup.reports.ReportsDataItem>){
        if(ReportDataList.size>0){
            binding.showreports.visibility= View.VISIBLE
            binding.notfoundimage.visibility=View.GONE
            reportAdapter = RetailerReportListAdapter(ReportDataList,this,ConstantClass.Retailer)
            binding.showreports.adapter = reportAdapter
            reportAdapter.notifyDataSetChanged()

        }else{
            binding.showreports.visibility= View.GONE
            binding.notfoundimage.visibility=View.VISIBLE
        }
    }



    fun hitApiForLogin() {

        var sessionOutReq = SessionOutReq(
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
            clientCode = preference.getStringValue(ConstantClass.ClientCode, "")
        )

        Log.d("SessionOutReq", Gson().toJson(sessionOutReq))

        viewModel.getSessionReq(sessionOutReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("SessionOutResponse", Gson().toJson(response))
                                if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                                    ConstantClass.dialog.dismiss()
                                }
                                ConstantClass.checkActiveStatusAndLogout(this@RetailerCustomerReportsPage, response.status, preference)
                            }
                        }
                    }

                    ApiStatus.ERROR -> {

                    }

                    ApiStatus.LOADING -> {

                    }
                }
            }
        }

        var request = ValidateSessionRequest(
            preference.getStringValue(ConstantClass.RetailerCode, ""),
            preference.getStringValue(ConstantClass.DEVICEID, ""),
            preference.getStringValue(ConstantClass.FCMTOKEN, "")
        )
        Log.d("validaterequest", Gson().toJson(request))

        viewModel.getSessionExpiredReq(request).observe(this){resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("validateresp", Gson().toJson(response))
                                if(response.status==0){
                                    hitApiForRetailerLogout()
                                }
                            }
                        }
                    }

                    ApiStatus.ERROR -> {

                    }

                    ApiStatus.LOADING -> {

                    }
                }
            }
        }


    }


    fun hitApiForRetailerLogout() {
        var loginRequest = LogoutReq(
            retailerCode = preference.getStringValue(ConstantClass.RetailerCode, ""),
        )

        Log.d("LogoutReq", Gson().toJson(loginRequest))

        viewModel.getLogout(loginRequest).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("LogoutResponse", Gson().toJson(response))
                                preference.setBooleanValue(ConstantClass.LoggedIn, false)
                                preference.setStringValue(ConstantClass.LoginType, "")
                                ConstantClass.ClickOnCardDashboard = ""
                                val intent = Intent(this@RetailerCustomerReportsPage, ChooseYourRolePage::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                        }
                    }

                    ApiStatus.ERROR -> {

                    }

                    ApiStatus.LOADING -> {

                    }
                }
            }
        }

    }


}