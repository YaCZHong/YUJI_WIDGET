package com.czh.yuji_widget.vm

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseVM : ViewModel() {
    val toastHintLiveData = MutableLiveData<String>()
    val loadingLiveData = MutableLiveData(false)
    private var loadingCount: Int = 0

    /**
     * 该函数只能在主线程中调用
     */
    fun handleLoadingStatus(loading: Boolean) {

        //断言
        assert(Looper.getMainLooper() == Looper.myLooper()) { "handleLoadingStatus函数只能在主线程中调用" }

        if (loading) {
            loadingCount++
        } else {
            loadingCount--
        }

        // 获取Loading的状态，非0时显示，0时隐藏
        val show: Boolean = loadingCount != 0

        // 过滤重复
        if (show != loadingLiveData.value) {
            loadingLiveData.value = show
        }
    }
}