package com.czh.yuji_widget.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.czh.yuji_widget.R
import com.czh.yuji_widget.adapter.*
import com.czh.yuji_widget.databinding.ActivityMainBinding
import com.czh.yuji_widget.db.AppDatabase
import com.czh.yuji_widget.db.City
import com.czh.yuji_widget.util.VibratorUtils
import com.czh.yuji_widget.util.dp2px
import com.czh.yuji_widget.util.toast.toast
import com.czh.yuji_widget.vm.MainVM
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm by viewModels<MainVM>()
    private lateinit var mAdapter: MainCityAdapter
    private val mAnimator = ViewPager2.PageTransformer { page, position ->
        val absPos = abs(position)
        page.apply {
//            val scale = if (absPos > 1) 0F else 1 - 0.1f * absPos
            val scale = if (absPos <= 1) {
                1 - 0.1f * absPos
            } else if (absPos > 1 && absPos < 2) {
                0.9f - 0.1f * (absPos - 1)
            } else {
                0f
            }
            scaleX = scale
            scaleY = scale
        }
    }

    private var lastCityCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.fab.setOnClickListener {
            val intent = Intent(this, AddCityActivity::class.java)
            startActivity(intent)
        }
        mAdapter = MainCityAdapter(object : OnItemClickListener<City> {
            override fun onClick(t: City, view: View) {
                updateCity(t, false)
            }

            override fun onLongClick(t: City, view: View) {
                showBottomSheet(t)
            }
        })

        binding.vp.apply {
            offscreenPageLimit = 1
            val recyclerView = getChildAt(0) as RecyclerView
            recyclerView.apply {
                val padding = dp2px(36)
                setPadding(padding, 0, padding, 0)
                clipToPadding = false
                overScrollMode = View.OVER_SCROLL_NEVER
            }
            adapter = mAdapter
            setPageTransformer(mAnimator)
        }

        vm.poemLiveData.observe(this, Observer {
            binding.tvPoem.text = it
        })
        vm.toastHintLiveData.observe(this, Observer {
            toast(it)
        })
        vm.loadingLiveData.observe(this, Observer { show ->
            if (show) {
                showLoading("正在获取数据...")
            } else {
                hideLoading()
            }
        })

        lifecycleScope.launch {
            AppDatabase.getInstance().cityDao().cities().observe(this@MainActivity, Observer {
                binding.fab.visibility = if (it.size >= 5) View.GONE else View.VISIBLE
                mAdapter.setData(it)

                // 如果有城市增删，则跳回第一个城市
                if (lastCityCount != it.size) {
                    lastCityCount = it.size
                    binding.vp.post {
                        binding.vp.currentItem = 0
                    }
                }

                // 检测更新天气数据
                it.forEach { item ->
                    updateCity(item.copy())
                }
            })
        }

        vm.getPoem()
    }

    private fun updateCity(city: City, timeLimit: Boolean = true) {
        if (timeLimit && (System.currentTimeMillis() - city.updateTime < 10 * 60 * 1000)) {
            return
        }
        vm.getWeather(city)
    }

    private fun showBottomSheet(city: City) {
        val mBottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)
        view.findViewById<TextView>(R.id.tv_widget).setOnClickListener {
            vm.makeCityToWidget(city.copy())
            mBottomSheetDialog.dismiss()
        }
        view.findViewById<TextView>(R.id.tv_delete).setOnClickListener {
            vm.deleteCity(city)
            mBottomSheetDialog.dismiss()
        }
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            mBottomSheetDialog.dismiss()
        }
        mBottomSheetDialog.setContentView(view)
        mBottomSheetDialog.show()
        VibratorUtils.shortVibrate(this)
    }
}