package com.czh.yuji_widget.http.response

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherNowResponse(
    val code: String,
    val fxLink: String,
    val now: Now,
    val refer: Refer,
    val updateTime: String
) : Parcelable

@Parcelize
data class Now(
    val cloud: String,
    val dew: String,
    val feelsLike: String,
    val humidity: String,
    val icon: String,
    val obsTime: String,
    val precip: String,
    val pressure: String,
    val temp: String,
    val text: String,
    val vis: String,
    val wind360: String,
    val windDir: String,
    val windScale: String,
    val windSpeed: String
) : Parcelable