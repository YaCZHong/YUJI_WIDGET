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
import com.czh.yuji_widget.config.AppConfig
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

    private val jumpAppIntent = Intent(AppConfig.mContext, MainActivity::class.java).let { intent ->
        PendingIntent.getActivity(AppConfig.mContext, 0, intent, 0)
    }
    private val jumpAlarmIntent = Intent("android.intent.action.SHOW_ALARMS").let { intent ->
        PendingIntent.getActivity(AppConfig.mContext, 0, intent, 0)
    }
    private val jumpCalendarIntent = Intent().let { intent ->
        intent.component = ComponentName(
            "com.android.calendar",
            "com.android.calendar.LaunchActivity"
        )
        PendingIntent.getActivity(AppConfig.mContext, 0, intent, 0)
    }

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

                    val remoteViews = getRemoteViews(context, city, weatherNow)
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
                appWidgetManager.updateAppWidget(
                    appWidgetId,
                    getRemoteViews(context, city, weatherNow)
                )
            }
        }
    }

    private fun getRemoteViews(
        context: Context,
        city: City,
        weatherNow: Now
    ): RemoteViews {
        return RemoteViews(context.packageName, R.layout.appwidget_simple).apply {
            setOnClickPendingIntent(R.id.tv_jump, jumpAppIntent)
            setOnClickPendingIntent(R.id.tv_hour, jumpAlarmIntent)
            setOnClickPendingIntent(R.id.tv_minute, jumpAlarmIntent)
            setOnClickPendingIntent(R.id.tv_date, jumpCalendarIntent)
            setImageViewResource(
                R.id.iv_weather,
                getIcon(weatherNow.icon)
            )
            setTextViewText(R.id.tv_temp, "${weatherNow.temp}℃")
            setTextViewText(R.id.tv_city, city.city)
        }
    }
}