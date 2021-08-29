package com.czh.yuji_widget.config

import android.app.Application
import android.content.Context

object AppConfig {

    private lateinit var mApplication: Application

    val mContext: Context get() = mApplication.applicationContext

    fun init(application: Application) {
        mApplication = application
    }
}