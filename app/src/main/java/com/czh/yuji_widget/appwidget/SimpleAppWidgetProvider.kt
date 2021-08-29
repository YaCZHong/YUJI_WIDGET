package com.czh.yuji_widget.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.czh.yuji_widget.activity.MainActivity
import com.czh.yuji_widget.R

class SimpleAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            val pendingIntent = Intent(context, MainActivity::class.java).let { intent ->
                PendingIntent.getActivity(context, 0, intent, 0)
            }
            val remoteViews = RemoteViews(context.packageName, R.layout.appwidget_simple).apply {
                setOnClickPendingIntent(R.id.tv_jump, pendingIntent)
                setImageViewResource(R.id.iv_weather, R.drawable.ic_100)
                setTextViewText(R.id.tv_temp, "29℃")
                setTextViewText(R.id.tv_city, "天河区")
            }
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }
}