package com.czh.yuji_widget.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.czh.yuji_widget.db.AppDatabase
import com.czh.yuji_widget.db.City
import com.czh.yuji_widget.http.repo.WeatherRepo
import com.czh.yuji_widget.util.GsonUtils
import com.czh.yuji_widget.appwidget.notifyWidgetUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainVM : ViewModel() {

    fun getWeatherNow(city: City) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    WeatherRepo.getWeatherNow(city.lat, city.lon)
                }
                if (response.code == "200") {
                    city.weatherNowJson = GsonUtils.instance.toJson(response.now)
                    AppDatabase.getInstance().cityDao().updateCity(city)
                    if (city.isWidget == 1) {
                        notifyWidgetUpdate()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getWeather7D(city: City) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    WeatherRepo.getWeather7D(city.lat, city.lon)
                }
                if (response.code == "200") {
                    city.weatherDailyJson = GsonUtils.instance.toJson(response.daily)
                    AppDatabase.getInstance().cityDao().updateCity(city)
                    if (city.isWidget == 1) {
                        notifyWidgetUpdate()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}