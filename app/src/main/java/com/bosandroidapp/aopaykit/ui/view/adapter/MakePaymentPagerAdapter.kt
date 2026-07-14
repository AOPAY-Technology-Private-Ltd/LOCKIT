package com.bosandroidapp.aopaykit.ui.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment.ReportPage
import com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment.AdminBankListActivity

private const val NUM_TABS = 2

class MakePaymentPagerAdapter (fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle) {


    override fun getItemCount(): Int {
        return NUM_TABS
    }


    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> AdminBankListActivity()
            1 -> ReportPage()
            else -> AdminBankListActivity()
        }

    }


}