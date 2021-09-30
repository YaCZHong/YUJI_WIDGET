package com.czh.yuji_widget.http.repo

import com.czh.yuji_widget.http.HttpManager
import com.czh.yuji_widget.http.api.PoemApi
import com.czh.yuji_widget.http.response.PoemResponse
import com.czh.yuji_widget.http.response.PoemTokenResponse
import com.czh.yuji_widget.util.Persistent

object PoemRepo {
    private val api = HttpManager.retrofitOfPoem.create(PoemApi::class.java)

    private suspend fun getToken(): PoemTokenResponse {
        return api.getToken()
    }

    suspend fun getPoem(): PoemResponse {
        var token = Persistent.poemToken
        if (token == null) {
            try {
                token = getToken().data
                Persistent.poemToken = token
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return api.getPoem(token ?: "")
    }
}