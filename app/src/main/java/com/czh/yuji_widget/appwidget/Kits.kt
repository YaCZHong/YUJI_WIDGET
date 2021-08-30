package com.czh.yuji_widget.appwidget

import android.content.ComponentName
import android.os.Build
import android.content.Intent
import com.czh.yuji_widget.config.AppConfig

const val ACTION_UPDATE_WIDGET = "com.czh.yuji_widget.action.APPWIDGET_UPDATE"

/**
 * 发送更新小部件广播
 */
fun notifyWidgetUpdate() {
    val intent = Intent(ACTION_UPDATE_WIDGET)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        intent.component = ComponentName(AppConfig.mContext, SimpleAppWidgetProvider::class.java)
        AppConfig.mContext.sendBroadcast(intent)
        intent.component = ComponentName(AppConfig.mContext, DetailAppWidgetProvider::class.java)
        AppConfig.mContext.sendBroadcast(intent)
    } else {
        AppConfig.mContext.sendBroadcast(intent)
    }
}