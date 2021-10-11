package com.czh.yuji_widget.util

import android.util.Log
import com.czh.yuji_widget.BuildConfig
import com.czh.yuji_widget.util.toast.toastLong
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object AppCoroutine {

    private const val TAG = "AppCoroutine"
    private val supervisor = SupervisorJob()
    private val handler = CoroutineExceptionHandler { context, throwable ->
        if (BuildConfig.DEBUG) {
            toastLong("$throwable")
        }
        Log.e(TAG, "coroutine($context) error occur: $throwable")
    }
    val Main = Dispatchers.Main + CoroutineName("YJ-Main") + handler + supervisor
    val IO = Dispatchers.IO + CoroutineName("YJ-IO") + handler + supervisor
    val Default = Dispatchers.Default + CoroutineName("YJ-Default") + handler + supervisor

}
