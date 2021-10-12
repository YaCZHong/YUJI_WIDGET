package com.czh.yuji_widget.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.czh.yuji_widget.R
import com.czh.yuji_widget.db.City
import com.czh.yuji_widget.http.response.Daily
import com.czh.yuji_widget.http.response.Now
import com.czh.yuji_widget.util.GsonUtils
import com.czh.yuji_widget.util.getIcon
import com.czh.yuji_widget.util.getWeek
import com.czh.yuji_widget.util.parseTime
import com.google.gson.reflect.TypeToken

class MainCityAdapter(private val listener: OnItemClickListener<City>) :
    RecyclerView.Adapter<MainCityAdapter.ViewHolder>() {

    private var cities: List<City> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    fun setData(data: List<City>) {
        val diffCallback = CityDiffCallback(cities, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        cities = data
    }

    class ViewHolder(view: View, listener: OnItemClickListener<City>) :
        RecyclerView.ViewHolder(view) {
        private val tvUpdateTime: TextView = itemView.findViewById(R.id.tv_update_time)
        private val tvCity: TextView = itemView.findViewById(R.id.tv_city)
        private val ivWeatherNow: ImageView = itemView.findViewById(R.id.iv_weather_now)
        private val tvWeatherNowTemp: TextView = itemView.findViewById(R.id.tv_weather_now_temp)

        private val tvDate1: TextView = itemView.findViewById(R.id.tv_date_1)
        private val tvDate2: TextView = itemView.findViewById(R.id.tv_date_2)
        private val tvDate3: TextView = itemView.findViewById(R.id.tv_date_3)
        private val tvDate4: TextView = itemView.findViewById(R.id.tv_date_4)
        private val tvDate5: TextView = itemView.findViewById(R.id.tv_date_5)

        private val ivWeather1: ImageView = itemView.findViewById(R.id.iv_weather_1)
        private val ivWeather2: ImageView = itemView.findViewById(R.id.iv_weather_2)
        private val ivWeather3: ImageView = itemView.findViewById(R.id.iv_weather_3)
        private val ivWeather4: ImageView = itemView.findViewById(R.id.iv_weather_4)
        private val ivWeather5: ImageView = itemView.findViewById(R.id.iv_weather_5)

        private val tvTempRange1: TextView = itemView.findViewById(R.id.tv_temp_range_1)
        private val tvTempRange2: TextView = itemView.findViewById(R.id.tv_temp_range_2)
        private val tvTempRange3: TextView = itemView.findViewById(R.id.tv_temp_range_3)
        private val tvTempRange4: TextView = itemView.findViewById(R.id.tv_temp_range_4)
        private val tvTempRange5: TextView = itemView.findViewById(R.id.tv_temp_range_5)

        private val tvDateList = listOf(tvDate1, tvDate2, tvDate3, tvDate4, tvDate5)
        private val ivWeatherList =
            listOf(ivWeather1, ivWeather2, ivWeather3, ivWeather4, ivWeather5)
        private val tvTempRangeList =
            listOf(tvTempRange1, tvTempRange2, tvTempRange3, tvTempRange4, tvTempRange5)

        private var currentCity: City? = null

        init {
            itemView.setOnClickListener {
                currentCity?.let {
                    listener.onClick(it, itemView)
                }
            }

            itemView.setOnLongClickListener {
                currentCity?.let {
                    listener.onLongClick(it, itemView)
                }
                true
            }
        }

        fun bind(city: City) {
            currentCity = city
            tvUpdateTime.text = parseTime(city.updateTime)
            tvCity.text = city.city
//            tvOverdueHint.visibility =
//                if (System.currentTimeMillis() - city.updateTime > 30 * 60 * 1000) View.VISIBLE else View.GONE
            tvCity.isSelected = city.isWidgetCity()

            try {
                if (!TextUtils.isEmpty(city.weatherNowJson)) {
                    val weatherNow =
                        GsonUtils.instance.fromJson(city.weatherNowJson, Now::class.java)
                    updateWeatherNowUI(weatherNow)
                }

                if (!TextUtils.isEmpty(city.weatherDailyJson)) {
                    val weatherDailies = GsonUtils.instance.fromJson<List<Daily>>(
                        city.weatherDailyJson,
                        object : TypeToken<List<Daily>>() {}.type
                    )
                    updateWeatherDailyUI(weatherDailies)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun updateWeatherNowUI(weatherNow: Now) {
            Glide.with(ivWeatherNow.context).load(getIcon(weatherNow.icon))
                .override(140, 140)
                .into(ivWeatherNow)
            tvWeatherNowTemp.text = "${weatherNow.temp}°"
        }

        private fun updateWeatherDailyUI(weatherDailies: List<Daily>) {
            weatherDailies.forEachIndexed { index, daily ->
                val array = daily.fxDate.split("-").map { it.toInt() }
                tvDateList[index].text = getWeek(array[0], array[1], array[2])
                Glide.with(ivWeatherList[index].context).load(getIcon(daily.iconDay))
                    .override(60, 60)
                    .into(ivWeatherList[index])
                tvTempRangeList[index].text = "${daily.tempMax}/${daily.tempMin}"
            }
        }
    }
}