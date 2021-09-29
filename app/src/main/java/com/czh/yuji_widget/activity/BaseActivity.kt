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

    fun statusBarTransparent(transparent: Boolean) {
        window.decorView.systemUiVisibility =
            if (transparent) View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = if (transparent) Color.TRANSPARENT else Color.WHITE
    }
}