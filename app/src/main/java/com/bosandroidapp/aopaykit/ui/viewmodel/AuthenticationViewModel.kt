package com.bosandroidapp.aopaykit.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bosandroidapp.aopaykit.data.customeraction.GetKitCustomerLocation
import com.bosandroidapp.aopaykit.data.customeraction.kitinventory.GetKitInventoryListRequest
import com.bosandroidapp.aopaykit.data.customeraction.GetPendingDeviceActionReq
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSaveDeviceActionRequest
import com.bosandroidapp.aopaykit.data.customeraction.RetailerSendNotificationToCustomerReq
import com.bosandroidapp.aopaykit.data.customeraction.SaveRetailerDeviceTokenRequest
import com.bosandroidapp.aopaykit.data.customeraction.SendInstalledAppOnServerRequest
import com.bosandroidapp.aopaykit.data.customeraction.UpdateCustomerDeviceActionRequest
import com.bosandroidapp.aopaykit.data.enach.EnachDateUploadReq
import com.bosandroidapp.aopaykit.data.model.AdminBankDetailsReq
import com.bosandroidapp.aopaykit.data.model.CustomerEmiStatusReq
import com.bosandroidapp.aopaykit.data.model.CustomerKitRequest
import com.bosandroidapp.aopaykit.data.model.DueOverdueRequest
import com.bosandroidapp.aopaykit.data.model.GenerateAccessTokenRequest
import com.bosandroidapp.aopaykit.data.model.GetRetailerLedgerReq
import com.bosandroidapp.aopaykit.data.model.Getproductclientreq
import com.bosandroidapp.aopaykit.data.model.HoldAmountWithdrawReq
import com.bosandroidapp.aopaykit.data.model.LowCibilCustomerReportReq
import com.bosandroidapp.aopaykit.data.model.MakePaymentAdminReportRequest
import com.bosandroidapp.aopaykit.data.model.RaiseMakePaymentReq
import com.bosandroidapp.aopaykit.data.model.RetailerWalletAmountReq
import com.bosandroidapp.aopaykit.data.model.RetailerWalletPayoutAtMakePaymentTimeReq
import com.bosandroidapp.aopaykit.data.model.RetailerWalletReportReq
import com.bosandroidapp.aopaykit.data.model.SessionOutReq
import com.bosandroidapp.aopaykit.data.model.UploadDeviceInfoReq
import com.bosandroidapp.aopaykit.data.model.ValidateAccessKeyReq
import com.bosandroidapp.aopaykit.data.model.ValidateSessionRequest
import com.bosandroidapp.aopaykit.data.model.VerifyCustomerReq
import com.bosandroidapp.aopaykit.data.model.kitoption.KitOptionRequest
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPlanRequest
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPurchaseHistoryRequest
import com.bosandroidapp.aopaykit.data.model.kitplan.KitPurchasePlanSaveRequest
import com.bosandroidapp.aopaykit.data.model.loginsignup.CustomerLoanEmiReceiveReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.ForgotPasswordReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetEMISplitDetlailsReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.GetIsEligibleLoanReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LoanCreatedReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LoginReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.LogoutReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.RegistrationReq
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
import com.bosandroidapp.aopaykit.data.repository.AuthRepository
import com.bosandroidapp.aopaykit.utils.ApiResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException

class AuthenticationViewModel (private val repository: AuthRepository):ViewModel(){

    fun getRegistration(req: RegistrationReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getregistration(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getLogin(req: LoginReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            val response = repository.getlogin(req)
            emit(ApiResponse.success(data = response))

        } catch (e: HttpException) {
            val errorCode = e.code()
            val errorMessage = when (errorCode) {
                400 -> "Bad Request"
                401 -> "Unauthorized"
                403 -> "Forbidden"
                404 -> "Resource not found"
                500 -> "Internal Server Error"
                else -> "Something went wrong! Error Code: $errorCode"
            }
            emit(ApiResponse.error(data = null, message = errorMessage))

        } catch (e: IOException) {
            emit(ApiResponse.error(data = null, message = "Network error! Please check your internet connection."))

        } catch (e: Exception) {
            emit(ApiResponse.error(data = null, message = e.message ?: "Unknown error occurred"))
        }
    }


    fun getLogout(req: LogoutReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getlogout(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun sendOTPReq(req: SendOtpReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.sendOTP(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun verifyOTPReq(req: VerifyOTPReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.verifyOTPReq(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun forgotPasswordReq(req: ForgotPasswordReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.forgotPassword(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getMobileList(req: Getproductclientreq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getMobileList(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getSplitEmiDetails(req: GetEMISplitDetlailsReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getEmiSplitData(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getRetailerLoanCreatedReq(req: LoanCreatedReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            val response = repository.getRetailerLoanCreatedReq(req)

            if (response!!.isSuccessful) {
                emit(ApiResponse.success(data = response))
            }
            else {
                // ❌ Handle API error (like 500)
                val errorBody = response.errorBody()?.string()
                val code = response.code()
                val message = when (code) {
                    500 -> "Internal Server Error (500)"
                    404 -> "Resource Not Found (404)"
                    401 -> "Unauthorized Access (401)"
                    else -> "Unexpected error: $code"
                }

                Log.e("API_ERROR", "Code: $code | Body: $errorBody")

                emit(ApiResponse.error(data = null, message = message))
            }

        } catch (exception: Exception) {
            Log.e("API_EXCEPTION", "Exception: ${exception.localizedMessage}")
            emit(ApiResponse.error(data = null, message = exception.message ?: "Network Error Occurred!"))
        }
    }

    fun getCustomerLoanEmiDetailsReq(req: GetCustomerLoanDetailsReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getCustomerLoanEmiDetailsReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getTransactionHistoryList(req: TransactionHistoryReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getTransactionHistoryList(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getCustomerLoanEmiDetailsReq(req: DueOverdueRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.dueoverdueCustomerRequest(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getverifycustomerReq(req: VerifyCustomerReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.verifycustomerReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun getverifycustomerKitReq(req: VerifyCustomerReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.verifycustomerKitReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun verifyKitcustomerReq(req: VerifyCustomerReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.verifyKitcustomerReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getCustomerLoanDetailsReq(req: CustomerLoanEmiReceiveReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getCustomerLoanEmiReceiveReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getRetailerWalletPayoutReqForMakePayment(req: RetailerWalletPayoutAtMakePaymentTimeReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            val response = repository.RetailerWalletPayoutReqForMakePayment(req)
            if (response!!.isSuccessful) {
                emit(ApiResponse.success(data = response))
            }
            else{
                // ❌ Handle API error (like 500)
                val errorBody = response.errorBody()?.string()
                val code = response.code()
                val message = when (code) {
                    500 -> "Internal Server Error (500)"
                    404 -> "Resource Not Found (404)"
                    401 -> "Unauthorized Access (401)"
                    else -> "Unexpected error: $code"
                }

                Log.e("API_ERROR", "Code: $code | Body: $errorBody")

                emit(ApiResponse.error(data = null, message = message))
            }
        }
        catch (exception: Exception) {
            Log.e("API_EXCEPTION", "Exception: ${exception.localizedMessage}")
            emit(ApiResponse.error(data = null, message = exception.message ?: "Network Error Occurred!"))
        }
    }



    fun getAadharVerificationReq(req: AadharVerificationReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getAadharVerificationReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getReportsReq(req: GetReportsReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getReportsReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getLoanEligibleReq(req: GetIsEligibleLoanReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getLoanEligibleReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getgetMemberShipReqeReq(req: GetIsEligibleLoanReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getMemberShipReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getRetailerProfileReq(req: RetailerProfileReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getRetailerProfileReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun getRetailerLedgerReq(req: GetRetailerLedgerReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getRetailerLedgerReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun getRetailerWalletPayoutReq(req: RetailerWalletPayoutReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getRetailerWalletPayoutReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun loanSettlementReportReq(req: LoanSettlementReportReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.loanSettlementReportReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getRetailerWalletAmountReq(req: RetailerWalletAmountReq) = liveData(Dispatchers.IO) {

        emit(ApiResponse.loading(data = null))
        try {
            val response = repository.getRetailerWalletAmountReq(req)

            if (response!!.isSuccessful) {
                emit(ApiResponse.success(data = response))
            } else {
                // ❌ Handle API error (like 500)
                val errorBody = response.errorBody()?.string()
                val code = response.code()
                val message = when (code) {
                    500 -> "Internal Server Error (500)"
                    404 -> "Resource Not Found (404)"
                    401 -> "Unauthorized Access (401)"
                    else -> "Unexpected error: $code"
                }

                Log.e("API_ERROR", "Code: $code | Body: $errorBody")

                emit(ApiResponse.error(data = null, message = message))
            }

        } catch (exception: Exception) {
            Log.e("API_EXCEPTION", "Exception: ${exception.localizedMessage}")
            emit(ApiResponse.error(data = null, message = exception.message ?: "Network Error Occurred!"))
        }
    }

    fun getRetailerWalletReport(req: RetailerWalletReportReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getRetailerWalletReportReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getPayoutReportReq(req: PayoutReportReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getPayoutReportReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getAddBankAccountReq(req: com.bosandroidapp.aopaykit.data.model.AddBankAccountReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getAddBankAccountReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getHoldAmountWithdrawRequest(req: HoldAmountWithdrawReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.requestHoldAmountWithdrawRequest(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getLowCibilReports(req: LowCibilCustomerReportReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getLowCibilReports(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getAccessKeyForValidateAPKReq(req: GenerateAccessTokenRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getAccessKeyForValidateAPKReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun getAccessKeyForValidateAPKReq(req: ValidateAccessKeyReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.validateTokenFromRetailerReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getSessionReq(req: SessionOutReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.sessionOutReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getSessionExpiredReq(req: ValidateSessionRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.sessionExpired(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun uploadDeviceInfo(req: UploadDeviceInfoReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.uploadDeviceInfo(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun UpdateEmandateDetails(req: EnachDateUploadReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.UpdateEmandateDetails(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun NotificationSendTokenRequest(req: NotificationSendTokenRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.sendTokenViaNotificationReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun updateActionFromCustomerDevice(req: UpdateCustomerDeviceActionRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.updateActionFromCustomerDevice(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }




        fun sendNotificationFeatureNameReq(req: SendNotificationFeatureNameRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.sendNotificationFeatureNameReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun getPurchaseHistoryRequest(req: KitPurchaseHistoryRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getPurchaseHistoryRequest(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun savePurchaseHistoryDataOnSuccessPG(req: KitPurchasePlanSaveRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.savePurchaseKitPlan(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun LoanEmIScheduleWithStatusReq(req: CustomerEmiStatusReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.LoanEmIScheduleWithStatusReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun GetAdminBankDetailsReq(req: AdminBankDetailsReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.GetAdminBankDetailsReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun uploadDocumentForRaisAmountTransferAdminReq(req: RaiseMakePaymentReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.uploadDocumentForRaisAmountTransferAdminReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

    fun MakePaymentAdminReportRequest(req: MakePaymentAdminReportRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getMakePaymentReportReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun kitPlanTopUpRequest(req: KitPlanRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.kitPlanTopUpRequest(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getRequestKitOption(req: KitOptionRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getRequestKitOption(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getKitCustomerLocation(req: GetKitCustomerLocation) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getKitCustomerLocation(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getRetailerDeviceActionToCustomerRequest(req: RetailerSaveDeviceActionRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getRetailerDeviceActionToCustomerRequest(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun sendRetailerNotificationToCustomerRequest(req: RetailerSendNotificationToCustomerReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.sendRetailerNotificationToCustomerRequest(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun getPendingDeviceActionRequest(req: GetPendingDeviceActionReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getPendingDeviceActionRequest(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun getActiveDeviceActionRequest(req: GetPendingDeviceActionReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getActiveDeviceActionRequest(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getCustomerKitRequest(req : CustomerKitRequest)= liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getCustomerKitRequest(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun uploadCustomerDeviceInsatlledAppsOnServerRequest(req : SendInstalledAppOnServerRequest)= liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.uploadCustomerDeviceInsatlledAppsOnServerRequest(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun getkitInventoryListRequest(req : GetKitInventoryListRequest)= liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getkitInventoryListRequest(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun getKitCustomerInstalledAppRequest(createdBy: String) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getKitCustomerInstalledAppRequest(createdBy)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun saveRetailerDeviceTokenRequest(req : SaveRetailerDeviceTokenRequest) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.saveRetailerDeviceTokenRequest(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


}