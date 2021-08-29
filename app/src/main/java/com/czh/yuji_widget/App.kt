package com.czh.yuji_widget

import android.app.Application
import com.czh.yuji_widget.config.AppConfig
import com.qweather.sdk.view.HeConfig

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppConfig.init(this)
        HeConfig.init("HE2108281833141145", "b93951ebd1b048d1a0198e91007a995a")
        HeConfig.switchToDevService()
    }
}