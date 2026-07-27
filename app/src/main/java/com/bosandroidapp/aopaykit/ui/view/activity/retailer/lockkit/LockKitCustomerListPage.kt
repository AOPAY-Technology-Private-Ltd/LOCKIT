package com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadharNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.AadharVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.AccountNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.AccountType
import com.bosandroidapp.aopaykit.constant.ConstantClass.BankIFSCCode
import com.bosandroidapp.aopaykit.constant.ConstantClass.BankName
import com.bosandroidapp.aopaykit.constant.ConstantClass.BranchName
import com.bosandroidapp.aopaykit.constant.ConstantClass.BrandName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustAlternateMobileNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustAlternateMobileOTP
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustAlternateMobileVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustAreaSector
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustCityName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustCountry
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustCurrentAddress
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustFirstName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustFlatNo
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustLastName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustMiddleName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustPinCode
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustPrimaryMobileNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustPrimaryMobileVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustPrimaryOTP
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustStateName
import com.bosandroidapp.aopaykit.constant.ConstantClass.CusteMailID
import com.bosandroidapp.aopaykit.constant.ConstantClass.EmiAmount
import com.bosandroidapp.aopaykit.constant.ConstantClass.ImeiNumber1
import com.bosandroidapp.aopaykit.constant.ConstantClass.ImeiNumber2
import com.bosandroidapp.aopaykit.constant.ConstantClass.IsRetailerAggrementVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.ModelColor
import com.bosandroidapp.aopaykit.constant.ConstantClass.ModelName
import com.bosandroidapp.aopaykit.constant.ConstantClass.ModelVarient
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanNumber
import com.bosandroidapp.aopaykit.constant.ConstantClass.PanNumberVerified
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefAddress
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefName
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefRelationShip
import com.bosandroidapp.aopaykit.constant.ConstantClass.RefmobileNo
import com.bosandroidapp.aopaykit.constant.ConstantClass.Tenure
import com.bosandroidapp.aopaykit.constant.ConstantClass.isAggrementVerified
import com.bosandroidapp.aopaykit.data.model.CustomerKitRequest
import com.bosandroidapp.aopaykit.data.model.kitoption.CustomerListItem
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityLockKitCustomerListPageBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.slideshow.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.view.adapter.LockKitCustomerListAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.BureauScore.Companion.userScore
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LockKitCustomerListPage : AppCompatActivity() {

    lateinit var binding : ActivityLockKitCustomerListPageBinding
    lateinit var adapter : LockKitCustomerListAdapter
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference: SharedPreference
    var kitCustomerList : List<CustomerListItem?> ?= listOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLockKitCustomerListPageBinding.inflate(layoutInflater)
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

        setOnClickListner()

        // Observe global refresh events
        lifecycleScope.launch {
            AuthRepository.customerListUpdate.collectLatest {
                getKitCustomerList()
            }
        }

    }


    override fun onResume() {
        super.onResume()
        getKitCustomerList()
    }


    fun setOnClickListner(){
        binding.back.setOnClickListener {
            finish()
        }


        binding.home.setOnClickListener {
            val intent = Intent(this, DashBoard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            onBackPressed()
        }



    }


   fun setDataInList(kitCustomerList:List<CustomerListItem?> ?){
        adapter = LockKitCustomerListAdapter(this,kitCustomerList)
        binding.lockkitcustomerlist.adapter = adapter
    }


    fun getKitCustomerList(){
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val safeLastName = if (!lastName.isNullOrBlank() && lastName != "null") lastName else ""
        var createdBy = firstName.plus(" ").plus(safeLastName)
        var retailercode = preference.getStringValue(ConstantClass.RetailerCode, "")

        var registationRequest = CustomerKitRequest(
            mode = "GET",
            firstName =  "",
            middleName =  "",
            lastName =  "",
            primaryMobileNumber =  "",
            primaryOTP =  "",
            primaryMobileVerified =  "",
            alternateMobileNumber = "",
            alternateMobileOTP =  "",
            pAlternateMobileVerified =  "",
            eMailID =  "",
            flatNo =  "",
            aearSector =  "",
            pinCode = "",
            currentAddress =  "",
            stateName =  "",
            cityName =  "",
            country =  "India",
            aadharNumber =  "",
            aadharNumberVerified =  "",
            panNumber = "",
            panNumberVerified = "",
            brandName = "",
            modelName = "",
            modelVariant = "",
            color = "",
            sellingPrice =  "",
            downPayment = "",
            tenure =  "",
            emiAmount = "",
            imeiNumber1 = "",
            imeiNumber2 =  "",
            accountNumber = "",
            bankIFSCCode = "",
            bankName =  "",
            accountType =  "",
            branchName =  "",
            refName =  "",
            refRelationShip = "",
            refmobileNo = "",
            refAddress = "",
            debitOrCreditCard = "",
            upiMandate = "yes",
            createdBy = createdBy,
            membershipfees = "",
            retailercode = retailercode,
            cibilScore = "",
            isAggrementVerified = "",
            IsRetailerAggrementVerified = "",
            custPhoto_File =  null,
            imeiNumber1_SealPhotoPath = null,
            imeiNumber2_SealPhotoPath =  null,
            imeiNumber_PhotoPath =  null,
            invoive_Path =  null,
            aadharFront_Path =  null,
            aadharBack_Path =  null,
            panFront_Path =  null
        )
        Log.d("RegistationRequest", Gson().toJson(registationRequest))
        viewModel.getCustomerKitRequest(registationRequest).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                ConstantClass.dialog.dismiss()
                                Toast.makeText(this, response!!.message, Toast.LENGTH_SHORT).show()
                                if (response!!.statuss!!.toLowerCase().equals("success", ignoreCase = true)) {

                                   if(response.customerList!!.size>0){
                                       kitCustomerList = response.customerList
                                       setDataInList(kitCustomerList)
                                       binding.lockkitcustomerlist.visibility=View.VISIBLE
                                       binding.notfoundimage.visibility=View.GONE
                                   }
                                    else {
                                       binding.lockkitcustomerlist.visibility=View.VISIBLE
                                       binding.notfoundimage.visibility=View.GONE
                                   }

                                }
                                else {
                                    binding.lockkitcustomerlist.visibility=View.GONE
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

}