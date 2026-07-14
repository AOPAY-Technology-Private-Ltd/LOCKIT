package com.bosandroidapp.aopaykit.data.repository

import com.bos.payment.appName.network.ApiInterface
import com.bosandroidapp.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore.CibilScoreReq
import com.bosandroidapp.aopaykit.data.model.loginsignup.verification.AAdhaarDetailesReq

class CibilRepository(private val apiInterface: ApiInterface) {

    suspend fun getReportsReq(req: CibilScoreReq) = apiInterface.getcibilscore(req)

    suspend fun getAadharDetailsReq(req: AAdhaarDetailesReq) = apiInterface.getAadharDetails(req)



}