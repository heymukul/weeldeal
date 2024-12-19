package com.example.project1.api

import androidx.room.Transaction
import com.example.project1.Models.CheckStatus
import com.google.android.gms.common.api.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Part

interface APiInterface {
    @GET("apis/pg-sandbox/pg/v1/status/{merchantId}/{transactionId}")
    suspend fun checkStatus(
        @HeaderMap headers: Map<String,String>,
        @Part("merchantId") merchantId : String,
        @Part("transactionId") transactionId : String
    ):retrofit2.Response<CheckStatus>
}