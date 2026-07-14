package com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityLockKitCustomerListPageBinding
import com.bosandroidapp.aopaykit.ui.slideshow.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.view.adapter.LockKitCustomerListAdapter

class LockKitCustomerListPage : AppCompatActivity() {

    lateinit var binding : ActivityLockKitCustomerListPageBinding
    lateinit var adapter : LockKitCustomerListAdapter

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

        setDataInList()
        setOnClickListner()


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


   fun setDataInList(){
        adapter = LockKitCustomerListAdapter(this,/* 5*/)
        binding.lockkitcustomerlist.adapter = adapter
        binding.lockkitcustomerlist.visibility=View.VISIBLE
        binding.notfoundimage.visibility=View.GONE
    }

}