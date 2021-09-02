package com.czh.yuji_widget.vm

import androidx.lifecycle.viewModelScope
import com.czh.yuji_widget.db.AppDatabase
import com.czh.yuji_widget.db.City
import com.czh.yuji_widget.http.repo.WeatherRepo
import com.czh.yuji_widget.util.GsonUtils
import com.czh.yuji_widget.appwidget.notifyWidgetUpdate
import com.czh.yuji_widget.http.response.Weather7DResponse
import com.czh.yuji_widget.http.response.WeatherNowResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainVM : BaseVM() {

    fun getWeather(city: City) {
        viewModelScope.launch(Dispatchers.IO) {
            handleLoadingStatus(true)
            try {
                val data1 = async { getWeatherNow(city) }
                val data2 = async { getWeather7D(city) }
                updateWeather(city, data1.await(), data2.await())
            } catch (e: Exception) {
                toastHintLiveData.postValue("获取${city.city}天气失败")
                e.printStackTrace()
            } finally {
                handleLoadingStatus(false)
            }
        }
    }

    private suspend fun getWeatherNow(city: City): WeatherNowResponse {
        return withContext(Dispatchers.IO) {
            WeatherRepo.getWeatherNow(city.lat, city.lon)
        }
    }

    private suspend fun getWeather7D(city: City): Weather7DResponse {
        return withContext(Dispatchers.IO) {
            WeatherRepo.getWeather7D(city.lat, city.lon)
        }
    }

    private suspend fun updateWeather(
        city: City,
        weatherNow: WeatherNowResponse,
        weather7D: Weather7DResponse
    ) {
        if (weatherNow.code == "200" && weather7D.code == "200") {
            city.updateTime = System.currentTimeMillis()
            city.weatherNowJson = GsonUtils.instance.toJson(weatherNow.now)
            city.weatherDailyJson = GsonUtils.instance.toJson(weather7D.daily)

            AppDatabase.getInstance().cityDao().updateCity(city)
            if (city.isWidget == 1) {
                notifyWidgetUpdate()
            }
        }
    }

    fun makeCityToWidget(city: City) {

    }

    fun deleteCity(city: City) {
        viewModelScope.launch(Dispatchers.IO) {
            val cities = AppDatabase.getInstance().cityDao().getCities()
            if (cities.size == 1) {
                toastHintLiveData.postValue("城市不能为空")
                return@launch
            }
            if (city.isWidgetCity()) {
                AppDatabase.getInstance().cityDao().deleteCity(city)
                AppDatabase.getInstance().cityDao().getCities().also {
                    it[0].isWidget = 1
                    AppDatabase.getInstance().cityDao().updateCity(it[0])
                    notifyWidgetUpdate()
                }
            } else {
                AppDatabase.getInstance().cityDao().deleteCity(city)
            }
            toastHintLiveData.postValue("删除成功")
        }
    }
}