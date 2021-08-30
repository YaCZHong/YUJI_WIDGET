package com.czh.yuji_widget.http.repo

import com.czh.yuji_widget.BuildConfig
import com.czh.yuji_widget.http.HttpManager
import com.czh.yuji_widget.http.api.WeatherApi
import com.czh.yuji_widget.http.response.Weather7DResponse
import com.czh.yuji_widget.http.response.WeatherNowResponse

object WeatherRepo {
    private val api = HttpManager.retrofitOfWeather.create(WeatherApi::class.java)

    suspend fun getWeather7D(
        lat: String,
        lon: String,
        key: String = BuildConfig.HF_KEY
    ): Weather7DResponse {
        return api.getWeather7D("$lon,$lat", key)
    }

    suspend fun getWeatherNow(
        lat: String,
        lon: String,
        key: String = BuildConfig.HF_KEY
    ): WeatherNowResponse {
        return api.getWeatherNow("$lon,$lat", key)
    }
}