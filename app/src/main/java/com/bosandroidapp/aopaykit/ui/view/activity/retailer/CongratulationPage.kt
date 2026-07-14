package com.bosandroidapp.aopaykit.ui.view.activity.retailer

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.ApiInterface
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.databinding.ActivityCongratulationPageBinding

import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.ui.slideshow.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel

class CongratulationPage : AppCompatActivity() {
    lateinit var binding: ActivityCongratulationPageBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var api: ApiInterface

    companion object{
        var loaneCode:String = ""
        var customerCode:String = ""
        var FirstName:String = ""
        var MiddleName:String = ""
        var LastName:String = ""
        var Message:String = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityCongratulationPageBinding.inflate(layoutInflater)
         setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        setView()
        setOnClickListner()

    }


    fun setView(){
        if(ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.kit)){
            binding.LoanCode.text = customerCode
            binding.message.text = "Kit installation completed successfully for ${ FirstName } ${MiddleName} ${LastName} ."
            binding.customerName.visibility=View.GONE
        }
        else{
            binding.customerName.visibility=View.VISIBLE
            binding.LoanCode.text = loaneCode
            binding.message.text = getString(com.bosandroidapp.aopaykit.R.string.your_loan)
            binding.customerName.text =  FirstName .plus(" ").plus(MiddleName).plus(" ").plus(LastName) . plus("")
        }


        binding.downloadtxt.text=ConstantClass.Exit

        api = RetrofitClient.apiInterface
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

    }


    fun setOnClickListner(){

        binding.nextlayout.setOnClickListener {
            val intent = Intent(this@CongratulationPage, DashBoard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }


    override fun onBackPressed() {
        val intent = Intent(this@CongratulationPage, DashBoard::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}