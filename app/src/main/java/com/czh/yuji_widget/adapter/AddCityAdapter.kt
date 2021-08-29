package com.czh.yuji_widget.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.czh.yuji_widget.R

class AddCityAdapter(private val listener: OnItemClickListener<HeWeatherCity>) :
    RecyclerView.Adapter<AddCityAdapter.ViewHolder>() {

    private var cities: List<HeWeatherCity> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_add_city, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    fun setData(data: List<HeWeatherCity>) {
        val diffCallback = AddCityDiffCallback(cities, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        cities = data
    }

    class ViewHolder(itemView: View, private val listener: OnItemClickListener<HeWeatherCity>) :
        RecyclerView.ViewHolder(itemView) {
        private val tvCityName: TextView = itemView.findViewById(R.id.tv_city_name)
        private val tvCityParent: TextView = itemView.findViewById(R.id.tv_city_parent)
        private var currentHeWeatherCity: HeWeatherCity? = null

        init {
            itemView.setOnClickListener {
                currentHeWeatherCity?.let {
                    listener.onClick(it, itemView)
                }
            }
        }

        fun bind(heWeatherCity: HeWeatherCity) {
            this.currentHeWeatherCity = heWeatherCity
            tvCityName.text = heWeatherCity.name
            tvCityParent.text = heWeatherCity.parent
        }
    }
}

class AddCityDiffCallback(
    private val oldItems: List<HeWeatherCity>,
    private val newItems: List<HeWeatherCity>
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
        val oldItem: HeWeatherCity = oldItems[oldItemPosition]
        val newItem: HeWeatherCity = newItems[newItemPosition]
        return oldItem.name == newItem.name
                && oldItem.parent == newItem.parent
                && oldItem.lat == newItem.lat
                && oldItem.lon == newItem.lon
    }
}

data class HeWeatherCity(
    val id: String,
    val name: String,
    val parent: String,
    val lat: String,
    val lon: String
)