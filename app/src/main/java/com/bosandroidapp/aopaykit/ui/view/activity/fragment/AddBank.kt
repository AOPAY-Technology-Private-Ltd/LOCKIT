package com.bosandroidapp.aopaykit.ui.view.activity.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window.*
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.FragmentAddAccountBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.data.pennydrop.BankListReq
import com.bosandroidapp.aopaykit.data.pennydrop.PennyDropCheckStatusRequest
import com.bosandroidapp.aopaykit.data.pennydrop.PennyDropRequest
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.repository.PanRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.fragment.BankListPage.Companion.bankDataList
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.ui.viewmodel.PanViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.google.gson.Gson
import java.util.Collections

class AddBank : Fragment() {
     lateinit var binding : FragmentAddAccountBinding
     lateinit var preference : SharedPreference
     lateinit var viewModel: AuthenticationViewModel
     lateinit var dialog : Dialog
     var checkHoldername : Boolean = false
     var checkIFSCcode : Boolean = false
     var checkbranchName : Boolean = false
     var checkbranchAddress : Boolean = false
     var checkEmailID : Boolean = false
    lateinit var panViewModel: PanViewModel
    val bankList = mutableListOf<String>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddAccountBinding.inflate(layoutInflater, container, false)
        preference = SharedPreference(requireContext())

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        panViewModel = ViewModelProvider(this, com.bosandroidapp.aopaykit.data.viewModelFactory.PanViewModelFactory(PanRepository(RetrofitClient.apiInterfacePAN)))[PanViewModel::class.java]

        hitApiForBankList()
        setview()
        setclickListner()
        return binding.root
    }


    fun hitApiForBankList() {
        bankList.clear()
        bankList.add(ConstantClass.SelectBank)

        var req = BankListReq(
            registrationID = ConstantClass.PAN_VERIFICATION_REGISTRATION_ID
        )

        panViewModel.getBankListReq(req).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                ConstantClass.dialog.dismiss()
                                if(response!!.status!!.toLowerCase().equals("false")){
                                    Toast.makeText(requireContext(),response.message, Toast.LENGTH_SHORT).show()
                                }
                                response?.data?.banks?.forEach {
                                    bankList.add(it!!.name!!)
                                    bankList.sort()
                                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, bankList)
                                    binding.bankname.setAdapter(adapter)
                                }

                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(requireContext())
                    }

                }

            }

        }

    }


    fun setview(){

        binding.accountholdername.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.accountholdername.error = "Emoji not allowed"
                        checkHoldername= false
                    }else{
                        checkHoldername= true
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        binding.ifsccode.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.ifsccode.error = "Emoji not allowed"
                        checkIFSCcode= false
                    }
                    else{
                        checkIFSCcode= true
                    }
                }
            }

        })


        binding.branchname.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.branchname.error = "Emoji not allowed"
                        checkbranchName= false
                    }
                    else{
                        checkbranchName= true
                    }
                }
            }

        })


        binding.branchaddress.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.branchaddress.error = "Emoji not allowed"
                        checkbranchAddress= false
                    }
                    else{
                        checkbranchAddress= true
                    }
                }
            }

        })


        binding.emailid.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.emailid.error = "Emoji not allowed"
                        checkEmailID= false
                    }
                    else{
                        checkEmailID= true
                    }
                }
            }

        })


    }


    fun setclickListner(){

        binding.verifybuttonlayout.setOnClickListener {

       val (isValid, errorMessage) = isValidForm(
           accountNumber = binding.accountnumber.text.toString().trim(),
           holdername = binding.accountholdername.text.toString().trim(),
           bankname = binding.bankname.text.toString(),
           ifscCode = binding.ifsccode.text.toString().trim(),
           branchname = binding.branchname.text.toString().trim(),
           branchaddress = binding.branchaddress.text.toString().trim(),
           mobilenumber = binding.mobilenumber.text.toString().trim(),
           emailid = binding.emailid.text.toString().trim(),
       )

       if (isInternetAvailable(requireContext())){
           if (!isValid) {
               Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
           }
           else{
               OpenPopUpForVAlert()
           }

       }

     }

        binding.bankname.setOnClickListener {
            binding.bankname.showDropDown()
        }

    }

    fun isValidForm(
        accountNumber: String,
        holdername:String,
        bankname:String,
        ifscCode: String,
        branchname: String,
        branchaddress: String,
        mobilenumber: String,
        emailid: String,

    ): Pair<Boolean, String?> {
        // Bank details
        if (accountNumber.isBlank()) return Pair(false, "Enter account number")

        if (holdername.isBlank()) return Pair(false, "Enter holder name")

        if (!checkHoldername) return Pair(false, "Enter valid holder name")

        if(bankname.equals(ConstantClass.SelectBank)) return Pair(false,"Please ,select bank")


        if (!ifscCode.matches(Regex("^[A-Z]{4}0[A-Z0-9]{6}$"))) return Pair(false, "Enter valid IFSC code")


        if (!checkIFSCcode) return Pair(false, "Enter valid IFSC code")

        if (branchname.isBlank()) return Pair(false, "Enter branch name")

        if (!checkbranchName) return Pair(false, "Enter valid branch name")

        if (branchaddress.isBlank()) return Pair(false, "Enter branch address")

        if (!checkbranchAddress) return Pair(false, "Enter valid branch address")

        if (mobilenumber.isBlank()) return Pair(false, "Enter mobile number")

        if (emailid.isBlank()) return Pair(false, "Enter email ID")

        if (!emailid.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")))
            return Pair(false, "Enter valid email address")

        if (!checkEmailID ) return Pair(false, "Enter valid email id")

        return Pair(true, null)
    }



    @SuppressLint("SetTextI18n")
    fun OpenPopUpForVAlert() {
        dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.signoutalert)


        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }


        dialog.setCanceledOnTouchOutside(false)

        val cancel = dialog.findViewById<Button>(R.id.btnCancel)
        val done = dialog.findViewById<Button>(R.id.btnLogout)
        val txt = dialog.findViewById<TextView>(R.id.dialog_message)
        val image = dialog.findViewById<ImageView>(R.id.imageview)

        image.visibility = View.VISIBLE

        done.text = "Yes"

        txt.text = "Do you want to add bank account"

        done.setOnClickListener {
            hitApiForRequestPennyDrop()

        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


    fun hitApiForRequestPennyDrop(){
        var request = PennyDropRequest(
            bankName =binding.bankname.text.toString().trim(),
            benificiaryName = binding.accountholdername.text.toString().trim(),
            address = binding.branchaddress.text.toString().trim(),
            paymentMode = ConstantClass.ModeOfPayment,
            iFSCCode = binding.ifsccode.text.toString().trim(),
            registrationID = ConstantClass.PENNYDROP_REGISTRATION_ID,
            refID = "",
            accountNumber = binding.accountnumber.text.toString().trim(),
        )

        Log.d("PennyDropReq",Gson().toJson(request))

        panViewModel.getpennyDropReq(request).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                Log.d("PennyDropRes",Gson().toJson(response))
                                ConstantClass.dialog.dismiss()
                                if(response!!.model?.status.equals(ConstantClass.SUCCESS)){
                                    hitApiForRequestPennyDropCheckStatus(response!!.model!!.clientRefNum!!)
                                }
                                else{
                                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_LONG).show()
                                }
                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenLoader(requireContext())
                    }

                }

            }

        }

    }

    fun hitApiForRequestPennyDropCheckStatus(refID: String){
        var request = PennyDropCheckStatusRequest(
            registrationID = ConstantClass.PENNYDROP_REGISTRATION_ID,
            refID = refID,
        )

        Log.d("PennyDropCheckStatusReq",Gson().toJson(request))

        panViewModel.getpennyDropCheckStatusReq(request).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                Log.d("PennyDropCheckStatusRes",Gson().toJson(response))
                                ConstantClass.dialog.dismiss()
                                if(response!!.model?.status.equals(ConstantClass.SUCCESS)){
                                    hitApiFoAddAccount()
                                }
                                else{
                                    Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                                }
                            }

                        }

                    }

                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {

                    }

                }

            }

        }

    }

    fun hitApiFoAddAccount() {

   var req = com.bosandroidapp.aopaykit.data.model.AddBankAccountReq(
       action = "INSERT",
       retailerID = preference.getStringValue(ConstantClass.RetailerCode, ""),
       accountNumber = binding.accountnumber.text.toString(),
       accountName = binding.accountholdername.text.toString(),
       bankName =binding.bankname.text.toString(),
       ifscCode = binding.ifsccode.text.toString().trim(),
       branchName = binding.branchname.text.toString(),
       branchAddress = binding.branchaddress.text.toString(),
       mobilenumber = binding.mobilenumber.text.toString(),
       emailID = binding.emailid.text.toString()
   )

   Log.d("AddBankAccountReq", Gson().toJson(req))

   viewModel.getAddBankAccountReq(req).observe(requireActivity()) { resources ->
       resources.let {
           when (it.apiStatus) {
               ApiStatus.SUCCESS -> {
                   it.data.let { users ->
                       users!!.body().let { response ->

                           if(dialog!=null && dialog.isShowing){
                               dialog.dismiss()
                           }

                           if (response!!.statuss.equals("True")) {
                               Log.d("AddBankAccountResp", Gson().toJson(response))
                               ConstantClass.dialog.dismiss()

                               if(dialog!=null && dialog.isShowing){
                                   dialog.dismiss()
                               }
                               clearEditPage()
                               hitApiForReports()
                               Toast.makeText(requireContext(),"Account added successfully ",Toast.LENGTH_SHORT).show()

                           }
                           else {
                               ConstantClass.dialog.dismiss()
                               Toast.makeText(requireContext(),response.message,Toast.LENGTH_SHORT).show()
                           }

                       }

                   }

               }

               ApiStatus.ERROR -> {
                   ConstantClass.dialog.dismiss()
               }

               ApiStatus.LOADING -> {
                   ConstantClass.OpenLoader(requireContext())
               }
           }
       }
   }

}

    fun clearEditPage(){
        binding.accountnumber.text.clear()
        binding.accountholdername.text.clear()
        binding.ifsccode.text.clear()
        binding.branchname.text.clear()
        binding.bankname.setHint("Select Bank")
        binding.branchaddress.text.clear()
        binding.mobilenumber.text.clear()
        binding.emailid.text.clear()
    }


    fun containsEmoji(text: String): Boolean {
        for (char in text) {
            val type = Character.getType(char)
            if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                return true
            }
        }
        return false
    }


    fun hitApiForReports(){

        var req = com.bosandroidapp.aopaykit.data.model.AddBankAccountReq(
            action = "GET",
            retailerID = preference.getStringValue(ConstantClass.RetailerCode, ""),
            accountNumber = "",
            accountName = "",
            bankName = "",
            ifscCode = "",
            branchName = "",
            branchAddress = "",
            mobilenumber = "",
            emailID = ""
        )

        Log.d("GetBankListReq", Gson().toJson(req))

        viewModel.getAddBankAccountReq(req).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let {
                                    response ->
                                if(response!!.statuss.equals("True")){
                                    Log.d("BankListRes",Gson().toJson(response))
                                    bankDataList = response?.data!!
                                    Collections.reverse(bankDataList)
                                }
                                else{

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


}