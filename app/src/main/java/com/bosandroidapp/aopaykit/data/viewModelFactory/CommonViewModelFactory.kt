package com.bosandroidapp.aopaykit.data.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel

class CommonViewModelFactory(private val repository: AuthRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> AuthenticationViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}