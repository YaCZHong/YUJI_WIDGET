package com.czh.yuji_widget.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.czh.yuji_widget.R
import com.czh.yuji_widget.adapter.*
import com.czh.yuji_widget.databinding.ActivityMainBinding
import com.czh.yuji_widget.db.AppDatabase
import com.czh.yuji_widget.db.City
import com.czh.yuji_widget.util.VibratorUtils
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
                VibratorUtils.shortVibrate(this@MainActivity)
                showPopupWindow(t)
            }
        })
        binding.rv.adapter = mAdapter
        binding.rv.addItemDecoration(ItemDecoration(dp2px(8)))
    }

    private fun initData() {
        lifecycleScope.launch {
            AppDatabase.getInstance().cityDao().cities().observe(this@MainActivity, Observer {
                binding.fab.visibility = if (it.size >= 5) View.GONE else View.VISIBLE
                mAdapter.setData(it)
                it.forEach { item ->
                    updateCity(item)
                }
            })
        }
    }

    private fun updateCity(city: City) {
        if (System.currentTimeMillis() - city.updateTime > 10 * 60 * 1000) {
            vm.getWeather(city)
        }
    }

    private fun showPopupWindow(city: City) {
        val popupWindow = PopupWindow(this)
        val contentView: View = layoutInflater.inflate(R.layout.popupwindow, null)
        contentView.findViewById<TextView>(R.id.tv_widget).apply {
            text = "设为小部件城市"
            setOnClickListener {
                popupWindow.dismiss()
            }
        }
        contentView.findViewById<TextView>(R.id.tv_delete).apply {
            text = "删除"
            setOnClickListener {
                vm.deleteCity(city)
                popupWindow.dismiss()
            }
        }
        popupWindow.contentView = contentView
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = false
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
    }
}