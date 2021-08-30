package com.czh.yuji_widget.http.api

import com.czh.yuji_widget.http.response.HCitiesResponse
import retrofit2.http.*

interface CityApi {
    @GET(".")
    suspend fun getCities(
        @Query("location") location: String,
        @Query("key") key: String
    ): HCitiesResponse
}