package com.czh.yuji_widget.http.repo

import com.czh.yuji_widget.http.HttpManager
import com.czh.yuji_widget.http.api.WeatherApi

object WeatherRepo {
    private val api = HttpManager.retrofit.create(WeatherApi::class.java)

    suspend fun getWeather(): String {
        return api.getCities()
    }
}