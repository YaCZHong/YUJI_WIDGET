package com.czh.yuji_widget.http.api

import com.czh.yuji_widget.http.response.PoemResponse
import com.czh.yuji_widget.http.response.PoemTokenResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface PoemApi {
    @GET("token")
    suspend fun getToken(): PoemTokenResponse

    @GET("sentence")
    suspend fun getPoem(@Header("X-User-Token") token: String): PoemResponse
}