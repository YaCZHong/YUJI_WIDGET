package com.czh.yuji_widget.http.api

import com.czh.yuji_widget.http.response.Weather7DResponse
import com.czh.yuji_widget.http.response.WeatherNowResponse
import retrofit2.http.*

interface WeatherApi {
    @GET("7d")
    suspend fun getWeather7D(
        @Query("location") location: String,
        @Query("key") key: String
    ): Weather7DResponse

    @GET("now")
    suspend fun getWeatherNow(
        @Query("location") location: String,
        @Query("key") key: String
    ): WeatherNowResponse
}