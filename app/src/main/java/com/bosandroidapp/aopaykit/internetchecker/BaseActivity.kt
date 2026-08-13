package com.bosandroidapp.aopaykit.internetchecker

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.CustPrimaryMobileVerified
import com.bosandroidapp.aopaykit.utils.ApplicationClass
import kotlinx.coroutines.launch

open class BaseActivity : AppCompatActivity() {
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initial check
        if (!ConstantClass.isInternetAvailable(this)) {
            if (dialog == null || !dialog!!.isShowing) {
                dialog = ConstantClass.showNoInternetDialog(this)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                ApplicationClass.isNetworkAvailable.collect { connected ->
                    if (connected) {
                        dialog?.dismiss()
                        dialog = null
                    }
                    else {
                        if (dialog == null || !dialog!!.isShowing) {
                            dialog = ConstantClass.showNoInternetDialog(this@BaseActivity)
                        }
                    }
                }
            }
        }

    }


    override fun onPause() {
        super.onPause()
        try {
            if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                ConstantClass.dialog.dismiss()
            }
            
            if (dialog != null && dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        } catch (e: Exception) {
        }
    }


}