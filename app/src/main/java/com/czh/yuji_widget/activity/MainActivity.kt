package com.czh.yuji_widget.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.czh.yuji_widget.adapter.*
import com.czh.yuji_widget.databinding.ActivityMainBinding
import com.czh.yuji_widget.db.AppDatabase
import com.czh.yuji_widget.db.City
import com.czh.yuji_widget.util.dp2px
import com.czh.yuji_widget.vm.MainVM
import kotlinx.coroutines.launch

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
        mAdapter = MainCityAdapter(object : OnItemClickListener<City> {
            override fun onClick(t: City, view: View) {

            }

            override fun onLongClick(t: City, view: View) {
                // 暂不实现
            }
        })
        binding.rv.adapter = mAdapter
        binding.rv.addItemDecoration(ItemDecoration(dp2px(8)))
    }

    private fun initData() {
        lifecycleScope.launch {
            AppDatabase.getInstance().cityDao().cities().observe(this@MainActivity, Observer {
                mAdapter.setData(it)
                binding.fab.visibility = if (it.size > 5) View.GONE else View.VISIBLE
                it.forEach { item ->
                    updateCity(item)
                }
            })
        }
    }

    private fun updateCity(city: City) {
        if (TextUtils.isEmpty(city.weatherNowJson)) {
            vm.getWeatherNow(city)
        }
        if (TextUtils.isEmpty(city.weatherDailyJson)) {
            vm.getWeather7D(city)
        }
    }
}