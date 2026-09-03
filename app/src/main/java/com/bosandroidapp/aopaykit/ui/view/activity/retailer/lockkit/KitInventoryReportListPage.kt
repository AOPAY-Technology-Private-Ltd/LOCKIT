package com.bosandroidapp.aopaykit.ui.view.activity.retailer.lockkit

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.R
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.data.customeraction.kitinventory.GetKitInventoryListRequest
import com.bosandroidapp.aopaykit.data.customeraction.kitinventory.GetKitInventoryListResponse
import com.bosandroidapp.aopaykit.data.customeraction.kitinventory.KitListItem
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityKitInventoryReportListPageBinding
import com.bosandroidapp.aopaykit.internetchecker.BaseActivity
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.view.adapter.KitInventoryAdapter
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus

class KitInventoryReportListPage : BaseActivity() {

    private lateinit var binding: ActivityKitInventoryReportListPageBinding
    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var preference: SharedPreference
    private var adapter: KitInventoryAdapter? = null
    private var fullKitList: List<KitListItem> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKitInventoryReportListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.clHeader.setPadding(0, systemBarsInsets.top, 0, binding.clHeader.paddingBottom)
            view.setPadding(systemBarsInsets.left, 0, systemBarsInsets.right, systemBarsInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)

        setupListeners()
        fetchInventoryReport()
    }

    private fun setupListeners() {
        binding.back.setOnClickListener { finish() }

        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                filterList(checkedId)
            }
        }
    }

    private fun fetchInventoryReport() {
        val retailerCode = preference.getStringValue(ConstantClass.RetailerCode, "")
        val request = GetKitInventoryListRequest(
           /* clientCode = preference.getStringValue(ConstantClass.ClientCode, ""),*/
            retailerCode = retailerCode,
            status = "ALL"
        )

        viewModel.getkitInventoryListRequest(request).observe(this) { resources ->
            when (resources.apiStatus) {
                ApiStatus.SUCCESS -> {
                    ConstantClass.dialog.dismiss()
                    val response = resources.data?.body()
                    if (response != null) {
                        updateUI(response)
                    }
                }
                ApiStatus.LOADING -> ConstantClass.OpenLoader(this)
                ApiStatus.ERROR -> ConstantClass.dialog.dismiss()
            }
        }
    }

    private fun updateUI(response: GetKitInventoryListResponse) {
        binding.tvTotalKits.text = response.totalKits?.toString() ?: "0"
        binding.tvAvailableKits.text = response.availableKits?.toString() ?: "0"
        binding.tvUsedKits.text = response.usedKits?.toString() ?: "0"
        // Hide Closed if not in API, or set to 0

        fullKitList = response.kitList?.filterNotNull() ?: listOf()
        adapter = KitInventoryAdapter(fullKitList)
        binding.rvKitInventory.adapter = adapter

        if (fullKitList.isEmpty()) {
            binding.notfoundimage.visibility = View.VISIBLE
            binding.rvKitInventory.visibility = View.GONE
        } else {
            binding.notfoundimage.visibility = View.GONE
            binding.rvKitInventory.visibility = View.VISIBLE
        }
    }

    private fun filterList(checkedId: Int) {
        val filtered = when (checkedId) {
            R.id.btnAvailable -> fullKitList.filter { it.kitStatus == "AVAILABLE" }
            R.id.btnUsed -> fullKitList.filter { it.kitStatus == "USED" }
            R.id.btnClosed -> fullKitList.filter { it.kitStatus == "CLOSED" }
            else -> fullKitList
        }
        adapter?.updateList(filtered)
        binding.notfoundimage.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
    }


}
