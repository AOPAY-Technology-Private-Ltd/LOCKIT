package com.bosandroidapp.aopaykit.ui.view.activity.retailer.reports

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bosandroidapp.aopaykit.databinding.ActivityReportSelectionPageBinding

class ReportSelectionPage : AppCompatActivity() {

    lateinit var binding : ActivityReportSelectionPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityReportSelectionPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        setOnClickListner()

    }


    fun setOnClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.loanstatusreport.setOnClickListener {
             startActivity(Intent(this@ReportSelectionPage, RetailerCustomerReportsPage::class.java))
        }

        binding.pendingemireport.setOnClickListener {
            startActivity(Intent(this@ReportSelectionPage, DuesEMIPage::class.java))
        }

        binding.lowcibilreport.setOnClickListener {
            startActivity( Intent(this@ReportSelectionPage, LowCibilScoreCustomerReports::class.java))
        }

        binding.settlementReport.setOnClickListener {
            startActivity( Intent(this@ReportSelectionPage, SettlementLoanReport::class.java))
        }

        binding.payoutReport.setOnClickListener {
            startActivity(Intent(this@ReportSelectionPage, PayoutReport::class.java))
        }

        binding.Ledgerreport.setOnClickListener {
            startActivity(Intent(this@ReportSelectionPage,Ledgerreport::class.java))
        }

    }


}