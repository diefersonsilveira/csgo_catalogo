package com.project.csgoinfos.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE = "https://raw.githubusercontent.com/ByMykel/CSGO-API/main/public/api/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttp = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val gson = GsonBuilder().setLenient().create()

    fun api(lang: String): CSGOApi {
        return Retrofit.Builder()
            .baseUrl("$BASE$lang/")
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CSGOApi::class.java)
    }
}

