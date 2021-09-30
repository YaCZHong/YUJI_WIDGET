package com.czh.yuji_widget.util

import android.content.Context
import android.content.SharedPreferences
import com.czh.yuji_widget.config.AppConfig

object SpUtils {
    private val sp: SharedPreferences by lazy {
        AppConfig.mContext.getSharedPreferences("YUJI_WIDGET", Context.MODE_PRIVATE)
    }

    fun put(key: String, value: String?, isCommit: Boolean = false) {
        if (isCommit) {
            sp.edit().putString(key, value).commit()
        } else {
            sp.edit().putString(key, value).apply()
        }
    }

    fun getString(key: String, defaultValue: String? = null): String? {
        return sp.getString(key, defaultValue)
    }

    fun put(key: String, value: Int, isCommit: Boolean = false) {
        if (isCommit) {
            sp.edit().putInt(key, value).commit()
        } else {
            sp.edit().putInt(key, value).apply()
        }
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sp.getInt(key, defaultValue)
    }

    fun put(key: String, value: Boolean, isCommit: Boolean = false) {
        if (isCommit) {
            sp.edit().putBoolean(key, value).commit()
        } else {
            sp.edit().putBoolean(key, value).apply()
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sp.getBoolean(key, defaultValue)
    }
}