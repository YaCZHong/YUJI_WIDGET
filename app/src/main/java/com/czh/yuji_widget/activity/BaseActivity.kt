package com.czh.yuji_widget.activity

import androidx.appcompat.app.AppCompatActivity
import com.czh.yuji_widget.dialog.LoadingDialog

abstract class BaseActivity : AppCompatActivity() {

    private var loading: LoadingDialog? = null

    fun showLoading(msg: String) {
        loading?.dismiss()
        LoadingDialog(msg).also { loading = it }.show(supportFragmentManager, "")
    }

    fun hideLoading() {
        loading?.dismiss()
    }
}