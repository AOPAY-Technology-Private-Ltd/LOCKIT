package com.bosandroidapp.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bosandroidapp.aopaykit.data.model.cibilscore.CAISAccountDETAILSItem
import com.bosandroidapp.aopaykit.databinding.CibilaccountdetailsLayoutBinding
import com.bosandroidapp.aopaykit.databinding.FragmentCreditAccountsBinding


class CreditAccounts : Fragment() {

    lateinit var binding : FragmentCreditAccountsBinding
    lateinit var adapter : AccoutnDetailsAdapter

    companion object{
       var AccountList : MutableList<CAISAccountDETAILSItem> = mutableListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreditAccountsBinding.inflate(layoutInflater,container,false)
        initview()
        return binding.root
    }


    fun initview(){
        adapter = AccoutnDetailsAdapter(requireContext(), AccountList)
        binding.accountlist.adapter = adapter
        adapter.notifyDataSetChanged()

    }



    class AccoutnDetailsAdapter (var context: Context, var list:MutableList<CAISAccountDETAILSItem>): RecyclerView.Adapter<AccoutnDetailsAdapter.ViewHolder>(){



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccoutnDetailsAdapter.ViewHolder {
            val binding = CibilaccountdetailsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }


        override fun onBindViewHolder(holder: AccoutnDetailsAdapter.ViewHolder, position: Int) {
           holder.status.text = "Status:  " .plus(list[position].accountStatus)
           holder.reported.text = list[position].dateReported
           holder.loanamount.text = "LoanAmount : ".plus(list[position].highestCreditOrOriginalLoanAmount)
           holder.currentamount.text = "CurrentAmount : " .plus(list[position].currentBalance)
           holder.accountnumber.text = list[position].accountNumber
           holder.subname.text = list[position].subscriberName
        }


        class ViewHolder (private val binding: CibilaccountdetailsLayoutBinding): RecyclerView.ViewHolder(binding.root) {
            var status = binding.status
            var reported = binding.reporteddate
            var loanamount = binding.loanamount
            var currentamount = binding.currentamount
            var accountnumber = binding.accountnumber
            var subname = binding.subname
        }


        override fun getItemCount(): Int {
            return list.size
        }

    }


}