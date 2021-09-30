package com.czh.yuji_widget.http.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PoemTokenResponse(val status: String, val data: String) : Parcelable
