package com.czh.yuji_widget.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.czh.yuji_widget.activity.MainActivity
import com.czh.yuji_widget.R
import com.czh.yuji_widget.db.AppDatabase
import com.czh.yuji_widget.http.response.Now
import com.czh.yuji_widget.util.GsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

            GlobalScope.launch {
                val city = withContext(Dispatchers.IO) {
                    AppDatabase.getInstance().cityDao().getWidgetCity()
                }
                val weatherNow = GsonUtils.instance.fromJson(city.weatherNowJson, Now::class.java)
                val remoteViews =
                    RemoteViews(context.packageName, R.layout.appwidget_simple).apply {
                        setOnClickPendingIntent(R.id.tv_jump, pendingIntent)
                        setImageViewResource(R.id.iv_weather, R.drawable.ic_100)
                        setTextViewText(R.id.tv_temp, "${weatherNow.temp}℃")
                        setTextViewText(R.id.tv_city, city.city)
                    }
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }
        }
    }
}