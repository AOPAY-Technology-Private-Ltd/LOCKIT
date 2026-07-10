package com.bosandroidapp.aopaykit.data.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bosandroidapp.aopaykit.data.repository.CibilRepository
import com.bosandroidapp.aopaykit.ui.viewmodel.CibilViewModel


class CibilViewModelFactory (private val repository: CibilRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(CibilViewModel::class.java) -> CibilViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}