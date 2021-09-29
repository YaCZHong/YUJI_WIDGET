package com.czh.yuji_widget.activity

import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.czh.yuji_widget.dialog.LoadingDialog

open class BaseActivity : AppCompatActivity() {
    private var loading: LoadingDialog? = null

    fun showLoading(msg: String) {
        loading?.dismiss()
        LoadingDialog(msg).also { loading = it }.show(supportFragmentManager, "")
    }

    fun hideLoading() {
        loading?.dismiss()
    }
}