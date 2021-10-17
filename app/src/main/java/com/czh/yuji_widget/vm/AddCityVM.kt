package com.czh.yuji_widget.vm

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.czh.yuji_widget.http.repo.CityRepo
import com.czh.yuji_widget.http.response.Location
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCityVM : BaseVM() {
    val cities = MutableLiveData<List<Location>>()

    fun getCities(query: String) {
        if (TextUtils.isEmpty(query)) {
            toastHintLiveData.value = "请输入城市名"
            return
        }
        viewModelScope.launch(Dispatchers.Main) {
            handleLoadingStatus(true)
            try {
                val response = withContext(Dispatchers.IO) {
                    CityRepo.getCities(query)
                }
                if (response.code == "200") {
                    cities.value = response.location
                } else {
                    toastHintLiveData.value = "搜索失败，errorCode: ${response.code}"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                toastHintLiveData.value = "搜索失败，errorInfo: ${e.message}"
            } finally {
                handleLoadingStatus(false)
            }
        }
    }
}