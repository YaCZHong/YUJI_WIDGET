package com.czh.yuji_widget.util

object Persistent {

    var poemToken: String?
        get() = SpUtils.getString("poemToken", null)
        set(value) {
            SpUtils.put("poemToken", value)
        }
}