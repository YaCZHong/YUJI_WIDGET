package com.czh.yuji_widget.http

import com.czh.yuji_widget.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object HttpManager {

    private const val TIME_OUT = 15L

    val retrofitOfCity: Retrofit by lazy {
        Retrofit.Builder()
            .client(okClient)
            .baseUrl(Urls.CITY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val retrofitOfWeather: Retrofit by lazy {
        Retrofit.Builder()
            .client(okClient)
            .baseUrl(Urls.WEATHER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val okClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(logger)
        builder.build()
    }

    private val logger: HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().also {
            it.level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.BASIC
            }
        }
    }
}
