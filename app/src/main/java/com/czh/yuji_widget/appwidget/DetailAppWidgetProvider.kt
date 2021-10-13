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
import com.czh.yuji_widget.http.response.Daily
import com.czh.yuji_widget.util.GsonUtils
import com.czh.yuji_widget.util.getIcon
import com.czh.yuji_widget.util.getWeek
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailAppWidgetProvider : AppWidgetProvider() {

    private val jumpAppIntent = Intent(AppConfig.mContext, MainActivity::class.java).let { intent ->
        PendingIntent.getActivity(AppConfig.mContext, 0, intent, 0)
    }
    private val jumpAlarmIntent = Intent("android.intent.action.SHOW_ALARMS").let { intent ->
        PendingIntent.getActivity(AppConfig.mContext, 0, intent, 0)
    }
//    private val jumpCalendarIntent = Intent().let { intent ->
//        intent.component = ComponentName(
//            "com.android.calendar",
//            "com.android.calendar.LaunchActivity"
//        )
//        PendingIntent.getActivity(AppConfig.mContext, 0, intent, 0)
//    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        when (intent.action) {
            ACTION_UPDATE_WIDGET -> {
                GlobalScope.launch {
                    val city = withContext(Dispatchers.IO) {
                        AppDatabase.getInstance().cityDao().getWidgetCity()
                    }
                    val weatherDailies = GsonUtils.instance.fromJson<List<Daily>>(
                        city.weatherDailyJson,
                        object : TypeToken<List<Daily>>() {}.type
                    )

                    val remoteViews = getRemoteViews(context, city, weatherDailies)
                    AppWidgetManager.getInstance(context).updateAppWidget(
                        ComponentName(context, DetailAppWidgetProvider::class.java),
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
            val weatherDailies = GsonUtils.instance.fromJson<List<Daily>>(
                city.weatherDailyJson,
                object : TypeToken<List<Daily>>() {}.type
            )

            appWidgetIds.forEach { appWidgetId ->
                val remoteViews = getRemoteViews(context, city, weatherDailies)
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }
        }
    }

    private fun getRemoteViews(
        context: Context,
        city: City,
        weatherDailies: List<Daily>
    ): RemoteViews {

        return RemoteViews(context.packageName, R.layout.appwidget_detail).apply {
            setOnClickPendingIntent(R.id.ll_weather, jumpAppIntent)
            setOnClickPendingIntent(R.id.ll_time, jumpAlarmIntent)

            setTextViewText(R.id.tv_city, city.city)

            val arrayDay0 = weatherDailies[0].fxDate.split("-").map { it.toInt() }
            val arrayDay1 = weatherDailies[1].fxDate.split("-").map { it.toInt() }
            val arrayDay2 = weatherDailies[2].fxDate.split("-").map { it.toInt() }
            val arrayDay3 = weatherDailies[3].fxDate.split("-").map { it.toInt() }
            val arrayDay4 = weatherDailies[4].fxDate.split("-").map { it.toInt() }
            setTextViewText(R.id.tv_date_1, getWeek(arrayDay0[0], arrayDay0[1], arrayDay0[2]))
            setTextViewText(R.id.tv_date_2, getWeek(arrayDay1[0], arrayDay1[1], arrayDay1[2]))
            setTextViewText(R.id.tv_date_3, getWeek(arrayDay2[0], arrayDay2[1], arrayDay2[2]))
            setTextViewText(R.id.tv_date_4, getWeek(arrayDay3[0], arrayDay3[1], arrayDay3[2]))
            setTextViewText(R.id.tv_date_5, getWeek(arrayDay4[0], arrayDay4[1], arrayDay4[2]))

            setImageViewResource(
                R.id.iv_weather_1,
                getIcon(weatherDailies[0].iconDay)
            )
            setImageViewResource(
                R.id.iv_weather_2,
                getIcon(weatherDailies[1].iconDay)
            )
            setImageViewResource(
                R.id.iv_weather_3,
                getIcon(weatherDailies[2].iconDay)
            )
            setImageViewResource(
                R.id.iv_weather_4,
                getIcon(weatherDailies[3].iconDay)
            )
            setImageViewResource(
                R.id.iv_weather_5,
                getIcon(weatherDailies[4].iconDay)
            )
            setTextViewText(
                R.id.tv_temp_range_1,
                "${weatherDailies[0].tempMax}°/${weatherDailies[0].tempMin}°"
            )
            setTextViewText(
                R.id.tv_temp_range_2,
                "${weatherDailies[1].tempMax}°/${weatherDailies[1].tempMin}°"
            )
            setTextViewText(
                R.id.tv_temp_range_3,
                "${weatherDailies[2].tempMax}°/${weatherDailies[2].tempMin}°"
            )
            setTextViewText(
                R.id.tv_temp_range_4,
                "${weatherDailies[3].tempMax}°/${weatherDailies[3].tempMin}°"
            )
            setTextViewText(
                R.id.tv_temp_range_5,
                "${weatherDailies[4].tempMax}°/${weatherDailies[4].tempMin}°"
            )
        }
    }
}