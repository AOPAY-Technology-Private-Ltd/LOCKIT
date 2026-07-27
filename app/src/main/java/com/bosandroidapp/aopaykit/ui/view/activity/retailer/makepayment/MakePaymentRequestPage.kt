package com.bosandroidapp.aopaykit.ui.view.activity.retailer.makepayment

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.bosandroidapp.aopaykit.constant.ConstantClass.isInternetAvailable
import com.bosandroidapp.aopaykit.constant.ConstantClass.saveImageToCache
import com.bosandroidapp.aopaykit.data.model.RaiseMakePaymentReq
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.data.viewModelFactory.CommonViewModelFactory
import com.bosandroidapp.aopaykit.databinding.ActivityMakePaymentRequestPageBinding
import com.bosandroidapp.aopaykit.localdb.SharedPreference
import com.bosandroidapp.aopaykit.ui.viewmodel.AuthenticationViewModel
import com.bosandroidapp.aopaykit.utils.ApiStatus
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.io.File
import kotlin.toString

class MakePaymentRequestPage : AppCompatActivity() {
    lateinit var binding: ActivityMakePaymentRequestPageBinding
    lateinit var preference: SharedPreference
    var photoUri: Uri? = null
    var imagepath: String? = ""
    private val CAMERA_REQUEST_CODE_FRONT = 1001
    var transferMode: MutableList<String?> = arrayListOf()
    lateinit var viewModel: AuthenticationViewModel


    companion object {
        var BankName: String = ""
        var BankAccountNumber: String = ""
        var BankIFSCCODE: String = ""
        var BranchName: String = ""
        var BankHolderName: String = ""
        var checkQR: Boolean = false
    }


    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Handle the photoUri, e.g., show image in ImageView
                binding.receiptPhoto.visibility = View.VISIBLE
                binding.cameraicon.visibility = View.GONE

                Glide.with(this).load(photoUri).centerCrop().into(binding.receiptPhoto)

                binding.clicktosealphoto1.text = "Reupload"
                imagepath = saveImageToCache(this, photoUri!!, "ReceiptPhoto")!!.absolutePath
            }
            else {
                photoUri = null
            }

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMakePaymentRequestPageBinding.inflate(layoutInflater)
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

        viewModel = ViewModelProvider(
            this,
            CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface))
        )[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)

        setTransferModepinner()
        setOnClickListner()
    }

    private fun setOnClickListner() {

        binding.back.setOnClickListener {
            finish()
        }

        binding.transfermode.onItemSelectedListener = object : OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var mode = binding.transfermode.selectedItem.toString().trim()
                if (mode.equals("Cash Deposit", ignoreCase = true)) {
                    binding.transactionIdetv.visibility = View.GONE
                    binding.transactiontitle.visibility = View.GONE
                    binding.uploadpiclayout.visibility = View.GONE
                } else {
                    binding.transactionIdetv.visibility = View.VISIBLE
                    binding.transactiontitle.visibility = View.VISIBLE
                    binding.uploadpiclayout.visibility = View.VISIBLE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        binding.amountetxtve.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()

                if (input.contains(".")) {
                    val decimalIndex = input.indexOf(".")
                    val digitsAfterDecimal = input.length - decimalIndex - 1

                    if (digitsAfterDecimal > 2) {
                        // Trim extra digits
                        val trimmed = input.substring(0, decimalIndex + 3)
                        binding.amountetxtve.setText(trimmed)
                        binding.amountetxtve.setSelection(trimmed.length) // move cursor to end
                    }
                }

            }
        })

        binding.clicktosealphoto1.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }

        binding.confirmbutton.setOnClickListener {
            if (!isInternetAvailable(this)) {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amountToBeTransfer = binding.amountetxtve.text.toString().trim()
            val transactionId = binding.transactionIdetv.text.toString().trim()
            val branchCode = binding.branchcodetxt.text.toString().trim()
            val remarks = binding.remarksetxtev.text.toString().trim()
            val transferMode = binding.transfermode.selectedItem.toString()

            // ✅ Validate branch code
            if (branchCode.isEmpty()) {
                Toast.makeText(this, "Branch code not found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Validate amount or transaction based on transfer mode

            if (transferMode.equals(ConstantClass.CashDeposit, ignoreCase = true)) {
                if (amountToBeTransfer.isEmpty()) {
                    Toast.makeText(this, "Please enter paid amount", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                // ✅ Validate remarks
                if (remarks.isEmpty()) {
                    Toast.makeText(this, "Please enter remarks", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            else{

                if (transactionId.isEmpty()) {
                    Toast.makeText(this, "Please enter transaction ID", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (amountToBeTransfer.isEmpty()) {
                    Toast.makeText(this, "Please enter paid amount", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val amount = amountToBeTransfer.toDoubleOrNull()

                if (amount == null || amount <= 0) {
                    Toast.makeText(this, "Amount must be greater than 0", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // ✅ Validate remarks
                if (remarks.isEmpty()) {
                    Toast.makeText(this, "Please enter remarks", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                if (imagepath.isNullOrBlank()) {
                    Toast.makeText(this, "Please upload receipt photo", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


            }


            // ✅ All validations passed — proceed with API call
            HitApiForTransferAmountToAdminAccount()
        }

    }


    fun HitApiForTransferAmountToAdminAccount() {
        var retailerCode = preference.getStringValue(ConstantClass.RetailerCode, "")
        var cancelcheque: File? = null
        if (photoUri == null) {

        } else {
            cancelcheque = saveImageToCache(this, photoUri!!, "cancelcheque.jpg")
        }

        var registationRequest = RaiseMakePaymentReq(
            RetailerCode = retailerCode,
            RequestAmount = binding.amountetxtve.text.toString().trim(),
            PaymentMode = binding.transfermode.selectedItem.toString().trim(),
            BankName = binding.bankName.text.toString().trim(),
            AccountHolderName = BankHolderName.toString().trim(),
            AccountNumber = BankAccountNumber.toString().trim(),
            IFSCCode = BankIFSCCODE.toString().trim(),
            UTRNumber = binding.transactionIdetv.text.toString().trim(),
            UPIID = "",
            Remarks = binding.remarksetxtev.text.toString().trim(),
            ApprovedRemarks = "",
            CreatedBy = "",
            RecordStatus = "Pending",
            ActiveStatus = "Active",
            cancelcheque
        )

        Log.d("RegistationRequest", Gson().toJson(registationRequest))

        viewModel.uploadDocumentForRaisAmountTransferAdminReq(registationRequest)
            .observe(this) { resources ->
                resources.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data.let { users ->
                                users!!.body().let { response ->
                                    ConstantClass.dialog.dismiss()
                                    if (response!!.statuss!!.toLowerCase().equals("true", ignoreCase = true)) {
                                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                                        finish()
                                    }
                                    else {
                                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                                    }

                                }

                            }

                        }

                        ApiStatus.ERROR -> {
                            ConstantClass.dialog.dismiss()
                        }

                        ApiStatus.LOADING -> {
                            ConstantClass.OpenLoader(this)
                        }

                    }
                }
            }


    }

    private fun setTransferModepinner() {
        transferMode.clear()
        if (checkQR) {
            transferMode.add("QR")
        }
        transferMode.add("Cash Deposit")
        transferMode.add("UPI")
        transferMode.add("NEFT")
        transferMode.add("RTGS")
        transferMode.add("IMPS")

        var adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, transferMode)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.transfermode.adapter = adapter

        binding.bankName.text = BankName
        binding.branchcodetxt.text = BranchName
        binding.accountNumber.text = BankAccountNumber
        binding.ifscCode.text = BankAccountNumber
    }


    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            clickCameraForUploadDocument()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE_FRONT
            )
        }
    }


    fun clickCameraForUploadDocument() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(this, "${this.packageName}.fileprovider", photoFile)
        cameraLauncher.launch(photoUri!!)
    }


    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

}