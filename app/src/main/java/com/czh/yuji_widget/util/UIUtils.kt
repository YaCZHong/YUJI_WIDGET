package com.czh.yuji_widget.util

import android.os.Handler
import android.os.Looper

object UIUtils {
    private val handler = Handler(Looper.getMainLooper())

    fun postTask(runnable: Runnable, delay: Long = 0) {
        handler.postDelayed(runnable, 0)
    }
}