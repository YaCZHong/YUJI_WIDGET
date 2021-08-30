package com.czh.yuji_widget

import android.app.Application
import com.czh.yuji_widget.config.AppConfig

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppConfig.init(this)
    }
}