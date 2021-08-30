package com.czh.yuji_widget.http.api


import retrofit2.http.*

interface WeatherApi {
    @GET(".")
    suspend fun getCities(
        @Query("location") location: String = "潮州",
        @Query("key") key: String = "f73e360db4694d4881b98a9fb49d6cb0"
    ): String
}