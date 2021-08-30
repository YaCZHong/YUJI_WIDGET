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
import com.czh.yuji_widget.http.response.Daily
import com.czh.yuji_widget.util.GsonUtils
import com.czh.yuji_widget.util.WeatherIconUtils
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

            GlobalScope.launch {
                val city = withContext(Dispatchers.IO) {
                    AppDatabase.getInstance().cityDao().getWidgetCity()
                }
                val weatherDailies = GsonUtils.instance.fromJson<List<Daily>>(
                    city.weatherDailyJson,
                    object : TypeToken<List<Daily>>() {}.type
                )
                val remoteViews =
                    RemoteViews(context.packageName, R.layout.appwidget_detail).apply {
                        setOnClickPendingIntent(R.id.ll_weather, pendingIntent)
                        setTextViewText(R.id.tv_city, city.city)

                        setTextViewText(R.id.tv_date_1, weatherDailies[0].fxDate.substring(5))
                        setImageViewResource(
                            R.id.iv_weather_1,
                            WeatherIconUtils.getIcon(weatherDailies[0].iconDay)
                        )
                        setTextViewText(
                            R.id.tv_temp_range_1,
                            "${weatherDailies[0].tempMax}/${weatherDailies[0].tempMin}"
                        )
                        setTextViewText(R.id.tv_date_2, weatherDailies[1].fxDate.substring(5))
                        setImageViewResource(
                            R.id.iv_weather_2,
                            WeatherIconUtils.getIcon(weatherDailies[1].iconDay)
                        )
                        setTextViewText(
                            R.id.tv_temp_range_2,
                            "${weatherDailies[1].tempMax}/${weatherDailies[1].tempMin}"
                        )
                        setTextViewText(R.id.tv_date_3, weatherDailies[2].fxDate.substring(5))
                        setImageViewResource(
                            R.id.iv_weather_3,
                            WeatherIconUtils.getIcon(weatherDailies[2].iconDay)
                        )
                        setTextViewText(
                            R.id.tv_temp_range_3,
                            "${weatherDailies[2].tempMax}/${weatherDailies[2].tempMin}"
                        )
                        setTextViewText(R.id.tv_date_4, weatherDailies[3].fxDate.substring(5))
                        setImageViewResource(
                            R.id.iv_weather_4,
                            WeatherIconUtils.getIcon(weatherDailies[3].iconDay)
                        )
                        setTextViewText(
                            R.id.tv_temp_range_4,
                            "${weatherDailies[3].tempMax}/${weatherDailies[3].tempMin}"
                        )
                        setTextViewText(R.id.tv_date_5, weatherDailies[4].fxDate.substring(5))
                        setImageViewResource(
                            R.id.iv_weather_5,
                            WeatherIconUtils.getIcon(weatherDailies[4].iconDay)
                        )
                        setTextViewText(
                            R.id.tv_temp_range_5,
                            "${weatherDailies[4].tempMax}/${weatherDailies[4].tempMin}"
                        )
                    }
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }
        }
    }
}