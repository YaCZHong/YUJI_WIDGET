package com.czh.yuji_widget.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.czh.yuji_widget.adapter.*
import com.czh.yuji_widget.databinding.ActivityMainBinding
import com.czh.yuji_widget.db.AppDatabase
import com.czh.yuji_widget.util.GsonUtils
import com.czh.yuji_widget.util.dp2px
import com.czh.yuji_widget.vm.MainVM
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.qweather.sdk.view.QWeather

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm by viewModels<MainVM>()
    private lateinit var mAdapter: MainCityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initData()
    }

    private fun initView() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddCityActivity::class.java))
        }
        mAdapter = MainCityAdapter(object : OnItemClickListener<MyWeatherCity> {
            override fun onClick(t: MyWeatherCity, view: View) {

            }

            override fun onLongClick(t: MyWeatherCity, view: View) {
                // 暂不实现
            }
        })
        binding.rv.adapter = mAdapter
        binding.rv.addItemDecoration(ItemDecoration(dp2px(8)))
    }

    private fun initData() {
        vm.cities.observe(this, Observer {
            mAdapter.setData(it)
        })
        AppDatabase.getInstance().cityDao().getCities().observe(this, Observer { cities ->
            vm.setCities(cities.map { item ->
                MyWeatherCity(item.city, item.lat, item.lon).also {
//                    updateCity(it)
                }
            })
        })
    }

    private fun updateCity(city: MyWeatherCity) {
        if (TextUtils.isEmpty(city.weatherNowJson)) {
            QWeather.getWeatherNow(
                this,
                "${city.lon},${city.lat}",
                object : QWeather.OnResultWeatherNowListener {
                    override fun onError(p0: Throwable?) {
                        Log.d("czh", "")
                    }

                    override fun onSuccess(data: WeatherNowBean?) {
                        data?.let {
                            city.weatherNowJson = GsonUtils.instance.toJson(it.now)
                            vm.notifyUpdate()
                        }
                    }
                })
        }

        if (TextUtils.isEmpty(city.weatherDailyJson)) {
            QWeather.getWeather7D(
                this,
                "${city.lon},${city.lat}",
                object : QWeather.OnResultWeatherDailyListener {
                    override fun onError(p0: Throwable?) {
                        Log.d("czh", "")
                    }

                    override fun onSuccess(data: WeatherDailyBean?) {
                        data?.let {
                            city.weatherDailyJson = GsonUtils.instance.toJson(it.daily)
                            vm.notifyUpdate()
                        }
                    }
                })
        }
    }
}