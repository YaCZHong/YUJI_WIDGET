package com.czh.yuji_widget.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(private val size: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = size
        outRect.right = size
        outRect.bottom = size
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = size
        }
    }
}