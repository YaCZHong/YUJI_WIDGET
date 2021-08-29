package com.czh.yuji_widget.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.czh.yuji_widget.R
import com.czh.yuji_widget.adapter.AddCityAdapter
import com.czh.yuji_widget.adapter.ItemDecoration
import com.czh.yuji_widget.adapter.HeWeatherCity
import com.czh.yuji_widget.adapter.OnItemClickListener
import com.czh.yuji_widget.databinding.ActivityAddCityBinding
import com.czh.yuji_widget.db.AppDatabase
import com.czh.yuji_widget.db.City
import com.czh.yuji_widget.dialog.LoadingDialog
import com.czh.yuji_widget.util.dp2px
import com.czh.yuji_widget.util.toast.toast
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.qweather.sdk.bean.base.Code
import com.qweather.sdk.bean.geo.GeoBean
import com.qweather.sdk.view.QWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @Description: 添加城市
 * @Author: czh
 * @CreateDate: 2021/8/29 16:22
 */
class AddCityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCityBinding
    private lateinit var mAdapter: AddCityAdapter
    private var loading: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mAdapter = AddCityAdapter(object : OnItemClickListener<HeWeatherCity> {
            override fun onClick(t: HeWeatherCity, view: View) {
                val city = City(city = t.name, lat = t.lat, lon = t.lon)
                lifecycleScope.launch(Dispatchers.IO) {
                    AppDatabase.getInstance().cityDao().addCity(city)
                    finish()
                }
            }

            override fun onLongClick(t: HeWeatherCity, view: View) {
                // 暂不实现
            }
        })
        binding.rv.adapter = mAdapter
        binding.rv.addItemDecoration(ItemDecoration(dp2px(8)))

        binding.searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.searchView.closeSearch()
                getCities(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        binding.searchView.postDelayed({ getCities("潮州") }, 400)
    }

    private fun getCities(query: String) {
        LoadingDialog(msg = "正在搜索中").also { loading = it }.show(supportFragmentManager, "")

        QWeather.getGeoCityLookup(
            this@AddCityActivity,
            query,
            object : QWeather.OnResultGeoListener {
                override fun onError(p0: Throwable?) {
                    loading?.dismiss()
                    toast("搜索失败")
                }

                override fun onSuccess(data: GeoBean?) {
                    loading?.dismiss()
                    data?.let {
                        if (it.code == Code.OK) {
                            val list = mutableListOf<HeWeatherCity>()
                            it.locationBean.forEach { bean ->
                                val heWeatherCity = HeWeatherCity(
                                    bean.id,
                                    bean.name,
                                    "${bean.country}--${bean.adm1}--${bean.adm2}",
                                    bean.lat,
                                    bean.lon
                                )
                                list.add(heWeatherCity)
                            }
                            mAdapter.setData(list)
                        } else {
                            toast("搜索失败 error code:${it.code}")
                        }
                    }
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_city, menu)
        menu?.findItem(R.id.action_search)?.also {
            binding.searchView.setMenuItem(it)
        }
        return true
    }

    override fun onBackPressed() {
        if (binding.searchView.isSearchOpen) {
            binding.searchView.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loading?.dismiss()
    }
}