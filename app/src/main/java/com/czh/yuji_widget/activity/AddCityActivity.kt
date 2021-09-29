package com.czh.yuji_widget.activity

import android.Manifest
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.os.Build
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationClient
import androidx.core.app.ActivityCompat
import com.czh.yuji_widget.dialog.showConfirmCancelDialog
import com.czh.yuji_widget.dialog.showConfirmDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.inputmethod.EditorInfo
import com.czh.yuji_widget.R
import com.gyf.immersionbar.ImmersionBar

/**
 * @Description: 添加城市
 * @Author: czh
 * @CreateDate: 2021/8/29 16:22
 */
class AddCityActivity : BaseActivity() {

    companion object {
        const val REQUEST_LOCATION_CODE = 10000
    }

    private lateinit var binding: ActivityAddCityBinding
    private val vm by viewModels<AddCityVM>()
    private lateinit var mAdapter: AddCityAdapter

    private var mLocationClient: AMapLocationClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLocation()
        initView()
        initData()
    }

    private fun initLocation() {
        val mLocationOption = AMapLocationClientOption().apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            isOnceLocation = true
            isOnceLocationLatest = true
        }

        mLocationClient = AMapLocationClient(this).apply {
            setLocationOption(mLocationOption)
            setLocationListener { location ->
                stopLocate()
                if (location.errorCode == 0) {
                    showConfirmCancelDialog(
                        this@AddCityActivity,
                        msg = "是否添加位置：${location.description}？",
                        confirmClick = {
                            saveCity(
                                location.description,
                                location.latitude.toString(),
                                location.longitude.toString()
                            )
                        })
                } else {
                    showConfirmDialog(this@AddCityActivity, msg = location.locationDetail)
                }
            }
        }
    }

    private fun initView() {
        ImmersionBar.with(this)
            .fitsSystemWindows(true)
            .statusBarColor(R.color.color_FFF9F9F9)
            .statusBarDarkFont(true)
            .init()
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mAdapter = AddCityAdapter(object : OnItemClickListener<Location> {
            override fun onClick(t: Location, view: View) {
                saveCity(t.name, t.lat, t.lon)
            }

            override fun onLongClick(t: Location, view: View) {
                // 暂不实现
            }
        })
        binding.apply {
            rv.adapter = mAdapter
            rv.addItemDecoration(ItemDecoration(dp2px(16), dp2px(12)))

            etSearch.setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    vm.getCities(v.text.toString())
                }
                true
            }
            ivLocate.setOnClickListener {
                toLocate()
            }
        }
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
            data?.let {
                if (it.isEmpty()) {
                    changeContentVisible(true)
                } else {
                    mAdapter.setData(it)
                    changeContentVisible(false)
                }
            } ?: changeContentVisible(true)
        })

//        binding.searchView.postDelayed({ vm.getCities("潮州") }, 400)
    }

    private fun changeContentVisible(isNoData: Boolean) {
        binding.tvAddHint.visibility = if (isNoData) View.VISIBLE else View.GONE
        binding.rv.visibility = if (isNoData) View.GONE else View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationClient?.onDestroy()
        mLocationClient = null
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocate()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    showConfirmCancelDialog(
                        this,
                        title = "权限缺失",
                        msg = "获取当前位置需要定位权限，是否确定授权？",
                        confirmClick = { requestLocationPermission() })
                } else {
                    showConfirmCancelDialog(
                        this,
                        title = "权限缺失",
                        msg = "获取当前位置需要定位权限，是否前往设置页面开启？",
                        confirmClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .apply { data = Uri.fromParts("package", packageName, null) }
                            startActivity(intent)
                        })
                }
            }
        }
    }

    private fun toLocate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startLocate()
            } else {
                requestLocationPermission()
            }
        } else {
            startLocate()
        }
    }

    private fun startLocate() {
        mLocationClient?.let {
            showLoading("定位中...")
            it.startLocation()
        }
    }

    private fun stopLocate() {
        mLocationClient?.let {
            it.stopLocation()
            hideLoading()
        }
    }

    private fun saveCity(cityName: String, lat: String, lon: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val cities = AppDatabase.getInstance().cityDao().getCities()
            val city = City(
                city = cityName,
                lat = lat,
                lon = lon,
                isWidget = if (cities.isEmpty()) 1 else 0
            )
            AppDatabase.getInstance().cityDao().addCity(city)
            finish()
        }
    }
}