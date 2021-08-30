package com.czh.yuji_widget.http.response

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class HCitiesResponse(
    val code: String,
    val location: List<Location>,
    val refer: Refer
) : Parcelable

@Parcelize
data class Location(
    val adm1: String,
    val adm2: String,
    val country: String,
    val fxLink: String,
    val id: String,
    val isDst: String,
    val lat: String,
    val lon: String,
    val name: String,
    val rank: String,
    val type: String,
    val tz: String,
    val utcOffset: String
) : Parcelable

@Parcelize
data class Refer(
    val license: List<String>,
    val sources: List<String>
) : Parcelable