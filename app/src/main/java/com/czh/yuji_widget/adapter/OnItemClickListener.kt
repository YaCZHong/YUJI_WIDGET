package com.czh.yuji_widget.adapter

import android.view.View

interface OnItemClickListener<T> {
    fun onClick(t: T, view: View)
    fun onLongClick(t: T, view: View)
}