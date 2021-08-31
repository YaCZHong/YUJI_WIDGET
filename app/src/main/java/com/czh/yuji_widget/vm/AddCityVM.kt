package com.czh.yuji_widget.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.czh.yuji_widget.http.repo.CityRepo
import com.czh.yuji_widget.http.response.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCityVM : ViewModel() {

    val toastHint = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>(false)
    val cities = MutableLiveData<List<Location>>()

    fun getCities(query: String) {
        viewModelScope.launch(Dispatchers.Main) {
            loading.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    CityRepo.getCities(query)
                }
                if (response.code == "200") {
                    cities.value = response.location
                } else {
                    toastHint.value = "搜索失败，errorCode: ${response.code}"
                }
                loading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                toastHint.value = "搜索失败，errorInfo: ${e.message}"
                loading.value = false
            }
        }
    }
}