package com.rasadhana.data.remote.retrofit

import com.rasadhana.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private fun createRetrofit(baseUrl: String): Retrofit {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Batas waktu koneksi
            .readTimeout(30, TimeUnit.SECONDS)    // Batas waktu membaca respons
            .writeTimeout(30, TimeUnit.SECONDS)   // Batas waktu mengirim data
            .addInterceptor(loggingInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun getApiService(): ApiService {
        return createRetrofit(BuildConfig.BASE_URL).create(ApiService::class.java)
    }

    fun getMlApiService(): MlApiService {
        return createRetrofit(BuildConfig.ML_BASE_URL).create(MlApiService::class.java)
    }
}