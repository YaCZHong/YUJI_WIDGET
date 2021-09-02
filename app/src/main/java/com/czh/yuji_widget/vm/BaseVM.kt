package com.czh.yuji_widget.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseVM : ViewModel() {
    val toastHintLiveData = MutableLiveData<String>()
    val loadingLiveData = MutableLiveData(false)
    private var loadingCount: Int = 0

    fun handleLoadingStatus(loading: Boolean) {
        if (loading) {
            loadingCount++
        } else {
            loadingCount--
        }

        // 获取Loading的状态，非0时显示，0时隐藏
        val show: Boolean = loadingCount != 0

        // 过滤重复
        if (show != loadingLiveData.value) {
            loadingLiveData.postValue(show)
        }
    }
}