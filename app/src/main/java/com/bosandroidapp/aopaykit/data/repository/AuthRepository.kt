package com.bosandroidapp.aopaykit.data.repository

import com.bos.payment.appName.network.ApiInterface
import com.bosandroidapp.aopaykit.data.customeraction.CustomerSideUpdateUnInstallAppRequest
import com.bosandroidapp.aopaykit.data.customeraction.GetKitCustomerLocation
import com.bosandroidapp.aopaykit.data.customeraction.kitinventory.GetKitInventoryListRequest
import com.bosandroidapp.aopaykit.data.customeraction.GetPendingDeviceActionReq
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSaveDeviceActionRequest
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSendNotificationToCustomerReq
import com.bosandroidapp.aopaykit.data.customeraction.SaveRetailerDeviceTokenRequest
import com.bosandroidapp.aopaykit.data.customeraction.SendInstalledAppOnServerRequest
import com.bosandroidapp.aopaykit.data.customeraction.UpdateCustomerDeviceActionRequest
import com.bosandroidapp.aopaykit.data.customeraction.UploadCustomerLocationRequest
import com.bosandroidapp.aopaykit.data.enach.EnachDateUploadReq
import com.bosandroidapp.aopaykit.data.model.AdminBankDetailsReq
import com.bosandroidapp.aopaykit.data.model.CustomerEmiStatusReq
import com.bosandroidapp.aopaykit.data.model.CustomerKitRequest
import com.bosandroidapp.aopaykit.data.model.CustomerlocationUploadReq
import com.bosandroidapp.aopaykit.data.model.DueOverdueRequest
import com.bosandroidapp.aopaykit.data.model.GenerateAccessTokenRequest
import com.bosandroidapp.aopaykit.data.model.GetRetailerLedgerReq
import com.bosandroidapp.aopaykit.data.model.HoldAmountWithdrawReq
import com.bosandroidapp.aopaykit.data.model.LowCibilCustomerReportReq
import com.bosandroidapp.aopaykit.data.model.MakePaymentAdminReportRequest
import com.bosandroidapp.aopaykit.data.model.MakepaymentResp
import com.bosandroidapp.aopaykit.data.model.RaiseMakePaymentReq
import com.bosandroidapp.aopaykit.data.model.RetailerWalletAmountReq
import com.bosandroidapp.aopaykit.data.model.RetailerWalletPayoutAtMakePaymentTimeReq
import com.bosandroidapp.aopaykit.data.model.RetailerWalletReportReq
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.UploadDeviceInfoReq
import com.bosandroidapp.aopaykit.data.model.ValidateAccessKeyReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.VerifyCustomerReq
import com.bosandroidapp.aopaykit.data.model.kitoption.KitCustomerListResponse
import com.bosandroidapp.aopaykit.data.model.kitoption.KitOptionRequest
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPlanRequest
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPurchaseHistoryRequest
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPurchasePlanSaveRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerLoanEmiReceiveReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerMakePaymentResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.ForgotPasswordReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetEMISplitDetlailsReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetIsEligibleLoanReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LoanCreatedReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LoginReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.RegistrationReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.RegistrationRes
import com.bosandroidapp.aopaykit.data.model.loginsignup.RetailerProfileReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.RetailerWalletPayoutReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.SendOtpReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.VerifyOTPReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.GetReportsReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.LoanSettlementReportReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.PayoutReportReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.TransactionHistoryReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.AadharVerificationReq
import com.bosandroidapp.aopaykit.data.notification.NotificationSendTokenRequest
import com.bosandroidapp.aopaykit.data.notification.SendNotificationFeatureNameRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class AuthRepository(private val apiInterface: ApiInterface) {

  companion object {
    private val _customerListUpdate = MutableSharedFlow<Unit>(replay = 1)
    val customerListUpdate: SharedFlow<Unit> = _customerListUpdate.asSharedFlow()

    suspend fun notifyCustomerListChanged() {
      _customerListUpdate.emit(Unit)
    }

  }

  suspend fun getregistration(req: RegistrationReq): Response<RegistrationRes> {
    val firstname = req.firstName.toRequestBody("text/plain".toMediaTypeOrNull())
    val lastname = req.lastName.toRequestBody("text/plain".toMediaTypeOrNull())
    val mob = req.mobileNumber.toRequestBody("text/plain".toMediaTypeOrNull())
    val mailid = req.emailId.toRequestBody("text/plain".toMediaTypeOrNull())
    val password = req.password.toRequestBody("text/plain".toMediaTypeOrNull())
    val cnfrmpassword = req.confrmpassword.toRequestBody("text/plain".toMediaTypeOrNull())
    val address = req.address.toRequestBody("text/plain".toMediaTypeOrNull())
    val aadhaarnumber = req.aadharnumber.toRequestBody("text/plain".toMediaTypeOrNull())
    val pannumber = req.panNumber.toRequestBody("text/plain".toMediaTypeOrNull())
    val storename = req.storeName.toRequestBody("text/plain".toMediaTypeOrNull())
    val storeaddress = req.storeAddress.toRequestBody("text/plain".toMediaTypeOrNull())


    // Convert image file to MultipartBody.Part
    val profilePhoto = if (req.profilePhoto != null && req.profilePhoto.exists()) {
      val requestFile = req.profilePhoto.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("Profile_Photo_FileName", req.profilePhoto.name, requestFile)
    } else {
      // send empty multipart field
      MultipartBody.Part.createFormData("Profile_Photo_FileName", "")
    }


    val aadhaarfront = if (req.aadhaarfront != null && req.aadhaarfront.exists()) {
      val requestFile = req.aadhaarfront.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("Adhaar_front_photo_FileName", req.aadhaarfront.name, requestFile)
    } else {
      // send empty multipart field
      MultipartBody.Part.createFormData("Adhaar_front_photo_FileName", "")
    }


    val aadhaarback = if (req.aadhaarback != null && req.aadhaarback.exists()) {
      val requestFile = req.aadhaarback.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("Adhaar_back_Photo_FileName", req.aadhaarback.name, requestFile)
    } else {
      // send empty multipart field
      MultipartBody.Part.createFormData("Adhaar_back_Photo_FileName", "")
    }

    val pancardfront = if (req.pancardfront != null && req.pancardfront.exists()) {
      val requestFile = req.pancardfront.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("PanCard_fornt_Photo_FileName", req.pancardfront.name, requestFile)
    } else {
      // send empty multipart field
      MultipartBody.Part.createFormData("PanCard_fornt_Photo_FileName", "")
    }

    val cancelcheque = if (req.cancelcheque != null && req.cancelcheque.exists()) {
      val requestFile = req.cancelcheque.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("cancle_cheque_Photo_FileName", req.cancelcheque.name, requestFile)
    } else {
      // send empty multipart field
      MultipartBody.Part.createFormData("cancle_cheque_Photo_FileName", "")
    }

    val storefront = if (req.storefront != null && req.storefront.exists()) {
      val requestFile = req.storefront.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("store_front_Photo_FileName", req.storefront.name, requestFile)
    } else {
      // send empty multipart field
      MultipartBody.Part.createFormData("store_front_Photo_FileName", "")
    }

    val companydoc = if (req.companydoc != null && req.companydoc.exists()) {
      val requestFile = req.companydoc.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("company_doc_Photo_FileName", req.companydoc.name, requestFile)
    } else {
      // send empty multipart field
      MultipartBody.Part.createFormData("company_doc_Photo_FileName", "")
    }

    return apiInterface.registration(firstname,lastname,mob,mailid,cnfrmpassword,password,address,aadhaarnumber,pannumber,storename,storeaddress,
      profilePhoto,aadhaarfront,aadhaarback,pancardfront,cancelcheque,storefront,companydoc)

  }

  suspend fun getlogin(req: LoginReq) = apiInterface.login(req)

  suspend fun getlogout(req: LogoutReq) = apiInterface.logout(req)

  suspend fun sendOTP(req: SendOtpReq) = apiInterface.sendOTP(req)

  suspend fun verifyOTPReq(req: VerifyOTPReq) = apiInterface.verifyOTP(req)

  suspend fun forgotPassword(req: ForgotPasswordReq) = apiInterface.forgotPassword(req)

  suspend fun getMobileList() = apiInterface.getAllDeviceDetails()

  suspend fun verifycustomerReq(req: VerifyCustomerReq) = apiInterface.verifycustomerReq(req)

  suspend fun verifyKitcustomerReq(req: VerifyCustomerReq) = apiInterface.verifyKitcustomerReq(req)

  suspend fun getEmiSplitData(req: GetEMISplitDetlailsReq) = apiInterface.getEmiSplitDataDetails(req)

  suspend fun getRetailerLoanCreatedReq(req:LoanCreatedReq) = apiInterface.getLoanCreatedByRetailer(req)

  suspend fun getCustomerLoanEmiDetailsReq(req: GetCustomerLoanDetailsReq) = apiInterface.getCustomerLoanDetailsList(req)

  suspend fun getTransactionHistoryList(req: TransactionHistoryReq) = apiInterface.getTransactionHistoryList(req)

  suspend fun dueoverdueCustomerRequest(req: DueOverdueRequest) = apiInterface.dueoverdueCustomerRequest(req)


  suspend fun getCustomerLoanEmiReceiveReq(req: CustomerLoanEmiReceiveReq): Response<CustomerMakePaymentResp> {
    val mode = req.mode.toRequestBody("text/plain".toMediaTypeOrNull())
    val loanCode = req.loanCode.toRequestBody("text/plain".toMediaTypeOrNull())
    val paymentDate = req.paymentDate.toRequestBody("text/plain".toMediaTypeOrNull())
    val paymentMode = req.paymentMode.toRequestBody("text/plain".toMediaTypeOrNull())
    val utrNumber = req.utrNumber.toRequestBody("text/plain".toMediaTypeOrNull())
    val remarks = req.remarks.toRequestBody("text/plain".toMediaTypeOrNull())
    val createdBy = req.createdBy.toRequestBody("text/plain".toMediaTypeOrNull())
    val Bankname = req.bankName.toRequestBody("text/plain".toMediaTypeOrNull())
    val customerCode = req.customerCode.toRequestBody("text/plain".toMediaTypeOrNull())
    val retailerCode = req.retailerCode.toRequestBody("text/plain".toMediaTypeOrNull())
    val receiptImagePath = req.receiptImagePath.toRequestBody("text/plain".toMediaTypeOrNull())

//
//    // Convert image file to MultipartBody.Part
//    val imagePart = if (req.imageFile != null && req.imageFile.exists()) {
//      val requestFile = req.imageFile.asRequestBody("image/*".toMediaTypeOrNull())
//      MultipartBody.Part.createFormData("ReceiptImage_FileName", req.imageFile.name, requestFile)
//    }
//    else {
//      // send empty multipart field
//        MultipartBody.Part.createFormData("ReceiptImage_FileName", "")
//    }

    val imagePart =  MultipartBody.Part.createFormData("ReceiptImage_FileName", "")
    return apiInterface.getcustomerLoanEmiReceive(mode, loanCode,  paymentDate, paymentMode, utrNumber, remarks, createdBy, customerCode, retailerCode, Bankname,receiptImagePath, imagePart)

  }

  suspend fun RetailerWalletPayoutReqForMakePayment(req: RetailerWalletPayoutAtMakePaymentTimeReq) = apiInterface.RetailerWalletPayoutReq(req)

  suspend fun getAadharVerificationReq(req: AadharVerificationReq) = apiInterface.getAadharVarification(req)

  suspend fun getReportsReq(req: GetReportsReq) = apiInterface.getReports(req)

  suspend fun getLoanEligibleReq(req: GetIsEligibleLoanReq) = apiInterface.getEligiblereq(req)

  suspend fun getMemberShipReq(req: GetIsEligibleLoanReq) = apiInterface.getMemberShipReq(req)

  suspend fun getRetailerProfileReq(req: RetailerProfileReq) = apiInterface.getRetailerProfileGetUpdateReq(req)

  suspend fun getRetailerLedgerReq(req: GetRetailerLedgerReq) = apiInterface.getRetailerLedgerReq(req)

  suspend fun getRetailerWalletPayoutReq(req: RetailerWalletPayoutReq) = apiInterface.getRetailerWalletPayoutReq(req)

  suspend fun loanSettlementReportReq(req: LoanSettlementReportReq) = apiInterface.loanSettlementReportReq(req)

  suspend fun getRetailerWalletAmountReq(req: RetailerWalletAmountReq) = apiInterface.getRetailerWalletAmountReq(req)

  suspend fun getRetailerWalletReportReq(req: RetailerWalletReportReq) = apiInterface.getRetailerWalletReport(req)

  suspend fun getPayoutReportReq(req: PayoutReportReq) = apiInterface.getPayoutReportReq(req)

  suspend fun getAddBankAccountReq(req: com.bosandroidapp.aopaykit.data.model.AddBankAccountReq) = apiInterface.addBankAccounts(req)

  suspend fun requestHoldAmountWithdrawRequest(req: HoldAmountWithdrawReq) = apiInterface.requestHoldAmountWithdrawRequest(req)

  suspend fun getLowCibilReports(req: LowCibilCustomerReportReq) = apiInterface.getLowCibilReports(req)

  suspend fun uploadcustomerlocation(req: CustomerlocationUploadReq) = apiInterface.uploadcustomerlocation(req)

  suspend fun getAccessKeyForValidateAPKReq(req: GenerateAccessTokenRequest) = apiInterface.getAccessKeyForValidateAPKReq(req)

  suspend fun validateTokenFromRetailerReq(req: ValidateAccessKeyReq) = apiInterface.validateTokenFromRetailerReq(req)

  suspend fun sessionOutReq(req: SessionOutReq) = apiInterface.sessionOutReq(req)

  suspend fun sessionExpired(req: ValidateSessionRequest) = apiInterface.sessionExpired(req)

  suspend fun uploadDeviceInfo(req: UploadDeviceInfoReq) = apiInterface.uploadDeviceInfo(req)

  suspend fun UpdateEmandateDetails(req: EnachDateUploadReq) = apiInterface.UpdateEmandateDetails(req)

  suspend fun sendTokenViaNotificationReq(req: NotificationSendTokenRequest) = apiInterface.sendTokenViaNotificationReq(req)

  suspend fun updateActionFromCustomerDevice(req: UpdateCustomerDeviceActionRequest) = apiInterface.updateActionFromCustomerDevice(req)

  suspend fun updateAppUninstallStatusReq(req: CustomerSideUpdateUnInstallAppRequest) = apiInterface.updateAppUninstallStatusReq(req)

  suspend fun uploadKitCustomerLocationRequest(req: UploadCustomerLocationRequest) = apiInterface.uploadKitCustomerLocationRequest(req)

  suspend fun sendNotificationFeatureNameReq(req: SendNotificationFeatureNameRequest) = apiInterface.sendNotificationFeatureNameReq(req)

  suspend fun getPurchaseHistoryRequest(req: KitPurchaseHistoryRequest) = apiInterface.getPurchaseHistoryRequest(req)


  suspend fun savePurchaseKitPlan(req: KitPurchasePlanSaveRequest) = apiInterface.savePurchaseKitPlan(req)


  suspend fun LoanEmIScheduleWithStatusReq(req: CustomerEmiStatusReq) = apiInterface.LoanEmIScheduleWithStatusReq(req)


  suspend fun GetAdminBankDetailsReq(req: AdminBankDetailsReq) = apiInterface.GetAdminBankDetailsReq(req)


  suspend fun uploadDocumentForRaisAmountTransferAdminReq(req: RaiseMakePaymentReq): retrofit2.Response<MakepaymentResp> {
    val RetailerCode = req.RetailerCode.toRequestBody("text/plain".toMediaTypeOrNull())
    val RequestAmount = req.RequestAmount.toRequestBody("text/plain".toMediaTypeOrNull())
    val PaymentMode = req.PaymentMode.toRequestBody("text/plain".toMediaTypeOrNull())
    val BankName = req.BankName.toRequestBody("text/plain".toMediaTypeOrNull())
    val AccountHolderName = req.AccountHolderName.toRequestBody("text/plain".toMediaTypeOrNull())
    val AccountNumber = req.AccountNumber.toRequestBody("text/plain".toMediaTypeOrNull())
    val IFSCCode = req.IFSCCode.toRequestBody("text/plain".toMediaTypeOrNull())
    val UTRNumber = req.UTRNumber.toRequestBody("text/plain".toMediaTypeOrNull())
    val UPIID = req.UPIID.toRequestBody("text/plain".toMediaTypeOrNull())
    val Remarks = req.Remarks.toRequestBody("text/plain".toMediaTypeOrNull())
    val ApprovedRemarks = req.ApprovedRemarks.toRequestBody("text/plain".toMediaTypeOrNull())
    val CreatedBy = req.CreatedBy.toRequestBody("text/plain".toMediaTypeOrNull())
    val RecordStatus = req.RecordStatus.toRequestBody("text/plain".toMediaTypeOrNull())
    val ActiveStatus = req.ActiveStatus.toRequestBody("text/plain".toMediaTypeOrNull())

    // Convert image file to MultipartBody.Part
    val imagePart1 = if (req.imagefile1 != null && req.imagefile1.exists()) {
      val requestFile = req.imagefile1.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("ReceiptImage_FileName", req.imagefile1.name, requestFile)
    } else {
      // send empty multipart field
      MultipartBody.Part.createFormData("ReceiptImage_FileName", "")
    }


    return apiInterface.uploadDocumentForRaisAmountTransferAdmin(RetailerCode,RequestAmount,PaymentMode,BankName,AccountHolderName,AccountNumber,IFSCCode,UTRNumber,UPIID,Remarks,ApprovedRemarks,CreatedBy,RecordStatus,ActiveStatus,imagePart1)

  }
  suspend fun getMakePaymentReportReq(req: MakePaymentAdminReportRequest) = apiInterface.getMakePaymentReportReq(req)
  suspend fun kitPlanTopUpRequest(req: KitPlanRequest) = apiInterface.kitPlanTopUpRequest(req)
  suspend fun getRequestKitOption(req: KitOptionRequest) = apiInterface.getRequestKitOption(req)
  suspend fun getKitCustomerLocation(req: GetKitCustomerLocation) = apiInterface.getKitCustomerLocation(req)
  suspend fun getRetailerDeviceActionToCustomerRequest(req: RetailerSaveDeviceActionRequest) = apiInterface.getRetailerDeviceActionToCustomerRequest(req)
  suspend fun sendRetailerNotificationToCustomerRequest(req: RetailerSendNotificationToCustomerReq) = apiInterface.sendRetailerNotificationToCustomerRequest(req)
  suspend fun getPendingDeviceActionRequest(req: GetPendingDeviceActionReq) = apiInterface.getPendingDeviceActionRequest(req)
  suspend fun getActiveDeviceActionRequest(req: GetPendingDeviceActionReq) = apiInterface.getActiveDeviceActionRequest(req)
  suspend fun uploadCustomerDeviceInsatlledAppsOnServerRequest(req: SendInstalledAppOnServerRequest) = apiInterface.uploadCustomerDeviceInsatlledAppsOnServerRequest(req)

  suspend fun getkitInventoryListRequest(req: GetKitInventoryListRequest) = apiInterface.getkitInventoryListRequest(req)

  suspend fun getKitCustomerInstalledAppRequest(createdBy: String) = apiInterface.getKitCustomerInstalledAppRequest(createdBy)

  suspend fun saveRetailerDeviceTokenRequest(req : SaveRetailerDeviceTokenRequest) = apiInterface.saveRetailerDeviceTokenRequest(req)

  suspend fun getCustomerKitRequest(req: CustomerKitRequest): Response<KitCustomerListResponse> {
    val mode = req.mode.toRequestBody("text/plain".toMediaTypeOrNull())
    val firstName = req.firstName.toRequestBody("text/plain".toMediaTypeOrNull())
    val middleName = req.middleName.toRequestBody("text/plain".toMediaTypeOrNull())
    val lastName = req.lastName.toRequestBody("text/plain".toMediaTypeOrNull())
    val primaryMobileNumber = req.primaryMobileNumber.toRequestBody("text/plain".toMediaTypeOrNull())
    val primaryOTP = req.primaryOTP.toRequestBody("text/plain".toMediaTypeOrNull())
    val primaryMobileVerified = req.primaryMobileVerified.toRequestBody("text/plain".toMediaTypeOrNull())
    val alternateMobileNumber = req.alternateMobileNumber.toRequestBody("text/plain".toMediaTypeOrNull())
    val alternateMobileOTP = req.alternateMobileOTP.toRequestBody("text/plain".toMediaTypeOrNull())
    val pAlternateMobileVerified = req.pAlternateMobileVerified.toRequestBody("text/plain".toMediaTypeOrNull())
    val eMailID = req.eMailID.toRequestBody("text/plain".toMediaTypeOrNull())
    val flatNo = req.flatNo.toRequestBody("text/plain".toMediaTypeOrNull())
    val aearSector = req.aearSector.toRequestBody("text/plain".toMediaTypeOrNull())
    val pinCode = req.pinCode.toRequestBody("text/plain".toMediaTypeOrNull())
    val currentAddress = req.currentAddress.toRequestBody("text/plain".toMediaTypeOrNull())
    val stateName = req.stateName.toRequestBody("text/plain".toMediaTypeOrNull())
    val cityName = req.cityName.toRequestBody("text/plain".toMediaTypeOrNull())
    val country = req.country.toRequestBody("text/plain".toMediaTypeOrNull())
    val aadharNumber = req.aadharNumber.toRequestBody("text/plain".toMediaTypeOrNull())
    val aadharNumberVerified = req.aadharNumberVerified.toRequestBody("text/plain".toMediaTypeOrNull())
    val panNumber = req.panNumber.toRequestBody("text/plain".toMediaTypeOrNull())
    val panNumberVerified = req.panNumberVerified.toRequestBody("text/plain".toMediaTypeOrNull())
    val brandName = req.brandName.toRequestBody("text/plain".toMediaTypeOrNull())
    val modelName = req.modelName.toRequestBody("text/plain".toMediaTypeOrNull())
    val modelVariant = req.modelVariant.toRequestBody("text/plain".toMediaTypeOrNull())
    val color = req.color.toRequestBody("text/plain".toMediaTypeOrNull())
    val sellingPrice = req.sellingPrice.toRequestBody("text/plain".toMediaTypeOrNull())
    val downPayment = req.downPayment.toRequestBody("text/plain".toMediaTypeOrNull())
    val tenure = req.tenure.toRequestBody("text/plain".toMediaTypeOrNull())
    val emiAmount = req.emiAmount.toRequestBody("text/plain".toMediaTypeOrNull())
    val imeiNumber1 = req.imeiNumber1.toRequestBody("text/plain".toMediaTypeOrNull())
    val imeiNumber2 = req.imeiNumber2.toRequestBody("text/plain".toMediaTypeOrNull())
    val accountNumber = req.accountNumber.toRequestBody("text/plain".toMediaTypeOrNull())
    val bankIFSCCode = req.bankIFSCCode.toRequestBody("text/plain".toMediaTypeOrNull())
    val bankName = req.bankName.toRequestBody("text/plain".toMediaTypeOrNull())
    val accountType = req.accountType.toRequestBody("text/plain".toMediaTypeOrNull())
    val branchName = req.branchName.toRequestBody("text/plain".toMediaTypeOrNull())
    val refName = req.refName.toRequestBody("text/plain".toMediaTypeOrNull())
    val refRelationShip = req.refRelationShip.toRequestBody("text/plain".toMediaTypeOrNull())
    val refmobileNo = req.refmobileNo.toRequestBody("text/plain".toMediaTypeOrNull())
    val refAddress = req.refAddress.toRequestBody("text/plain".toMediaTypeOrNull())
    val debitOrCreditCard = req.debitOrCreditCard.toRequestBody("text/plain".toMediaTypeOrNull())
    val upiMandate = req.upiMandate.toRequestBody("text/plain".toMediaTypeOrNull())
    val createdBy = req.createdBy.toRequestBody("text/plain".toMediaTypeOrNull())
    val membershipfees = req.membershipfees.toRequestBody("text/plain".toMediaTypeOrNull())
    val retailercode = req.retailercode.toRequestBody("text/plain".toMediaTypeOrNull())
    val cibilScore = req.cibilScore.toRequestBody("text/plain".toMediaTypeOrNull())
    val isAggrementVerified = req.isAggrementVerified.toRequestBody("text/plain".toMediaTypeOrNull())
    val IsRetailerAggrementVerified = req.IsRetailerAggrementVerified.toRequestBody("text/plain".toMediaTypeOrNull())


    val custPhoto_File = if (req.custPhoto_File != null && req.custPhoto_File.exists()) {
      val requestFile = req.custPhoto_File.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("custPhoto_File", req.custPhoto_File.name, requestFile)
    } else null

    val imeiNumber1_SealPhotoPath = if (req.imeiNumber1_SealPhotoPath != null && req.imeiNumber1_SealPhotoPath.exists()) {
      val requestFile = req.imeiNumber1_SealPhotoPath.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("imeiNumber1_SealPhotoPath", req.imeiNumber1_SealPhotoPath.name, requestFile)
    } else null

    val imeiNumber2_SealPhotoPath = if (req.imeiNumber2_SealPhotoPath != null && req.imeiNumber2_SealPhotoPath.exists()) {
      val requestFile = req.imeiNumber2_SealPhotoPath.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("imeiNumber2_SealPhotoPath", req.imeiNumber2_SealPhotoPath.name, requestFile)
    } else null

    val imeiNumber_PhotoPath = if (req.imeiNumber_PhotoPath != null && req.imeiNumber_PhotoPath.exists()) {
      val requestFile = req.imeiNumber_PhotoPath.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("imeiNumber_PhotoPath", req.imeiNumber_PhotoPath.name, requestFile)
    } else null

    val invoive_Path = if (req.invoive_Path != null && req.invoive_Path.exists()) {
      val requestFile = req.invoive_Path.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("invoive_Path", req.invoive_Path.name, requestFile)
    } else null

    val aadharFront_Path = if (req.aadharFront_Path != null && req.aadharFront_Path.exists()) {
      val requestFile = req.aadharFront_Path.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("aadharFront_Path", req.aadharFront_Path.name, requestFile)
    } else null

    val aadharBack_Path = if (req.aadharBack_Path != null && req.aadharBack_Path.exists()) {
      val requestFile = req.aadharBack_Path.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("aadharBack_Path", req.aadharBack_Path.name, requestFile)
    } else null

    val panFront_Path = if (req.panFront_Path != null && req.panFront_Path.exists()) {
      val requestFile = req.panFront_Path.asRequestBody("image/*".toMediaTypeOrNull())
      MultipartBody.Part.createFormData("panFront_Path", req.panFront_Path.name, requestFile)
    } else null

    return apiInterface.getRegisterKitCustomerReq(
      mode, firstName, middleName, lastName, primaryMobileNumber, primaryOTP, primaryMobileVerified,
      alternateMobileNumber, alternateMobileOTP, pAlternateMobileVerified, eMailID, flatNo, aearSector,
      pinCode, currentAddress, stateName, cityName, country, aadharNumber, aadharNumberVerified,
      panNumber, panNumberVerified, brandName, modelName, modelVariant, color, sellingPrice,
      downPayment, tenure, emiAmount, imeiNumber1, imeiNumber2, accountNumber, bankIFSCCode,
      bankName, accountType, branchName, refName, refRelationShip, refmobileNo, refAddress,
      debitOrCreditCard, upiMandate, createdBy, membershipfees, retailercode, cibilScore,
      isAggrementVerified, IsRetailerAggrementVerified, custPhoto_File,
      imeiNumber1_SealPhotoPath, imeiNumber2_SealPhotoPath, imeiNumber_PhotoPath, invoive_Path,
      aadharFront_Path, aadharBack_Path, panFront_Path
    )
  }

}