package com.bosandroidapp.aopaykit.ui.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bosandroidapp.aopaykit.ui.view.activity.fragment.AddBank
import com.bosandroidapp.aopaykit.ui.view.activity.fragment.BankListPage

private const val NUM_TABS = 2
class BankPagerAdapter (fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle) {


    override fun getItemCount(): Int {
        return NUM_TABS
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BankListPage()
            1-> AddBank()
            else -> BankListPage()
        }

    }

}