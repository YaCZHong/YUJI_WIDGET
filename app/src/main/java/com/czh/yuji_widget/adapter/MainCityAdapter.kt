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
import com.czh.yuji_widget.http.response.Now
import com.czh.yuji_widget.util.GsonUtils
import com.czh.yuji_widget.util.getIcon

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
//        val diffCallback = CityDiffCallback(cities, data)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        diffResult.dispatchUpdatesTo(this)
//        cities = data
        cities = data
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, listener: OnItemClickListener<City>) :
        RecyclerView.ViewHolder(view) {

        private val ivWeatherNow: ImageView = itemView.findViewById(R.id.iv_weather_now)
        private val tvCity: TextView = itemView.findViewById(R.id.tv_city)
        private val tvWeatherNow: TextView = itemView.findViewById(R.id.tv_weather_now)
        private val tvOverdueHint: TextView = itemView.findViewById(R.id.tv_overdue_hint)
        private val ivWidgetMark: ImageView = itemView.findViewById(R.id.iv_widget_mark)
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
            if (city.city.length > 8) {
                tvCity.textSize = 16f
            } else {
                tvCity.textSize = 20f
            }
            tvCity.text = city.city
            tvOverdueHint.visibility =
                if (System.currentTimeMillis() - city.updateTime > 30 * 60 * 1000) View.VISIBLE else View.GONE
            ivWidgetMark.visibility = if (city.isWidgetCity()) View.VISIBLE else View.GONE
            if (!TextUtils.isEmpty(city.weatherNowJson)) {
                try {
                    val weatherNow =
                        GsonUtils.instance.fromJson(city.weatherNowJson, Now::class.java)
                    Glide.with(ivWeatherNow.context).load(getIcon(weatherNow.icon))
                        .into(ivWeatherNow)
                    tvWeatherNow.text = "${weatherNow.text}，${weatherNow.temp}℃"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}