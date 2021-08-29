package com.czh.yuji_widget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.czh.yuji_widget.R

class MainCityAdapter(private val listener: OnItemClickListener<MyWeatherCity>) :
    RecyclerView.Adapter<MainCityAdapter.ViewHolder>() {

    private var cities: List<MyWeatherCity> = emptyList()

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

    fun setData(data: List<MyWeatherCity>) {
//        val diffCallback = MainCityDiffCallback(cities, data)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        diffResult.dispatchUpdatesTo(this)
//        cities = data
        cities = data
        notifyDataSetChanged()
    }

    class ViewHolder(view: View, listener: OnItemClickListener<MyWeatherCity>) :
        RecyclerView.ViewHolder(view) {
        private val tvCity: TextView = itemView.findViewById(R.id.tv_city)
        private val tvWeatherNow: TextView = itemView.findViewById(R.id.tv_weather_now)
        private val tvWeatherDaily: TextView = itemView.findViewById(R.id.tv_weather_daily)
        private var currentCity: MyWeatherCity? = null

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

        fun bind(city: MyWeatherCity) {
            currentCity = city
            tvCity.text = city.city
            tvWeatherNow.text = city.weatherNowJson
            tvWeatherDaily.text = city.weatherDailyJson
        }
    }
}

class MainCityDiffCallback(
    private val oldItems: List<MyWeatherCity>,
    private val newItems: List<MyWeatherCity>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].city == newItems[newItemPosition].city
                && oldItems[oldItemPosition].lat == newItems[newItemPosition].lat
                && oldItems[oldItemPosition].lon == newItems[newItemPosition].lon
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: MyWeatherCity = oldItems[oldItemPosition]
        val newItem: MyWeatherCity = newItems[newItemPosition]
        return oldItem.weatherNowJson == newItem.weatherNowJson
                && oldItem.weatherDailyJson == newItem.weatherDailyJson
                && oldItem.updateTime == newItem.updateTime
                && oldItem.updateState == newItem.updateState
                && oldItem.isWidget == newItem.isWidget
    }
}

data class MyWeatherCity(
    var city: String,
    var lat: String,
    var lon: String,
    var weatherNowJson: String = "",
    var weatherDailyJson: String = "",
    var updateTime: Long = 0,
    var updateState: Int = 0, // 0 更新失败，1 更新中，2 更新成功
    var isWidget: Int = 0
)
