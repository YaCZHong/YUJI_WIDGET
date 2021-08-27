package com.czh.yuji_widget.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.czh.yuji_widget.MainActivity
import com.czh.yuji_widget.R

class DetailAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            val pendingIntent = Intent(context, MainActivity::class.java).let { intent ->
                PendingIntent.getActivity(context, 0, intent, 0)
            }
            val remoteViews = RemoteViews(context.packageName, R.layout.appwidget_detail).apply {
                setImageViewResource(R.id.iv_weather_now, R.drawable.ic_weather_duoyun)
                setTextViewText(R.id.tv_temp_now, "29°")
                setTextViewText(R.id.tv_city, "天河区")
                setTextViewText(R.id.tv_temp_range, "33/28")
            }
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }
}