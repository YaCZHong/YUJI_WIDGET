package com.czh.yuji_widget

import com.czh.yuji_widget.http.repo.WeatherRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    Thread.sleep(1000)
    GlobalScope.launch {
        val data = WeatherRepo.getWeather()
        print(data)
    }
    Thread.sleep(10000)
}

fun getCities(key: String) {
//    val url =
//        "https://geoapi.qweather.com/v2/city/lookup?location=$key&key=f73e360db4694d4881b98a9fb49d6cb0"


}

fun getWeather7D(lat: String, lon: String) {
    val url =
        "https://devapi.qweather.com/v7/weather/7d?location=$lon,$lat&key=f73e360db4694d4881b98a9fb49d6cb0"
}

fun getWeatherNow(lat: String, lon: String) {
    val url =
        "https://devapi.qweather.com/v7/weather/now?location=$lon,$lat&key=f73e360db4694d4881b98a9fb49d6cb0"
}