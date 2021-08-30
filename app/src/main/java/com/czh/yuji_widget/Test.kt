package com.czh.yuji_widget

import com.czh.yuji_widget.http.repo.CityRepo
import com.czh.yuji_widget.http.repo.WeatherRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    Thread.sleep(1000)
    GlobalScope.launch {
        val data = CityRepo.getCities("chaozhou","f73e360db4694d4881b98a9fb49d6cb0")
        print(data)
    }

//    GlobalScope.launch {
//        val data = WeatherRepo.getWeatherNow("116.63230,23.66170","f73e360db4694d4881b98a9fb49d6cb0")
//        print(data)
//    }
    Thread.sleep(10000)
}