package com.bosandroidapp.aopaykit.ui.view.activity.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.FragmentPayoutPageBinding
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.AdminCibilScore
import com.bosandroidapp.aopaykit.constant.ConstantClass.AdminLoanApprovedStatus
import com.bosandroidapp.aopaykit.constant.ConstantClass.HoldAmount
import com.bosandroidapp.aopaykit.constant.ConstantClass.MaxHoldingAmount
import com.bosandroidapp.aopaykit.constant.ConstantClass.MinHoldingAmount
import com.bosandroidapp.aopaykit.constant.ConstantClass.WalletBalance
import com.bosandroidapp.aopaykit.constant.ConstantClass.formatToMMDDYYYY
import com.bosandroidapp.aopaykit.constant.ConstantClass.getDeviceIpAddress
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.data.model.HoldAmountWithdrawReq
import com.bosandroidapp.aopaykit.data.model.RetailerWalletAmountReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.RetailerWalletPayoutReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.activity.fragment.BankListPage.Companion.bankDataList
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.BankDetailsPage
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.bosandroidapp.aopaykit.utils.formatDate
import com.bosandroidapp.aopaykit.utils.getNetworkTime
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PayoutPage : Fragment() {

     lateinit var binding : FragmentPayoutPageBinding
     lateinit var preference : SharedPreference
     lateinit var viewModel: AuthenticationViewModel
     var currentDate : String = ""
     lateinit var dialog : Dialog
     var checkHoldername : Boolean = false
     var checkIFSCcode : Boolean = false
     var checkbranchName : Boolean = false
     var checkremarks : Boolean = false
     var holdcheckremarks : Boolean = false
     private val bankAccountNumber: MutableList<String?> = mutableListOf()

     companion object{
         var CheckActiveStatus : Boolean = false
     }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPayoutPageBinding.inflate(layoutInflater, container, false)
        preference = SharedPreference(requireContext())
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

        lifecycleScope.launch {
            val date = withContext(Dispatchers.IO) {
                getNetworkTime(context)
            }
            currentDate =formatToMMDDYYYY(date.first?.formatDate().orEmpty())
            Log.d("currentdate", Gson().toJson(currentDate))
        }

        setview()
        setDataInSpinner()
        setclickListner()
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        hitApiForGetBankList()
    }


    fun setview(){
        binding.walletamount.text = "₹ ".plus(WalletBalance)
        binding.holdamount.text = "₹ ".plus(HoldAmount)

        if(CheckActiveStatus){
            binding.walletpayoutlayout.visibility= View.GONE
            binding.holdammountlayout.visibility= View.VISIBLE
        }
        else
        {
            binding.walletpayoutlayout.visibility= View.VISIBLE
            binding.holdammountlayout.visibility= View.GONE
        }

        binding.amountEdittxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val enteredPriceStr = s.toString().trim()
                val enteredPrice = enteredPriceStr.toDoubleOrNull() ?: return
                val walletamount = WalletBalance.toDoubleOrNull() ?: 0.0

                // ✅ First check if wallet is negative
                if (walletamount < 0) {
                    binding.verifybuttonlayout.isEnabled = false
                    binding.amountEdittxt.error = "You cannot withdraw. Wallet balance is negative."
                    return
                }

                when {
                    enteredPrice == 0.0 -> {
                        binding.verifybuttonlayout.isEnabled = false
                        binding.amountEdittxt.error = "Enter amount greater than 0"
                    }

                    enteredPrice > walletamount -> {
                        binding.verifybuttonlayout.isEnabled = false
                        binding.amountEdittxt.error = "You can request up to ₹$walletamount"
                    }

                    else -> {
                        binding.verifybuttonlayout.isEnabled = true
                        binding.amountEdittxt.error = null
                    }
                }
            }

        })


        binding.holdamountetx.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val enteredText = s.toString().trim()
               // var maxHoldAmount = MaxHoldingAmount.toDoubleOrNull() ?: 0.0
                var minHoldAmount = MinHoldingAmount.toDoubleOrNull() ?: 0.0
                var HoldAmount = HoldAmount.toDoubleOrNull() ?: 0.0
                if(HoldAmount > minHoldAmount){

                    if (enteredText.isNotEmpty()) {

                        //val remainingBalance =  HoldAmount - maxHoldAmount
                        val remainingBalance =  HoldAmount - minHoldAmount

                        val enteredValue = enteredText.toDoubleOrNull()

                        if (enteredValue!! > remainingBalance) {
                            binding.holdamountetx.error = "Max allowed is $remainingBalance"
                            binding.holdamountrequest.isEnabled= false
                        }
                        else{
                            binding.holdamountrequest.isEnabled= true
                        }
                    }

                  }
                else {
                    binding.holdamountrequest.isEnabled= false
                   // binding.holdamountetx.error = "You cannot withdraw hold amount because hold amount is less than max hold amount $maxHoldAmount"
                    binding.holdamountetx.error = "You cannot withdraw hold amount because hold amount is less than min hold amount $minHoldAmount"
                }

            }

        })


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


        binding.remarks.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.remarks.error = "Emoji not allowed"
                        checkremarks= false
                    }
                    else{
                        checkremarks= true
                    }
                }
            }

        })


        binding.holdamountremarks.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (containsEmoji(it.toString())) {
                        binding.holdamountremarks.error = "Emoji not allowed"
                        holdcheckremarks= true
                    }
                    else{
                        holdcheckremarks= false
                    }
                }
            }

        })

    }


    fun setDataInSpinner(){
        val adapter = ArrayAdapter.createFromResource(requireContext(),  R.array.walletpaymentmode, R.layout.mobilenamelayout)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.paymentmode.adapter = adapter


        binding.accountnumber.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var accountnumber = binding.accountnumber.selectedItem.toString().trim()
                val selectedBank = bankDataList!!.find { it!!.accountNumber == accountnumber }
                selectedBank.let { it->
                    binding.accountholdername.text =(it!!.accountName)
                    binding.ifsccode.text=(it.ifscCode)
                    binding.branchname.text=(it.branchName)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        
    }





 fun setclickListner(){

     binding.addBankdetails.setOnClickListener {
         startActivity(Intent(requireContext(), BankDetailsPage::class.java))
     }


   binding.verifybuttonlayout.setOnClickListener {

       val (isValid, errorMessage) = isValidForm(
           accountNumber = binding.accountnumber.selectedItem?.toString()?.trim() ?: "",
           holdername = binding.accountholdername.text?.toString()?.trim()?:"",
           ifscCode = binding.ifsccode.text?.toString()?.trim()?:"",
           branchname = binding.branchname.text?.toString()?.trim()?:"",
           mode = binding.paymentmode.selectedItem?.toString()?.trim()?:"",
           amount = binding.amountEdittxt.text?.toString()?.trim()?:"",
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


   binding.holdamountrequest.setOnClickListener {
       if(holdcheckremarks){
           Toast.makeText(requireContext(),"Please enter valid remark",Toast.LENGTH_SHORT).show()
       }
       else{
           if(!binding.holdamountetx.text.toString().trim().isNullOrBlank()){
               OpenPopUpForHoldAmountAlert()
           }else{
               Toast.makeText(requireContext(),"Please enter amount!!",Toast.LENGTH_SHORT).show()
           }

       }


   }



}


    fun hitApiForHoldAmountRequest(){

        var req = HoldAmountWithdrawReq(
            retailerID = preference.getStringValue(ConstantClass.RetailerCode,""),
            amount = binding.holdamountetx.text.toString().trim(),
            remarks = binding.holdamountremarks.text.toString()
        )

        Log.d("HoldAmountReq", Gson().toJson(req))

        viewModel.getHoldAmountWithdrawRequest(req).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->

                                if(dialog!=null && dialog.isShowing){
                                    dialog.dismiss()
                                }

                                if (response!!.statuss.equals("True")) {
                                    Log.d("HoldAmountResp", Gson().toJson(response))
                                    ConstantClass.dialog.dismiss()

                                    if(dialog!=null && dialog.isShowing){
                                        dialog.dismiss()
                                    }
                                    clearHoldEditPage()
                                    hitApiForRetailerWalletAmount()
                                    Toast.makeText(requireContext(),"The request has been successfully raised with the admin",Toast.LENGTH_SHORT).show()

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
                        ConstantClass.OpenPopUpForVeryfyOTP(requireContext())
                    }
                }
            }
        }
    }


    fun isValidForm(
        accountNumber: String,
        holdername:String,
        ifscCode: String,
        branchname: String,
        mode: String,
        amount: String,
    ): Pair<Boolean, String?> {
        // Bank details
        if (accountNumber.isBlank()) return Pair(false, "Enter account number")

        if (holdername.isBlank()) return Pair(false, "Enter holder name")

        if (!checkHoldername) return Pair(false, "Enter valid holder name")

        if (!ifscCode.matches(Regex("^[A-Z]{4}0[A-Z0-9]{6}$"))) return Pair(false, "Enter valid IFSC code")

        if (!checkIFSCcode) return Pair(false, "Enter valid IFSC code")


        if (branchname.isBlank()) return Pair(false, "Enter branch name")


        if (!checkbranchName) return Pair(false, "Enter valid branch name")


        if(mode.equals(ConstantClass.paymentMode)) return Pair (false, "Select payment mode")


        if (amount.isBlank()) return Pair(false, "Enter payout amount")

        if (!checkremarks) return Pair(false, "Enter valid remarks")


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

        txt.text = "Do you want to request a payout of ₹${binding.amountEdittxt.text.toString()} from your wallet?"

        done.setOnClickListener {
            hitApiForWalletPayout()

        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


    @SuppressLint("SetTextI18n")
    fun OpenPopUpForHoldAmountAlert() {
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

        txt.text = "Do you want to withdraw ₹${binding.holdamountetx.text.toString()} from your hold amount, which will be added to your wallet?"

        done.setOnClickListener {
            hitApiForHoldAmountRequest()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }



   fun hitApiForWalletPayout() {
        var totalamnt =  binding.amountEdittxt.text.toString().toDouble()
        val deviceIp = getDeviceIpAddress()
        Log.d("IP_CHECK", "Device IP: $deviceIp")


   var req = RetailerWalletPayoutReq(
       registrationId = preference.getStringValue(ConstantClass.RetailerCode,""),
       paymentMode = binding.paymentmode.selectedItem.toString().trim(),
       paymentDate = currentDate!!,
       transferAmount = totalamnt,
       beneId = binding.accountnumber.selectedItem.toString().trim(),
       accountHolder = binding.accountholdername.text.toString(),
       ifscCode = binding.ifsccode.text.toString().trim(),
       branchName = binding.branchname.text.toString(),
       remarks = binding.remarks.text.toString()
   )

   Log.d("WalletpayoutReq", Gson().toJson(req))

   viewModel.getRetailerWalletPayoutReq(req).observe(requireActivity()) { resources ->
       resources.let {
           when (it.apiStatus) {
               ApiStatus.SUCCESS -> {
                   it.data.let { users ->
                       users!!.body().let { response ->

                           if(dialog!=null && dialog.isShowing){
                               dialog.dismiss()
                           }

                           if (response!!.statuss.equals("True")) {
                               Log.d("WalletpayoutResp", Gson().toJson(response))
                               ConstantClass.dialog.dismiss()

                               if(dialog!=null && dialog.isShowing){
                                   dialog.dismiss()
                               }
                               hitApiForRetailerWalletAmount()
                               clearEditPage()
                               Toast.makeText(requireContext(),"The request has been successfully raised with the admin",Toast.LENGTH_SHORT).show()

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
                   ConstantClass.OpenPopUpForVeryfyOTP(requireContext())
               }
           }
       }
   }

}


    fun clearEditPage(){
        binding.accountnumber.setSelection(0)
        binding.paymentmode.setSelection(0)
        binding.amountEdittxt.text.clear()
        binding.remarks.text.clear()
    }

    fun clearHoldEditPage(){
        binding.holdamountetx.text.clear()
        binding.holdamountremarks.text.clear()
    }


    fun hitApiForRetailerWalletAmount(){
        var registrationID = preference.getStringValue(ConstantClass.RetailerCode, "")
        var request = RetailerWalletAmountReq(
            retailerID = registrationID ,
            amountType = "CreditBalance"
        )

        Log.d("walletAmountReq", Gson().toJson(request))
        viewModel.getRetailerWalletAmountReq(request).observe(requireActivity()) { resources ->
            resources.let {
                when (it.apiStatus) {

                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                                    ConstantClass.dialog.dismiss()
                                }

                                val walletAmount = response.walletBalance!!.toDoubleOrNull() ?: 0.0
                                val holdAmount = response.holdAmount!!.toDoubleOrNull() ?: 0.0
                                val maxholdAmount = response.maxholdAmount!!.toDoubleOrNull() ?: 0.0
                                val minholdAmount = response.miniholdamountrequest!!.toDoubleOrNull() ?: 0.0

                                AdminLoanApprovedStatus = response.loanApprovalStatus!!
                                AdminCibilScore = response.cibilScore!!

                                val myWalletAmount = walletAmount/* - holdAmount*/
                                val myWalletAmountStr = String.format("%.2f", myWalletAmount)

                                WalletBalance = myWalletAmountStr
                                HoldAmount = String.format("%.2f", holdAmount)
                                MaxHoldingAmount = String.format("%.2f", maxholdAmount)
                                MinHoldingAmount = String.format("%.2f", minholdAmount)

                                binding.walletamount.text = "₹ ".plus(WalletBalance)
                                binding.holdamount.text = "₹ ".plus(HoldAmount)

                                Log.d("AdminLoanApprovedStatus", "${response.loanApprovalStatus!!} ${response.cibilScore!!}")

                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                            ConstantClass.dialog.dismiss()
                        }
                        // ✅ Print the full error details
                        Log.e("API_ERROR", "Status: ERROR")
                        Log.e("API_ERROR_CODE", resources.data?.code().toString())
                        Log.e("API_ERROR_MSG", resources.message ?: "Unknown Error")

                        Toast.makeText(requireContext(), "Server error occurred (Code: ${resources.data?.code() ?: "Unknown"})", Toast.LENGTH_LONG).show()

                        // Optional: Handle specific 500 error
                        if (resources.data?.code() == 500) {
                            Log.e("API_ERROR", "Internal Server Error from backend.")
                        }

                    }

                    ApiStatus.LOADING -> {

                    }

                }
            }
        }


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


    fun hitApiForGetBankList(){

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
                                    ConstantClass.dialog.dismiss()
                                if(response!!.statuss.equals("True")){
                                    Log.d("BankListRes",Gson().toJson(response))
                                    bankDataList = response?.data!!
                                    if(bankDataList.isNullOrEmpty()){
                                        binding.addBankdetails.visibility=View.VISIBLE
                                        binding.bankdetailslayout.visibility= View.GONE
                                    }else{
                                        bankDataList!!.forEach { it->
                                            bankAccountNumber.add(it!!.accountNumber)
                                        }
                                        val adapter = ArrayAdapter(requireContext(), R.layout.mobilenamelayout,bankAccountNumber)
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                        binding.accountnumber.adapter = adapter
                                        binding.addBankdetails.visibility=View.GONE
                                        binding.bankdetailslayout.visibility= View.VISIBLE
                                    }

                                }
                                else{
                                binding.addBankdetails.visibility=View.VISIBLE
                                binding.bankdetailslayout.visibility= View.GONE
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


}