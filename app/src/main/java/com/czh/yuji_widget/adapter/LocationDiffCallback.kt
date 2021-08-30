package com.czh.yuji_widget.adapter

import androidx.recyclerview.widget.DiffUtil
import com.czh.yuji_widget.http.response.Location

class LocationDiffCallback(
    private val oldItems: List<Location>,
    private val newItems: List<Location>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem: Location = oldItems[oldItemPosition]
        val newItem: Location = newItems[newItemPosition]
        return oldItem.adm1 == newItem.adm1
                && oldItem.adm2 == newItem.adm2
                && oldItem.country == newItem.country
                && oldItem.fxLink == newItem.fxLink
                && oldItem.isDst == newItem.isDst
                && oldItem.lat == newItem.lat
                && oldItem.lon == newItem.lon
                && oldItem.name == newItem.name
                && oldItem.rank == newItem.rank
                && oldItem.type == newItem.type
                && oldItem.tz == newItem.tz
                && oldItem.utcOffset == newItem.utcOffset
    }
}