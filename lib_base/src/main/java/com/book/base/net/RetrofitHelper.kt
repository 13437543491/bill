package com.book.base.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    private const val READ_TIME_OUT = 10000L
    private const val CONNECT_TIME_OUT = 10000L

    private val mHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    fun <T> createApiService(baseUrl: String, service: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(mHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        return retrofit.create(service)
    }
}