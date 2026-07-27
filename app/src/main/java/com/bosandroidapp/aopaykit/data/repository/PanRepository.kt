package com.bosandroidapp.aopaykit.data.repository

import com.bos.payment.appName.network.ApiInterface
import com.bosandroidapp.aopaykit.data.enach.EMandateRequest
import com.bosandroidapp.aopaykit.data.enach.ENachStatusReq
import com.bosandroidapp.aopaykit.data.loancharge.LoanChargeReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.AadharVerificationReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.PanVerificationReq
import com.bosandroidapp.aopaykit.data.pennydrop.BankListReq
import com.bosandroidapp.aopaykit.data.pennydrop.PennyDropCheckStatusRequest
import com.bosandroidapp.aopaykit.data.pennydrop.PennyDropRequest
import com.bosandroidapp.aopaykit.data.pg.PGRequestCall

class PanRepository(private val apiInterface: ApiInterface) {

    suspend fun getPanVerificationReq(req: PanVerificationReq) = apiInterface.getPanVarification(req)

    suspend fun getBankListReq(req: BankListReq) = apiInterface.getBankListRequest(req)

    suspend fun getpennyDropReq(req: PennyDropRequest) = apiInterface.pennyDropReq(req)

    suspend fun getpennyDropCheckStatusReq(req: PennyDropCheckStatusRequest) = apiInterface.pennyDropStatus(req)

    suspend fun getEMandateRequestReq(req: EMandateRequest) = apiInterface.geteMandateRequest(req)
    suspend fun geteMandateSatusRequest(req: ENachStatusReq) = apiInterface.geteMandateSatusRequest(req)
    suspend fun loanApplyChargesReq(req: LoanChargeReq) = apiInterface.loanApplyChargesReq(req)

    suspend fun getAadharVerificationReq(req: AadharVerificationReq) = apiInterface.getAadharVarification(req)

    suspend fun getPGRequestCall(req: PGRequestCall) = apiInterface.callPG(req)

    suspend fun getKitPGRequestCall(req: PGRequestCall) = apiInterface.kitCallPG(req)

}
