package com.czh.yuji_widget.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDecoration(private val horizontal: Int, private val vertical: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = horizontal
        outRect.right = horizontal
        outRect.bottom = vertical
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = vertical
        }
    }
}