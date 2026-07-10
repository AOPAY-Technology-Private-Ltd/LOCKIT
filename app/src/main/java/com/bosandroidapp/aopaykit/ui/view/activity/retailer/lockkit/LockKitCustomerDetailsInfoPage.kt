package com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.databinding.ActivityLockKitCustomerDetailsInfoPageBinding
import com.bosandroidapp.aopaykit.ui.slideshow.activity.DashBoard
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment.CustomerActionFragment
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment.CustomerDeviceFragment
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit.fragment.CustomerInfoFragment
import com.bosandroidapp.aopaykit.ui.view.adapter.InfoTabAdapter

class LockKitCustomerDetailsInfoPage : AppCompatActivity() {
    lateinit var binding : ActivityLockKitCustomerDetailsInfoPageBinding
    private lateinit var tabAdapter: InfoTabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLockKitCustomerDetailsInfoPageBinding.inflate(layoutInflater)
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

        setupTabs()
        setOnClickListner()

        // Load initial fragment
        replaceFragment(CustomerInfoFragment())
    }

    private fun setupTabs() {
        val tabList = listOf("Info", "Device", "Action")
        
        tabAdapter = InfoTabAdapter(tabList) { selectedTab ->
            when (selectedTab) {
                "Info" -> replaceFragment(CustomerInfoFragment())
                "Device" -> replaceFragment(CustomerDeviceFragment())
                "Action" -> replaceFragment(CustomerActionFragment())
            }
        }

        binding.rvTabs.apply {
            layoutManager = LinearLayoutManager(this@LockKitCustomerDetailsInfoPage, LinearLayoutManager.HORIZONTAL, false)
            adapter = tabAdapter
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
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

}