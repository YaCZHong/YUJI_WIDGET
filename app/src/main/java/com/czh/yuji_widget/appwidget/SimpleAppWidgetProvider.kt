package com.czh.yuji_widget.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.czh.yuji_widget.activity.MainActivity
import com.czh.yuji_widget.R
import com.czh.yuji_widget.db.AppDatabase
import com.czh.yuji_widget.db.City
import com.czh.yuji_widget.http.response.Now
import com.czh.yuji_widget.util.GsonUtils
import com.czh.yuji_widget.util.getIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SimpleAppWidgetProvider : AppWidgetProvider() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_UPDATE_WIDGET -> {
                GlobalScope.launch {
                    val city = withContext(Dispatchers.IO) {
                        AppDatabase.getInstance().cityDao().getWidgetCity()
                    }
                    val weatherNow =
                        GsonUtils.instance.fromJson(city.weatherNowJson, Now::class.java)

                    val pendingIntent = Intent(context, MainActivity::class.java).let { intent ->
                        PendingIntent.getActivity(context, 0, intent, 0)
                    }

                    val remoteViews = getRemoteViews(context, city, weatherNow, pendingIntent)
                    AppWidgetManager.getInstance(context).updateAppWidget(
                        ComponentName(context, SimpleAppWidgetProvider::class.java),
                        remoteViews
                    )
                }
            }
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        GlobalScope.launch {
            val city = withContext(Dispatchers.IO) {
                AppDatabase.getInstance().cityDao().getWidgetCity()
            }
            val weatherNow = GsonUtils.instance.fromJson(city.weatherNowJson, Now::class.java)

            appWidgetIds.forEach { appWidgetId ->
                val pendingIntent = Intent(context, MainActivity::class.java).let { intent ->
                    PendingIntent.getActivity(context, 0, intent, 0)
                }
                appWidgetManager.updateAppWidget(
                    appWidgetId,
                    getRemoteViews(context, city, weatherNow, pendingIntent)
                )
            }
        }
    }

    private fun getRemoteViews(
        context: Context,
        city: City,
        weatherNow: Now,
        pendingIntent: PendingIntent
    ): RemoteViews {
        return RemoteViews(context.packageName, R.layout.appwidget_simple).apply {
            setOnClickPendingIntent(R.id.tv_jump, pendingIntent)
            setImageViewResource(
                R.id.iv_weather,
                getIcon(weatherNow.icon)
            )
            setTextViewText(R.id.tv_temp, "${weatherNow.temp}℃")
            setTextViewText(R.id.tv_city, city.city)
        }
    }
}