package com.czh.yuji_widget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.czh.yuji_widget.R
import com.czh.yuji_widget.db.City

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
        private val tvCity: TextView = itemView.findViewById(R.id.tv_city)
        private val tvWeatherNow: TextView = itemView.findViewById(R.id.tv_weather_now)
        private val tvWeather7D: TextView = itemView.findViewById(R.id.tv_weather_7d)
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
            tvCity.text = city.city
            tvWeatherNow.text = city.weatherNowJson
            tvWeather7D.text = city.weatherDailyJson
        }
    }
}