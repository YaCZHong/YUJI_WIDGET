package com.czh.yuji_widget.http.response

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class Weather7DResponse(
    val code: String,
    val daily: List<Daily>,
    val fxLink: String,
    val refer: Refer,
    val updateTime: String
) : Parcelable

@Parcelize
data class Daily(
    val cloud: String,
    val fxDate: String,
    val humidity: String,
    val iconDay: String,
    val iconNight: String,
    val moonPhase: String,
    val moonrise: String,
    val moonset: String,
    val precip: String,
    val pressure: String,
    val sunrise: String,
    val sunset: String,
    val tempMax: String,
    val tempMin: String,
    val textDay: String,
    val textNight: String,
    val uvIndex: String,
    val vis: String,
    val wind360Day: String,
    val wind360Night: String,
    val windDirDay: String,
    val windDirNight: String,
    val windScaleDay: String,
    val windScaleNight: String,
    val windSpeedDay: String,
    val windSpeedNight: String
) : Parcelable