package com.czh.yuji_widget.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.czh.yuji_widget.R
import com.czh.yuji_widget.adapter.AddCityAdapter
import com.czh.yuji_widget.adapter.ItemDecoration
import com.czh.yuji_widget.adapter.OnItemClickListener
import com.czh.yuji_widget.databinding.ActivityAddCityBinding
import com.czh.yuji_widget.db.AppDatabase
import com.czh.yuji_widget.db.City
import com.czh.yuji_widget.http.response.Location
import com.czh.yuji_widget.util.dp2px
import com.czh.yuji_widget.util.toast.toast
import com.czh.yuji_widget.vm.AddCityVM
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @Description: 添加城市
 * @Author: czh
 * @CreateDate: 2021/8/29 16:22
 */
class AddCityActivity : BaseActivity() {

    private lateinit var binding: ActivityAddCityBinding
    private val vm by viewModels<AddCityVM>()
    private lateinit var mAdapter: AddCityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initData()
    }

    private fun initView() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mAdapter = AddCityAdapter(object : OnItemClickListener<Location> {
            override fun onClick(t: Location, view: View) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val cities = AppDatabase.getInstance().cityDao().getCities()
                    val city = City(
                        city = t.name,
                        lat = t.lat,
                        lon = t.lon,
                        isWidget = if (cities.isEmpty()) 1 else 0
                    )
                    AppDatabase.getInstance().cityDao().addCity(city)
                    finish()
                }
            }

            override fun onLongClick(t: Location, view: View) {
                // 暂不实现
            }
        })
        binding.rv.adapter = mAdapter
        binding.rv.addItemDecoration(ItemDecoration(dp2px(8)))

        binding.searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                vm.getCities(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun initData() {
        vm.toastHintLiveData.observe(this, Observer { toastHint ->
            toastHint?.let { toast(it) }
        })
        vm.loadingLiveData.observe(this, Observer { show ->
            if (show) {
                showLoading("正在搜索中...")
            } else {
                hideLoading()
            }
        })
        vm.cities.observe(this, Observer { data ->
            data?.let { mAdapter.setData(it) }
        })

        binding.searchView.postDelayed({ vm.getCities("潮州") }, 400)
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
}