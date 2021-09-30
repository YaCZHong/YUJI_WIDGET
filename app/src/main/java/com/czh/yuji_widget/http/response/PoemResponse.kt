package com.czh.yuji_widget.http.response

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class PoemResponse(
    val data: Data,
    val ipAddress: String,
    val status: String,
    val token: String,
    val errCode: Int = 0,
    val errMessage: String = ""
) : Parcelable

@Parcelize
data class Data(
    val cacheAt: String,
    val content: String,
    val id: String,
    val matchTags: List<String>,
    val origin: Origin,
    val popularity: Int,
    val recommendedReason: String
) : Parcelable

@Parcelize
data class Origin(
    val author: String,
    val content: List<String>,
    val dynasty: String,
    val title: String,
    val translate: List<String>
) : Parcelable