package com.czh.yuji_widget.http.repo

import com.czh.yuji_widget.BuildConfig
import com.czh.yuji_widget.http.HttpManager
import com.czh.yuji_widget.http.api.CityApi
import com.czh.yuji_widget.http.response.HCitiesResponse

object CityRepo {
    private val api = HttpManager.retrofitOfCity.create(CityApi::class.java)

    suspend fun getCities(location: String, key: String = BuildConfig.HF_KEY): HCitiesResponse {
        return api.getCities(location, key)
    }
}