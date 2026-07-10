package com.bos.payment.appName.network

import com.bosandroidapp.aopaykit.constant.ConstantClass
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private var retrofit: Retrofit? = null

    private fun getAllInstance(): Retrofit {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        // Create OkHttpClient with 1-minute timeout settings
        val okHttpClient = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(60, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(60, TimeUnit.SECONDS)   // Write timeout
            .build()

        // Build Retrofit instance with the custom OkHttpClient
        return Retrofit.Builder()
            .baseUrl(ConstantClass.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }

    private fun getAllInstanceSMS(): Retrofit{
        // Create OkHttpClient with 1-minute timeout settings
        val okHttpClient = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(120, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(120, TimeUnit.SECONDS)      // server response time
            .writeTimeout(120, TimeUnit.SECONDS)     // request send time
            .build()

        // Build Retrofit instance with the custom OkHttpClient
        return Retrofit.Builder()
            .baseUrl(ConstantClass.SMS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }


    private fun getAllInstancePAN(): Retrofit{
        // Create OkHttpClient with 1-minute timeout settings
        val okHttpClient = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(60, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(60, TimeUnit.SECONDS)   // Write timeout
            .build()

        // Build Retrofit instance with the custom OkHttpClient
        return Retrofit.Builder()
            .baseUrl(ConstantClass.PAN_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    }


    val apiInterface: ApiInterface = getAllInstance().create(ApiInterface::class.java)
    val apiInterfaceSMS: ApiInterface = getAllInstanceSMS().create(ApiInterface::class.java)

    val apiInterfacePAN: ApiInterface = getAllInstancePAN().create(ApiInterface::class.java)

}