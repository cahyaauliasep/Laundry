package org.d3if0088.penghitungan.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.d3if0088.penghitungan.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    fun retrofitService(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG)
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE

        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.readTimeout(60, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(60, TimeUnit.SECONDS)
        okHttpClient.addInterceptor(httpLoggingInterceptor).build()

        return Retrofit.Builder()
            .baseUrl("https://reqres.in/api/")
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}