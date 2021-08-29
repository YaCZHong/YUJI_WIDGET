package com.czh.yuji_widget.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.czh.yuji_widget.activity.MainActivity
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
                setOnClickPendingIntent(R.id.ll_weather, pendingIntent)
                setTextViewText(R.id.tv_city, "天河区")
                setTextViewText(R.id.tv_date_1, "09-14")
                setImageViewResource(R.id.iv_weather_1, R.drawable.ic_101)
                setTextViewText(R.id.tv_temp_range_1, "34/29")
                setTextViewText(R.id.tv_date_2, "09-15")
                setImageViewResource(R.id.iv_weather_2, R.drawable.ic_102)
                setTextViewText(R.id.tv_temp_range_2, "34/29")
                setTextViewText(R.id.tv_date_3, "09-16")
                setImageViewResource(R.id.iv_weather_3, R.drawable.ic_103)
                setTextViewText(R.id.tv_temp_range_3, "34/29")
                setTextViewText(R.id.tv_date_4, "09-17")
                setImageViewResource(R.id.iv_weather_4, R.drawable.ic_104)
                setTextViewText(R.id.tv_temp_range_4, "34/29")
                setTextViewText(R.id.tv_date_5, "09-18")
                setImageViewResource(R.id.iv_weather_5, R.drawable.ic_150)
                setTextViewText(R.id.tv_temp_range_5, "34/29")
            }
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }
    }
}