package com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.model.MakePaymentAdminReportRequest
import com.bosandroidapp.aopaykit.data.model.MakePaymentReportDataItem
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.FragmentReportPageBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.adapter.MakePaymentAdminReportAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class ReportPage : Fragment() {
    lateinit var binding: FragmentReportPageBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference: SharedPreference
    private val myCalender = Calendar.getInstance()
    private val myCalender1 = Calendar.getInstance()
    var FromDate: String= ""
    var ToDate: String =""
    lateinit var adapter : MakePaymentAdminReportAdapter

    var MakePaymentReportList : List<MakePaymentReportDataItem?> = listOf()
    var FilterMakePaymentReportList : List<MakePaymentReportDataItem?> = listOf()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentReportPageBinding.inflate(layoutInflater, container, false)

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(requireContext())
        clickListner()
        return  binding.root
    }


    fun clickListner() {


        binding.fromDate.setOnClickListener {

            DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->

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
                    hitApiForGettingMakePaymentRequestReport()
                }
            },
                myCalender.get(Calendar.YEAR),
                myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH)).show()

        }


        binding.toDate.setOnClickListener {

            DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->

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
                        hitApiForGettingMakePaymentRequestReport()
                    }
                },
                myCalender1.get(Calendar.YEAR),
                myCalender1.get(Calendar.MONTH),
                myCalender1.get(Calendar.DAY_OF_MONTH)
            ).show()


        }

    }

    fun hitApiForGettingMakePaymentRequestReport() {
        var retailercode = preference.getStringValue(ConstantClass.RetailerCode, "")
        var gettingreportsreq = MakePaymentAdminReportRequest(
            fromDate = FromDate,
            toDate = ToDate,
            activeStatus = "",
            retailerCode = retailercode
        )

        Log.d("makepaymentreportreq", Gson().toJson(gettingreportsreq))

        viewModel.MakePaymentAdminReportRequest(gettingreportsreq).observe(activity) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("cibilreportsresponse", Gson().toJson(response))
                                if(response.status!!.toLowerCase().equals("true" ,ignoreCase = true)){
                                    ConstantClass.dialog.dismiss()
                                    if(response.data!!.size>0){
                                        MakePaymentReportList = response.data!!
                                        binding.reportlist.visibility = View.VISIBLE
                                        binding.spinnerlayout.visibility = View.VISIBLE
                                        binding.notfoundlayout.visibility = View.GONE
                                        setDataOnUI(MakePaymentReportList)
                                        setview()
                                    }else{
                                        ConstantClass.dialog.dismiss()
                                        binding.reportlist.visibility = View.GONE
                                        binding.spinnerlayout.visibility = View.GONE
                                        binding.notfoundlayout.visibility = View.VISIBLE
                                        Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                                else{
                                    ConstantClass.dialog.dismiss()
                                    binding.reportlist.visibility = View.GONE
                                    binding.spinnerlayout.visibility = View.GONE
                                    binding.notfoundlayout.visibility = View.VISIBLE
                                    Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(requireContext())
                    }
                }
            }
        }

    }


    fun setview(){
        val adapter = ArrayAdapter.createFromResource(requireContext(),  R.array.cibilreporttype, R.layout.mobilenamelayout)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.reporttype.adapter = adapter


        binding.reporttype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                if(selectedItem.equals("All")){
                    if(MakePaymentReportList.size>0){
                        setDataOnUI(MakePaymentReportList)
                    }
                }
                else{
                    FilterMakePaymentReportList = MakePaymentReportList.filter { it!!.recordStatus.equals(selectedItem,ignoreCase = true) }
                    setDataOnUI(FilterMakePaymentReportList)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // nothing
            }
        }


    }

    fun setDataOnUI(makePaymentList:List<MakePaymentReportDataItem?>){
        Log.d("makePaymentList",Gson().toJson(makePaymentList))
        if(makePaymentList.size>0){
            binding.reportlist.visibility = View.VISIBLE
            binding.notfoundlayout.visibility = View.GONE
        }else{
            binding.reportlist.visibility = View.GONE
            binding.notfoundlayout.visibility = View.VISIBLE
        }
        adapter = MakePaymentAdminReportAdapter( requireActivity(),makePaymentList)
        binding.reportlist.adapter=adapter
        adapter.notifyDataSetChanged()
    }


}