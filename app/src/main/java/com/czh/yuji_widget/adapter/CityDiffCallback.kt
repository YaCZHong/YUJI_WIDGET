package com.czh.yuji_widget.adapter

import androidx.recyclerview.widget.DiffUtil
import com.czh.yuji_widget.db.City

class CityDiffCallback(
    private val oldItems: List<City>,
    private val newItems: List<City>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].uid == newItems[newItemPosition].uid
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: City = oldItems[oldItemPosition]
        val newItem: City = newItems[newItemPosition]
        return oldItem.city == newItem.city
                && oldItem.lat == newItem.lat
                && oldItem.lon == newItem.lon
                && oldItem.weatherNowJson == newItem.weatherNowJson
                && oldItem.weatherDailyJson == newItem.weatherDailyJson
                && oldItem.updateTime == newItem.updateTime
                && oldItem.updateState == newItem.updateState
                && oldItem.isWidget == newItem.isWidget
    }
}