package com.czh.yuji_widget.util

import android.util.Log
import com.czh.yuji_widget.BuildConfig
import com.czh.yuji_widget.util.toast.toastLong
import kotlinx.coroutines.CoroutineExceptionHandler

object AppCoroutine {
    private const val TAG = "AppCoroutine"
    val handler = CoroutineExceptionHandler { context, throwable ->
        if (BuildConfig.DEBUG) {
            toastLong("$throwable")
        }
        Log.e(TAG, "coroutine($context) error occur: $throwable")
    }
}
