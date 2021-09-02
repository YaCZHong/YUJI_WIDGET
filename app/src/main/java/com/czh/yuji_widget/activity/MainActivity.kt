package com.czh.yuji_widget.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.czh.yuji_widget.R
import com.czh.yuji_widget.adapter.*
import com.czh.yuji_widget.databinding.ActivityMainBinding
import com.czh.yuji_widget.db.AppDatabase
import com.czh.yuji_widget.db.City
import com.czh.yuji_widget.dialog.MainBottomSheetDialogFragment
import com.czh.yuji_widget.util.VibratorUtils
import com.czh.yuji_widget.util.dp2px
import com.czh.yuji_widget.util.toast.toast
import com.czh.yuji_widget.vm.MainVM
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm by viewModels<MainVM>()
    private lateinit var mAdapter: MainCityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddCityActivity::class.java))
        }
        mAdapter = MainCityAdapter(object : OnItemClickListener<City> {
            override fun onClick(t: City, view: View) {
                updateCity(t, false)
            }

            override fun onLongClick(t: City, view: View) {
                showBottomSheet(t)
            }
        })
        binding.rv.adapter = mAdapter
        binding.rv.addItemDecoration(ItemDecoration(dp2px(8)))

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
                it.forEach { item ->
                    updateCity(item)
                }
            })
        }
    }

    private fun updateCity(city: City, timeLimit: Boolean = true) {
        if (timeLimit && (System.currentTimeMillis() - city.updateTime < 10 * 60 * 1000)) {
            return
        }
        vm.getWeather(city)
    }

    private fun showBottomSheet(city: City) {
//        val mBottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogStyle)
//        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)
//        view.findViewById<TextView>(R.id.tv_widget).setOnClickListener {
//            vm.makeCityToWidget(city)
//            mBottomSheetDialog.dismiss()
//        }
//        view.findViewById<TextView>(R.id.tv_delete).setOnClickListener {
//            vm.deleteCity(city)
//            mBottomSheetDialog.dismiss()
//        }
//        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
//            mBottomSheetDialog.dismiss()
//        }
//        mBottomSheetDialog.setContentView(view)
//        mBottomSheetDialog.window?.apply {
//            findViewById<FrameLayout>(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT)
//        }
//
//        mBottomSheetDialog.show()
        MainBottomSheetDialogFragment(city).show(supportFragmentManager,"")
        VibratorUtils.shortVibrate(this)
    }
}