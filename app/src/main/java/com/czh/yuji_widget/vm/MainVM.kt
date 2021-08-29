package com.czh.yuji_widget.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.czh.yuji_widget.adapter.MyWeatherCity

class MainVM : ViewModel() {
    val cities = MutableLiveData<List<MyWeatherCity>>()

    fun setCities(list: List<MyWeatherCity>) {
        cities.postValue(list)
    }

    fun notifyUpdate(){
        cities.postValue(cities.value)
    }
}