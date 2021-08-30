package com.czh.yuji_widget.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.czh.yuji_widget.R
import com.czh.yuji_widget.http.response.Location

class AddCityAdapter(private val listener: OnItemClickListener<Location>) :
    RecyclerView.Adapter<AddCityAdapter.ViewHolder>() {

    private var cities: List<Location> = emptyList()

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

    fun setData(data: List<Location>) {
        val diffCallback = LocationDiffCallback(cities, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
        cities = data
    }

    class ViewHolder(itemView: View, private val listener: OnItemClickListener<Location>) :
        RecyclerView.ViewHolder(itemView) {
        private val tvCityName: TextView = itemView.findViewById(R.id.tv_city_name)
        private val tvCityParent: TextView = itemView.findViewById(R.id.tv_city_parent)
        private var currentCity: Location? = null

        init {
            itemView.setOnClickListener {
                currentCity?.let {
                    listener.onClick(it, itemView)
                }
            }
        }

        fun bind(city: Location) {
            this.currentCity = city
            tvCityName.text = city.name
            tvCityParent.text = "${city.country}--${city.adm1}--${city.adm2}"
        }
    }
}