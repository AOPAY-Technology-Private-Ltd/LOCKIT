package com.bos.payment.appName.network

import com.bosandroidapp.aopaykit.data.CustomerInstalledAppListResponse
import com.bosandroidapp.aopaykit.data.customeraction.CustomerSideUpdateUnInstallAppRequest
import com.bosandroidapp.aopaykit.data.customeraction.CustomerSideUpdateUnInstallAppResponse
import com.bosandroidapp.aopaykit.data.customeraction.GetKitCustomerLocation
import com.bosandroidapp.aopaykit.data.customeraction.GetKitCustomerLocationResponse
import com.bosandroidapp.aopaykit.data.customeraction.kitinventory.GetKitInventoryListRequest
import com.bosandroidapp.aopaykit.data.customeraction.kitinventory.GetKitInventoryListResponse
import com.bosandroidapp.aopaykit.data.customeraction.GetPendingDeviceActionReq
import com.bosandroidapp.aopaykit.data.customeraction.GetPendingDeviceActionResponse
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSaveDeviceActionRequest
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSaveDeviceActionResponse
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSendNotificationToCustomerReq
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSendNotificationToCustomerResponse
import com.bosandroidapp.aopaykit.data.customeraction.SaveRetailerDeviceTokenRequest
import com.bosandroidapp.aopaykit.data.customeraction.SaveRetailerDeviceTokenResponse
import com.bosandroidapp.aopaykit.data.customeraction.SendInstalledAppOnServerRequest
import com.bosandroidapp.aopaykit.data.customeraction.SendInstalledAppOnServerResponse
import com.bosandroidapp.aopaykit.data.customeraction.UpdateCustomerDeviceActionRequest
import com.bosandroidapp.aopaykit.data.customeraction.UpdateCustomerDeviceActionResponse
import com.bosandroidapp.aopaykit.data.customeraction.UploadCustomerLocationRequest
import com.bosandroidapp.aopaykit.data.customeraction.UploadCustomerLocationResponse
import com.bosandroidapp.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore.CibilScoreReq
import com.bosandroidapp.aopaykit.data.enach.EMandateRequest
import com.bosandroidapp.aopaykit.data.enach.EMandateResponse
import com.bosandroidapp.aopaykit.data.enach.ENachStatusReq
import com.bosandroidapp.aopaykit.data.enach.ENachStatusResp
import com.bosandroidapp.aopaykit.data.enach.EnachDateUploadReq
import com.bosandroidapp.aopaykit.data.enach.EnachDateUploadResp
import com.bosandroidapp.aopaykit.data.loancharge.LoanChargeReq
import com.bosandroidapp.aopaykit.data.loancharge.LoanChargeResp
import com.bosandroidapp.aopaykit.data.model.AddedBankListResp
import com.bosandroidapp.aopaykit.data.model.AdminBankDetailsReq
import com.bosandroidapp.aopaykit.data.model.AdminBankDetailsResp
import com.bosandroidapp.aopaykit.data.model.CustomerEmiStatusReq
import com.bosandroidapp.aopaykit.data.model.CustomerEmiStatusResponse
import com.bosandroidapp.aopaykit.data.model.CustomerlocationUploadReq
import com.bosandroidapp.aopaykit.data.model.CustomerlocationUploadResp
import com.bosandroidapp.aopaykit.data.model.DueOverdueRequest
import com.bosandroidapp.aopaykit.data.model.DueOverdueResponse
import com.bosandroidapp.aopaykit.data.model.GenerateAccessTokenRequest
import com.bosandroidapp.aopaykit.data.model.GenerateAccessTokenResponse
import com.bosandroidapp.aopaykit.data.model.GetRetailerLedgerReq
import com.bosandroidapp.aopaykit.data.model.GetRetailerLedgerResponse
import com.bosandroidapp.aopaykit.data.model.HoldAmountWithdrawReq
import com.bosandroidapp.aopaykit.data.model.HoldAmountWithdrawResp
import com.bosandroidapp.aopaykit.data.model.LowCibilCustomerReportReq
import com.bosandroidapp.aopaykit.data.model.LowCibilCustomerReportResp
import com.bosandroidapp.aopaykit.data.model.MakePaymentAdminReportRequest
import com.bosandroidapp.aopaykit.data.model.MakePaymentAdminReportResponse
import com.bosandroidapp.aopaykit.data.model.MakepaymentResp
import com.bosandroidapp.aopaykit.data.model.RetailerWalletAmountReq
import com.bosandroidapp.aopaykit.data.model.RetailerWalletPayoutAtMakePaymentTimeReq
import com.bosandroidapp.aopaykit.data.model.RetailerWalletPayoutAtMakePaymentTimeResp
import com.bosandroidapp.aopaykit.data.model.RetailerWalletReportReq
import com.bosandroidapp.aopaykit.data.model.RetailerWalletReportResp
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.SessionOutResponse
import com.bosandroidapp.aopaykit.data.model.UploadDeviceInfoReq
import com.bosandroidapp.aopaykit.data.model.UploadDeviceInfoResp
import com.bosandroidapp.aopaykit.data.model.ValidateAccessKeyReq
import com.bosandroidapp.aopaykit.data.model.ValidateAccessKeyResp
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.ValidateSessionResp
import com.bosandroidapp.aopaykit.data.model.VerifyCustomerReq
import com.bosandroidapp.aopaykit.data.model.VerifyCustomerResp
import com.bosandroidapp.aopaykit.data.model.cibilscore.CibilScroeResp
import com.bosandroidapp.aopaykit.data.model.kitoption.ActiveDeviceActionResponse
import com.bosandroidapp.aopaykit.data.model.kitoption.KitCustomerListResponse
import com.bosandroidapp.aopaykit.data.model.kitoption.KitOptionRequest
import com.bosandroidapp.aopaykit.data.model.kitoption.KitOptionResponse
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPlanRequest
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPlanResponse
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPurchaseHistoryRequest
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPurchaseHistoryResponse
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPurchasePlanSaveRequest
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPurchasePlanSaveResponse
import com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerMakePaymentResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.EligibleLoanResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.EmiSplitRes
import com.bosandroidapp.aopaykit.data.model.loginsignup.ForgotPasswordReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetAllMobileDetailsListRes
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetEMISplitDetlailsReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetIsEligibleLoanReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.GetReportsReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LoanCreatedReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LoanCreatedResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.LoginReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.MembershipFeeResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.RegisterCustomerResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.RegistrationRes
import com.bosandroidapp.aopaykit.data.model.loginsignup.RetailerProfileReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.RetailerProfileRespo
import com.bosandroidapp.aopaykit.data.model.loginsignup.RetailerWalletPayoutReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.RetailerWalletPayoutResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.SendOtpReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.SendOtpRes
import com.bosandroidapp.aopaykit.data.model.loginsignup.SmsResponse
import com.bosandroidapp.aopaykit.data.model.loginsignup.VerifyOTPReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.LoanSettlementReportReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.LoanSettlementReportResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.PayoutReportReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.PayoutReportResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.ReportsResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.TransactionHistoryReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.reports.TransactionHistoryResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.AAdhaarDetailesReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.AadhaarDetailsResponse
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.AadharVerificationReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.AadharVerificationResp
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.PanVerificationReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.PanVerificationResponse
import com.bosandroidapp.aopaykit.data.notification.NotificationSendTokenRequest
import com.bosandroidapp.aopaykit.data.notification.NotificationSendTokenResponse
import com.bosandroidapp.aopaykit.data.notification.SendNotificationFeatureNameRequest
import com.bosandroidapp.aopaykit.data.notification.SendNotificationFeatureNameResp
import com.bosandroidapp.aopaykit.data.pennydrop.BankListReq
import com.bosandroidapp.aopaykit.data.pennydrop.BankListResponse
import com.bosandroidapp.aopaykit.data.pennydrop.PennyDropCheckStatusRequest
import com.bosandroidapp.aopaykit.data.pennydrop.PennyDropCheckStatusResponse
import com.bosandroidapp.aopaykit.data.pennydrop.PennyDropRequest
import com.bosandroidapp.aopaykit.data.pennydrop.PennyDropResponse
import com.bosandroidapp.aopaykit.data.pg.PGRequestCall
import com.bosandroidapp.aopaykit.data.pg.PGRequestResponse

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiInterface {

    //pan verification
    @POST("api/AOP/V1/Validation/PanDetails")
    suspend fun getPanVarification(@Body req : PanVerificationReq): Response<PanVerificationResponse>?


    // Adhar verification
    @POST("api/AOP/V1/Validation/AadhaarValidateUrl")
    suspend fun getAadharVarification(@Body req : AadharVerificationReq): Response<AadharVerificationResp>?



    // Adhar details
    @POST("api/AOP/V1/Fetch/Digilocker/TransactionID")
    suspend fun getAadharDetails(@Body req : AAdhaarDetailesReq): Response<AadhaarDetailsResponse>?



    // cibil api for getting cibil score...............

    @POST("api/AOP/CreditAnalytics/Report")
    suspend fun getcibilscore(@Body req : CibilScoreReq): Response<CibilScroeResp>?



    // low cibil score customer report ....................
    @POST("api/V1/AopayFinance/GetCustomerReports")
    suspend fun getLowCibilReports(@Body req : LowCibilCustomerReportReq): Response<LowCibilCustomerReportResp>?



    @Multipart
    @POST("api/V1/AopayFinance/Registration")
    suspend fun registration(
        @Part("FirstName") firstname: RequestBody,
        @Part("LastName") lastname: RequestBody,
        @Part("MobileNumber") mobilenumber: RequestBody,
        @Part("EmailID") mailid: RequestBody,
        @Part("ConfirmPassword") cnfrmpass: RequestBody,
        @Part("Password") pass: RequestBody,
        @Part("Address") address: RequestBody,
        @Part("AadharNumber") aadhaarno: RequestBody,
        @Part("PanNumber") pannumber: RequestBody,
        @Part("StoreName") storename: RequestBody,
        @Part("StoreAddress") storeaddress: RequestBody,
        @Part profilePhoto: MultipartBody.Part,
        @Part aadhaarfront: MultipartBody.Part,
        @Part aadhaarback: MultipartBody.Part,
        @Part pancardfront: MultipartBody.Part,
        @Part cancelcheque: MultipartBody.Part,
        @Part storefront: MultipartBody.Part,
        @Part companydoc: MultipartBody.Part
        ):Response<RegistrationRes>



    @POST("api/V1/AopayFinance/Login")
    suspend fun login(@Body req: LoginReq): Response<RegistrationRes>?


    @POST("api/V1/AopayFinance/Logout")
    suspend fun logout(@Body req: LogoutReq): Response<LogoutResp>?


    @POST("api/V1/AopayFinance/ValidateSession")
    suspend fun sessionExpired(@Body req: ValidateSessionRequest): Response<ValidateSessionResp>?


    @POST("api/V1/AopayFinance/SendOTP")
    suspend fun sendOTP(@Body req: SendOtpReq): Response<SendOtpRes>?


    @POST("api/V1/AopayFinance/VerifyOTP")
    suspend fun verifyOTP(@Body req: VerifyOTPReq): Response<SendOtpRes>?


    @POST("api/V1/AopayFinance/ForgotPassword")
    suspend fun forgotPassword(@Body req: ForgotPasswordReq): Response<SendOtpRes>?


    @POST("api/V1/AopayFinance/GetAllDeviceDetails")
    suspend fun getAllDeviceDetails(): Response<GetAllMobileDetailsListRes>?


    @POST("api/V1/AopayFinance/GetModelWiseLoanDetails")
    suspend fun getEmiSplitDataDetails(@Body req : GetEMISplitDetlailsReq): Response<EmiSplitRes>?



    //  customer mobile verification api
    @POST("api/V1/AopayFinance/KitVerifyCustomer")
    suspend fun verifycustomerKitReq(@Body req : VerifyCustomerReq): Response<VerifyCustomerResp>?


    @POST("api/V1/AopayFinance/VerifyCustomer")
    suspend fun verifycustomerReq(@Body req : VerifyCustomerReq): Response<VerifyCustomerResp>?



    //  customer mobile verification api
    @POST("api/V1/AopayFinance/KitVerifyCustomer")
    suspend fun verifyKitcustomerReq(@Body req : VerifyCustomerReq): Response<VerifyCustomerResp>?


    @POST("api/V1/AopayFinance/ManageLoan")
    suspend fun getLoanCreatedByRetailer(@Body req : LoanCreatedReq): Response<LoanCreatedResp>?


    @Multipart
    @POST("api/V1/AopayFinance/ManageCustByCredit")
    suspend fun getCustomerCibilApprovedReq(
        @Part("Mode") mode: RequestBody,
        @Part("FirstName") firstName: RequestBody,
        @Part("MiddleName") middleName: RequestBody,
        @Part("LastName") lastName: RequestBody,
        @Part("PrimaryMobileNumber") primaryMobileNumber: RequestBody,
        @Part("PrimaryOTP") primaryOTP: RequestBody,
        @Part("PrimaryMobileVerified") primaryMobileVerified: RequestBody,
        @Part("AlternateMobileNumber") alternateMobileNumber: RequestBody,
        @Part("AlternateMobileOTP") alternateMobileOTP: RequestBody,
        @Part("PAlternateMobileVerified") pAlternateMobileVerified: RequestBody,
        @Part("EMailID") eMailID: RequestBody,
        @Part("FlatNo") flatNo: RequestBody,
        @Part("AearSector") aearSector: RequestBody,
        @Part("PinCode") pinCode: RequestBody,
        @Part("CurrentAddress") currentAddress: RequestBody,
        @Part("StateName") stateName: RequestBody,
        @Part("CityName") cityName: RequestBody,
        @Part("Country") country: RequestBody,
        @Part("AadharNumber") aadharNumber: RequestBody,
        @Part("AadharNumberVerified") aadharNumberVerified: RequestBody,
        @Part("PANNumber") panNumber: RequestBody,
        @Part("PANNumberVerified") panNumberVerified: RequestBody,
        @Part("BrandName") brandName: RequestBody,
        @Part("ModelName") modelName: RequestBody,
        @Part("ModelVariant") modelVariant: RequestBody,
        @Part("Color") color: RequestBody,
        @Part("SellingPrice") sellingPrice: RequestBody,
        @Part("DownPayment") downPayment: RequestBody,
        @Part("Tenure") tenure: RequestBody,
        @Part("EMIAmount") emiAmount: RequestBody,
        @Part("IMEINumber1") imeiNumber1: RequestBody,
        @Part("IMEINumber2") imeiNumber2: RequestBody,
        @Part("AccountNumber") accountNumber: RequestBody,
        @Part("BankIFSCCode") bankIFSCCode: RequestBody,
        @Part("BankName") bankName: RequestBody,
        @Part("AccountType") accountType: RequestBody,
        @Part("BranchName") branchName: RequestBody,
        @Part("RefName") refName: RequestBody,
        @Part("RefRelationShip") refRelationShip: RequestBody,
        @Part("RefmobileNo") refmobileNo: RequestBody,
        @Part("RefAddress") refAddress: RequestBody,
        @Part("DebitOrCreditCard") debitOrCreditCard: RequestBody,
        @Part("UPIMandate") upiMandate: RequestBody,
        @Part("CreatedBy") createdBy: RequestBody,
        @Part("MemberShipFees") membershipfees: RequestBody,
        @Part("PanApiResponse") PanApiResponse: RequestBody,
        @Part("AadhaarApiResponse") AadhaarApiResponse: RequestBody,
        @Part("CibilApiResponse") CibilApiResponse: RequestBody,
        @Part("CustomerCodes") CustomerCodes: RequestBody,
        @Part("RetailerCode") retailercode: RequestBody,
        @Part("CibilScore") cibilScore: RequestBody,
        @Part("IsAggrementVerified") isAggrementVerified: RequestBody,
        @Part("IsRetailerAggrementVerified") IsRetailerAggrementVerified: RequestBody,
        @Part custPhoto_File: MultipartBody.Part?,
        @Part imeiNumber1_SealPhotoPath: MultipartBody.Part?,
        @Part imeiNumber2_SealPhotoPath: MultipartBody.Part?,
        @Part imeiNumber_PhotoPath: MultipartBody.Part?,
        @Part invoive_Path: MultipartBody.Part?,
        @Part aadharFront_Path: MultipartBody.Part?,
        @Part aadharBack_Path: MultipartBody.Part?,
        @Part panFront_Path: MultipartBody.Part?
        ): Response<RegisterCustomerResp>


    @Multipart
    @POST("api/V1/AopayFinance/ManageCustomer")
    suspend fun getRegisterCustomerReq(
        @Part("Mode") mode: RequestBody,
        @Part("FirstName") firstName: RequestBody,
        @Part("MiddleName") middleName: RequestBody,
        @Part("LastName") lastName: RequestBody,
        @Part("PrimaryMobileNumber") primaryMobileNumber: RequestBody,
        @Part("PrimaryOTP") primaryOTP: RequestBody,
        @Part("PrimaryMobileVerified") primaryMobileVerified: RequestBody,
        @Part("AlternateMobileNumber") alternateMobileNumber: RequestBody,
        @Part("AlternateMobileOTP") alternateMobileOTP: RequestBody,
        @Part("PAlternateMobileVerified") pAlternateMobileVerified: RequestBody,
        @Part("EMailID") eMailID: RequestBody,
        @Part("FlatNo") flatNo: RequestBody,
        @Part("AearSector") aearSector: RequestBody,
        @Part("PinCode") pinCode: RequestBody,
        @Part("CurrentAddress") currentAddress: RequestBody,
        @Part("StateName") stateName: RequestBody,
        @Part("CityName") cityName: RequestBody,
        @Part("Country") country: RequestBody,
        @Part("AadharNumber") aadharNumber: RequestBody,
        @Part("AadharNumberVerified") aadharNumberVerified: RequestBody,
        @Part("PANNumber") panNumber: RequestBody,
        @Part("PANNumberVerified") panNumberVerified: RequestBody,
        @Part("BrandName") brandName: RequestBody,
        @Part("ModelName") modelName: RequestBody,
        @Part("ModelVariant") modelVariant: RequestBody,
        @Part("Color") color: RequestBody,
        @Part("SellingPrice") sellingPrice: RequestBody,
        @Part("DownPayment") downPayment: RequestBody,
        @Part("Tenure") tenure: RequestBody,
        @Part("EMIAmount") emiAmount: RequestBody,
        @Part("IMEINumber1") imeiNumber1: RequestBody,
        @Part("IMEINumber2") imeiNumber2: RequestBody,
        @Part("AccountNumber") accountNumber: RequestBody,
        @Part("BankIFSCCode") bankIFSCCode: RequestBody,
        @Part("BankName") bankName: RequestBody,
        @Part("AccountType") accountType: RequestBody,
        @Part("BranchName") branchName: RequestBody,
        @Part("RefName") refName: RequestBody,
        @Part("RefRelationShip") refRelationShip: RequestBody,
        @Part("RefmobileNo") refmobileNo: RequestBody,
        @Part("RefAddress") refAddress: RequestBody,
        @Part("DebitOrCreditCard") debitOrCreditCard: RequestBody,
        @Part("UPIMandate") upiMandate: RequestBody,
        @Part("CreatedBy") createdBy: RequestBody,
        @Part("MemberShipFees") membershipfees: RequestBody,
        @Part("RetailerCode") retailercode: RequestBody,
        @Part("CibilScore") cibilScore: RequestBody,
        @Part("IsAggrementVerified") isAggrementVerified: RequestBody,
        @Part("IsRetailerAggrementVerified") IsRetailerAggrementVerified: RequestBody,
        @Part custPhoto_File: MultipartBody.Part?, // File here
        @Part imeiNumber1_SealPhotoPath: MultipartBody.Part?,
        @Part imeiNumber2_SealPhotoPath: MultipartBody.Part?,
        @Part imeiNumber_PhotoPath: MultipartBody.Part?,
        @Part invoive_Path: MultipartBody.Part?,
        @Part aadharFront_Path: MultipartBody.Part?,
        @Part aadharBack_Path: MultipartBody.Part?,
        @Part panFront_Path: MultipartBody.Part?
    ): Response<RegisterCustomerResp>



    @Multipart
    @POST("api/V1/AopayFinance/ManageCustomer")
    suspend fun getRegisterOnlineCustomerReq(
        @Part("Mode") mode: RequestBody,
        @Part("FirstName") firstName: RequestBody,
        @Part("MiddleName") middleName: RequestBody,
        @Part("LastName") lastName: RequestBody,
        @Part("PrimaryMobileNumber") primaryMobileNumber: RequestBody,
        @Part("PrimaryOTP") primaryOTP: RequestBody,
        @Part("PrimaryMobileVerified") primaryMobileVerified: RequestBody,
        @Part("AlternateMobileNumber") alternateMobileNumber: RequestBody,
        @Part("AlternateMobileOTP") alternateMobileOTP: RequestBody,
        @Part("PAlternateMobileVerified") pAlternateMobileVerified: RequestBody,
        @Part("EMailID") eMailID: RequestBody,
        @Part("FlatNo") flatNo: RequestBody,
        @Part("AearSector") aearSector: RequestBody,
        @Part("PinCode") pinCode: RequestBody,
        @Part("CurrentAddress") currentAddress: RequestBody,
        @Part("StateName") stateName: RequestBody,
        @Part("CityName") cityName: RequestBody,
        @Part("Country") country: RequestBody,
        @Part("AadharNumber") aadharNumber: RequestBody,
        @Part("AadharNumberVerified") aadharNumberVerified: RequestBody,
        @Part("PANNumber") panNumber: RequestBody,
        @Part("PANNumberVerified") panNumberVerified: RequestBody,
        @Part("BrandName") brandName: RequestBody,
        @Part("ModelName") modelName: RequestBody,
        @Part("ModelVariant") modelVariant: RequestBody,
        @Part("Color") color: RequestBody,
        @Part("SellingPrice") sellingPrice: RequestBody,
        @Part("DownPayment") downPayment: RequestBody,
        @Part("Tenure") tenure: RequestBody,
        @Part("EMIAmount") emiAmount: RequestBody,
        @Part("IMEINumber1") imeiNumber1: RequestBody,
        @Part("IMEINumber2") imeiNumber2: RequestBody,
        @Part("AccountNumber") accountNumber: RequestBody,
        @Part("BankIFSCCode") bankIFSCCode: RequestBody,
        @Part("BankName") bankName: RequestBody,
        @Part("AccountType") accountType: RequestBody,
        @Part("BranchName") branchName: RequestBody,
        @Part("RefName") refName: RequestBody,
        @Part("RefRelationShip") refRelationShip: RequestBody,
        @Part("RefmobileNo") refmobileNo: RequestBody,
        @Part("RefAddress") refAddress: RequestBody,
        @Part("DebitOrCreditCard") debitOrCreditCard: RequestBody,
        @Part("UPIMandate") upiMandate: RequestBody,
        @Part("CreatedBy") createdBy: RequestBody,
        @Part("MemberShipFees") membershipfees: RequestBody,
        @Part("RetailerCode") retailercode: RequestBody,
        @Part("PanApiResponse") PanApiResponse: RequestBody,
        @Part("AadhaarApiResponse") AadhaarApiResponse: RequestBody,
        @Part("CibilApiResponse") CibilApiResponse: RequestBody,
        @Part("CibilScore") cibilScore: RequestBody,
        @Part("IsAggrementVerified") isAggrementVerified: RequestBody,
        @Part("IsRetailerAggrementVerified") IsRetailerAggrementVerified: RequestBody,
        @Part("IsrefKycVerified") IsrefKYCVerified: RequestBody,
        @Part("refAdhaarNumber") refAdhaarNumber: RequestBody,
        @Part custPhoto_File: MultipartBody.Part?, // File here
        @Part imeiNumber1_SealPhotoPath: MultipartBody.Part?,
        @Part imeiNumber2_SealPhotoPath: MultipartBody.Part?,
        @Part imeiNumber_PhotoPath: MultipartBody.Part?,
        @Part invoive_Path: MultipartBody.Part?,
        ): Response<RegisterCustomerResp>


    @Multipart
    @POST("api/V1/AopayFinance/KitManageCustomer")
    suspend fun getRegisterKitCustomerReq(
        @Part("Mode") mode: RequestBody,
        @Part("FirstName") firstName: RequestBody,
        @Part("MiddleName") middleName: RequestBody,
        @Part("LastName") lastName: RequestBody,
        @Part("PrimaryMobileNumber") primaryMobileNumber: RequestBody,
        @Part("PrimaryOTP") primaryOTP: RequestBody,
        @Part("PrimaryMobileVerified") primaryMobileVerified: RequestBody,
        @Part("AlternateMobileNumber") alternateMobileNumber: RequestBody,
        @Part("AlternateMobileOTP") alternateMobileOTP: RequestBody,
        @Part("PAlternateMobileVerified") pAlternateMobileVerified: RequestBody,
        @Part("EMailID") eMailID: RequestBody,
        @Part("FlatNo") flatNo: RequestBody,
        @Part("AearSector") aearSector: RequestBody,
        @Part("PinCode") pinCode: RequestBody,
        @Part("CurrentAddress") currentAddress: RequestBody,
        @Part("StateName") stateName: RequestBody,
        @Part("CityName") cityName: RequestBody,
        @Part("Country") country: RequestBody,
        @Part("AadharNumber") aadharNumber: RequestBody,
        @Part("AadharNumberVerified") aadharNumberVerified: RequestBody,
        @Part("PANNumber") panNumber: RequestBody,
        @Part("PANNumberVerified") panNumberVerified: RequestBody,
        @Part("BrandName") brandName: RequestBody,
        @Part("ModelName") modelName: RequestBody,
        @Part("ModelVariant") modelVariant: RequestBody,
        @Part("Color") color: RequestBody,
        @Part("SellingPrice") sellingPrice: RequestBody,
        @Part("DownPayment") downPayment: RequestBody,
        @Part("Tenure") tenure: RequestBody,
        @Part("EMIAmount") emiAmount: RequestBody,
        @Part("IMEINumber1") imeiNumber1: RequestBody,
        @Part("IMEINumber2") imeiNumber2: RequestBody,
        @Part("AccountNumber") accountNumber: RequestBody,
        @Part("BankIFSCCode") bankIFSCCode: RequestBody,
        @Part("BankName") bankName: RequestBody,
        @Part("AccountType") accountType: RequestBody,
        @Part("BranchName") branchName: RequestBody,
        @Part("RefName") refName: RequestBody,
        @Part("RefRelationShip") refRelationShip: RequestBody,
        @Part("RefmobileNo") refmobileNo: RequestBody,
        @Part("RefAddress") refAddress: RequestBody,
        @Part("DebitOrCreditCard") debitOrCreditCard: RequestBody,
        @Part("UPIMandate") upiMandate: RequestBody,
        @Part("CreatedBy") createdBy: RequestBody,
        @Part("MemberShipFees") membershipfees: RequestBody,
        @Part("RetailerCode") retailercode: RequestBody,
        @Part("clientcode") clientcode: RequestBody,
        @Part("CibilScore") cibilScore: RequestBody,
        @Part("IsAggrementVerified") isAggrementVerified: RequestBody,
        @Part("IsRetailerAggrementVerified") IsRetailerAggrementVerified: RequestBody,
        @Part custPhoto_File: MultipartBody.Part?, // File here
        @Part imeiNumber1_SealPhotoPath: MultipartBody.Part?,
        @Part imeiNumber2_SealPhotoPath: MultipartBody.Part?,
        @Part imeiNumber_PhotoPath: MultipartBody.Part?,
        @Part invoive_Path: MultipartBody.Part?,
        @Part aadharFront_Path: MultipartBody.Part?,
        @Part aadharBack_Path: MultipartBody.Part?,
        @Part panFront_Path: MultipartBody.Part?
    ): Response<KitCustomerListResponse>



    // kit customer list
    @POST("api/V1/AopayFinance/GetUpdateProfile")
    suspend fun getRetailerProfileGetUpdateReq(@Body req : RetailerProfileReq): Response<RetailerProfileRespo>?



    @POST("api/V1/AopayFinance/GetDealerHoldWalletLedger")
    suspend fun getRetailerLedgerReq(@Body req : GetRetailerLedgerReq): Response<GetRetailerLedgerResponse>?


    // retailer wallet payout........................
    @POST("api/V1/AopayFinance/ManagePayout")
    suspend fun getRetailerWalletPayoutReq(@Body req : RetailerWalletPayoutReq): Response<RetailerWalletPayoutResp>?


    // retailer loan settlement report
    @POST("api/V1/AopayFinance/GetDisbursedLoanSettlement")
    suspend fun loanSettlementReportReq(@Body req : LoanSettlementReportReq): Response<LoanSettlementReportResp>?


    @POST("api/V1/AopayFinance/WalletBalance")
    suspend fun getRetailerWalletAmountReq(@Body req : RetailerWalletAmountReq): Response<com.bosandroidapp.aopaykit.data.model.RetailerWalletResponse>?


    // retailer payout report
    @POST("api/V1/AopayFinance/GetPayoutTransferDetails")
    suspend fun getPayoutReportReq(@Body req : PayoutReportReq): Response<PayoutReportResp>?


    @POST("api/V1/AopayFinance/GetLookupReports")
    suspend fun getRetailerWalletReport(@Body req : RetailerWalletReportReq): Response<RetailerWalletReportResp>?


    // api for both addbank and get bank list.............................................................................
    @POST("api/V1/AopayFinance/RetailerBankAccountManage")
    suspend fun addBankAccounts(@Body req : com.bosandroidapp.aopaykit.data.model.AddBankAccountReq): Response<AddedBankListResp>?


    // api for hold amount request .............................................................................
    @POST("api/V1/AopayFinance/ManageHoldingAmount")
    suspend fun requestHoldAmountWithdrawRequest(@Body req : HoldAmountWithdrawReq): Response<HoldAmountWithdrawResp>?


    // get due overdue customer data......................
    @POST("api/V1/AopayFinance/getloancardfulldetails")
    suspend fun dueoverdueCustomerRequest(@Body req : DueOverdueRequest): Response<DueOverdueResponse>?


    // for customer.....................................................
    @POST("api/V1/AopayFinance/GetLoanDetailsCustomerWise") // for view retailer
    suspend fun getCustomerLoanDetailsList(@Body req : GetCustomerLoanDetailsReq): Response<com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerLoanEmiResp>?


    // retailer trasaction history...................................................
    @POST("api/V1/AopayFinance/GetWalletCreditDebitHistory")
    suspend fun getTransactionHistoryList(@Body req : TransactionHistoryReq): Response<TransactionHistoryResp>?


    @Multipart
    @POST("api/V1/AopayFinance/LoanEMIReceiving")
    suspend fun getcustomerLoanEmiReceive(
        @Part("Mode") mode: RequestBody,
        @Part("LoanCode") loancode: RequestBody,
        @Part("PaymentDate") paymentDate: RequestBody,
        @Part("PaymentMode") paymentMode: RequestBody,
        @Part("TransactionNo") utrNumber: RequestBody,
        @Part("Reason") remarks: RequestBody,
        @Part("UpdatedBy") createdBy: RequestBody,
        @Part("Customercode") customerCode: RequestBody,
        @Part("RetailerCode") retailerCode: RequestBody,
        @Part("BankName") bankName: RequestBody,
        @Part("ReceiptImagePath") receiptImagePath: RequestBody,
        @Part receiptImage: MultipartBody.Part
    ): Response<CustomerMakePaymentResp>



    @POST("api/V1/AopayFinance/TransferAmtToDealer")
    suspend fun RetailerWalletPayoutReq(@Body req : RetailerWalletPayoutAtMakePaymentTimeReq): Response<RetailerWalletPayoutAtMakePaymentTimeResp>?



    @Multipart
    @POST("api/V1/AopayFinance/MakePayment")
    suspend fun getCustomerReceiptUpload(
        @Part("CustomerCode") customercode: RequestBody,
        @Part("LoanCode") loancode: RequestBody,
        @Part("PaidAmount") paidamount: RequestBody,
        @Part("ReceiptImage_Path") path: RequestBody,
        @Part("CreatedBy") createBy: RequestBody,
        @Part("CreatedAt") createat: RequestBody,
        @Part("ActiveStatus") activestatus: RequestBody,
        @Part("RecordStatus") recordstatus: RequestBody,
        @Part("PaidEMINo") paidemino: RequestBody,
        @Part("TxnNumber") txnnumber: RequestBody,
        @Part("Remarks") remarks: RequestBody,
        @Part receiptImage: MultipartBody.Part
        ): Response<CustomerMakePaymentResp>




    // sms api implemented ...............................................
    @POST("vb/apikey.php")
    suspend fun sendSMSForVerifyMob(@Query("apikey") apikey : String,
                                    @Query("senderid") senderid : String,
                                    @Query("templateid") templateid : String,
                                    @Query("number") mobnumber : String,
                                    @Query("message") message : String): Response<SmsResponse>?




    // for customer and retailer both showing reports
    @POST("api/V1/AopayFinance/GetLoanDetailsRetailerWise")
    suspend fun getReports(@Body req : GetReportsReq): Response<ReportsResp>?




    // revalidate user eligible for loan or not
    @POST("api/V1/AopayFinance/IsLoanReapplyEligible")
    suspend fun getEligiblereq(@Body req : GetIsEligibleLoanReq): Response<EligibleLoanResp>?




    // revalidate user eligible for loan or not
    @POST("api/V1/AopayFinance/GetMembershipFee")
    suspend fun getMemberShipReq(@Body req : GetIsEligibleLoanReq): Response<MembershipFeeResp>?



    // get customer location......................
    @POST("api/V1/AopayFinance/managecustomerlocation")
    suspend fun uploadcustomerlocation(@Body req : CustomerlocationUploadReq): Response<CustomerlocationUploadResp>?



    // link for download apk file
    @GET("api/V1/AopayFinance/generate-download-lockit-qr")
    suspend fun getApkUrlLink(): Response<ResponseBody>?



    // for customer generate token key
    @POST("api/V1/AopayFinance/generatekey")
    suspend fun getAccessKeyForValidateAPKReq(@Body req : GenerateAccessTokenRequest): Response<GenerateAccessTokenResponse>?



    //  key validate retailer end
    @POST("api/V1/AopayFinance/validatekey")
    suspend fun validateTokenFromRetailerReq(@Body req : ValidateAccessKeyReq): Response<ValidateAccessKeyResp>?


    //  retailer sessionout api
    @POST("api/V1/AopayFinance/RetailerStatusManage")
    suspend fun sessionOutReq(@Body req : SessionOutReq): Response<SessionOutResponse>?


    // PennyDrop api for cheking bank details
    @POST("api/AOP/V1/PennyDrop/Request")
    suspend fun pennyDropReq(@Body req : PennyDropRequest): Response<PennyDropResponse>?


    @POST("api/AOP/V1/PennyDrop/CheckStatus")
    suspend fun pennyDropStatus(@Body req : PennyDropCheckStatusRequest): Response<PennyDropCheckStatusResponse>?


    @POST("api/AOP/Enach/V1/GetBankList")
    suspend fun getBankListRequest(@Body req: BankListReq): Response<BankListResponse>?


    @POST("api/AOP/Enach/V1/eMandate")
    suspend fun geteMandateRequest(@Body req: EMandateRequest): Response<EMandateResponse>?


    @POST("api/AOP/Enach/V1/eMandate/getStatus")
    suspend fun geteMandateSatusRequest(@Body req: ENachStatusReq): Response<ENachStatusResp>?


    // loan charge for each loan retailer
    @POST("api/Customer/LoanApplyCharges")
    suspend fun loanApplyChargesReq(@Body req: LoanChargeReq): Response<LoanChargeResp>?

    // customer device info

    @POST("api/V1/AopayFinance/GetDeviceInformation")
    suspend fun uploadDeviceInfo(@Body req : UploadDeviceInfoReq): Response<UploadDeviceInfoResp>?


    // customer device info

    @POST("api/V1/AopayFinance/UpdateEmandateDetails")
    suspend fun UpdateEmandateDetails(@Body req : EnachDateUploadReq): Response<EnachDateUploadResp>?



    @POST("api/notification/send")
    suspend fun sendNotificationFeatureNameReq(@Body req : SendNotificationFeatureNameRequest): Response<SendNotificationFeatureNameResp>?


    @POST("api/V1/AopayFinance/GetLoanEmIScheduleWithStatus")
    suspend fun LoanEmIScheduleWithStatusReq(@Body req : CustomerEmiStatusReq): Response<CustomerEmiStatusResponse>?


    @POST("api/V1/AopayFinance/GetAdminBankDetails")
    suspend fun GetAdminBankDetailsReq(@Body req : AdminBankDetailsReq): Response<AdminBankDetailsResp>?


    @Multipart
    @POST("api/V1/AopayFinance/RetailerMakePayment")
    suspend fun uploadDocumentForRaisAmountTransferAdmin(
        @Part("RetailerCode") RetailerCode: RequestBody,
        @Part("RequestAmount") RequestAmount: RequestBody,
        @Part("PaymentMode") PaymentMode: RequestBody,
        @Part("BankName") BankName: RequestBody,
        @Part("AccountHolderName") AccountHolderName: RequestBody,
        @Part("AccountNumber") AccountNumber: RequestBody,
        @Part("IFSCCode") IFSCCode: RequestBody,
        @Part("UTRNumber") UTRNumber: RequestBody,
        @Part("UPIID") UPIID: RequestBody,
        @Part("Remarks") Remarks: RequestBody,
        @Part("ApprovedRemarks") ApprovedRemarks: RequestBody,
        @Part("CreatedBy") CreatedBy: RequestBody,
        @Part("RecordStatus") RecordStatus: RequestBody,
        @Part("ActiveStatus") ActiveStatus: RequestBody,
        @Part imageFile1: MultipartBody.Part
    ): Response<MakepaymentResp>



    @POST("api/AOPay/Finance/Offline/V1/PaymentGateway")
    suspend fun callPG(@Body req : PGRequestCall) : Response<PGRequestResponse>?



    @POST("api/AOPay/Finance/LockKit/V1/PaymentGateway")
    suspend fun kitCallPG(@Body req : PGRequestCall) : Response<PGRequestResponse>?


    @POST("api/V1/AopayFinance/GetRetailerMakePaymentList")
    suspend fun getMakePaymentReportReq(@Body req : MakePaymentAdminReportRequest) : Response<MakePaymentAdminReportResponse>?


    @POST("api/V1/AopayFinance/GetRetailerKitPlans")
    suspend fun kitPlanTopUpRequest(@Body req : KitPlanRequest) : Response<KitPlanResponse>?


    //retailer kit option
    @POST("api/V1/AopayFinance/GetRetailerLoanModeDetails")
    suspend fun getRequestKitOption(@Body req : KitOptionRequest) : Response<KitOptionResponse>?

    @POST("api/V1/AopayFinance/GetCustomerLatestLocationKit")
    suspend fun getKitCustomerLocation(@Body req : GetKitCustomerLocation) : Response<GetKitCustomerLocationResponse>?


    // customer action related app via notification

    @POST("api/notification/SaveDeviceAction")
    suspend fun getRetailerDeviceActionToCustomerRequest(@Body req : RetailerSaveDeviceActionRequest) : Response<RetailerSaveDeviceActionResponse>?


    @POST("api/notification/SendDeviceNotification")
    suspend fun sendRetailerNotificationToCustomerRequest(@Body req : RetailerSendNotificationToCustomerReq) : Response<RetailerSendNotificationToCustomerResponse>?


    @POST("api/notification/GetSuccessDeviceActions")
    suspend fun getActiveDeviceActionRequest(@Body req : GetPendingDeviceActionReq) : Response<ActiveDeviceActionResponse>?


    @POST("api/notification/GetPendingDeviceActions")
    suspend fun getPendingDeviceActionRequest(@Body req : GetPendingDeviceActionReq) : Response<GetPendingDeviceActionResponse>?


    //hit api for customer
    @POST("api/notification/save-token")
    suspend fun sendTokenViaNotificationReq(@Body req : NotificationSendTokenRequest): Response<NotificationSendTokenResponse>?


    @POST("api/notification/UpdateDeviceActionStatus")
    suspend fun updateActionFromCustomerDevice(@Body req : UpdateCustomerDeviceActionRequest) : Response<UpdateCustomerDeviceActionResponse>?



    @POST("api/V1/AopayFinance/SaveCustomerLocationKit")
    suspend fun uploadKitCustomerLocationRequest(@Body req : UploadCustomerLocationRequest) : Response<UploadCustomerLocationResponse>?



    @POST("api/V1/AopayFinance/GetPurchaseHistory")
    suspend fun getPurchaseHistoryRequest(@Body req : KitPurchaseHistoryRequest) : Response<KitPurchaseHistoryResponse>?



    @POST("api/V1/AopayFinance/SavePurchaseHistory")
    suspend fun savePurchaseKitPlan(@Body req : KitPurchasePlanSaveRequest) : Response<KitPurchasePlanSaveResponse>?


    @POST("api/notification/SaveAppMaster")
    suspend fun uploadCustomerDeviceInsatlledAppsOnServerRequest(@Body req : SendInstalledAppOnServerRequest) : Response<SendInstalledAppOnServerResponse>?



    @POST("api/V1/AopayFinance/GetKitInventory")
    suspend fun getkitInventoryListRequest(@Body req : GetKitInventoryListRequest) : Response<GetKitInventoryListResponse>?



    @GET("api/notification/GetAppMaster")
    suspend fun getKitCustomerInstalledAppRequest(@Query("CreatedBy") createdBy: String) : Response<CustomerInstalledAppListResponse>?



    @POST("api/notification/SaveRetailerDeviceToken")
    suspend fun saveRetailerDeviceTokenRequest(@Body req : SaveRetailerDeviceTokenRequest) : Response<SaveRetailerDeviceTokenResponse>?


    
    // for customer side update api for uninstall option

    @POST("api/notification/CustomerAppUninstalled")
    suspend fun updateAppUninstallStatusReq(@Body req : CustomerSideUpdateUnInstallAppRequest) : Response<CustomerSideUpdateUnInstallAppResponse>?



}