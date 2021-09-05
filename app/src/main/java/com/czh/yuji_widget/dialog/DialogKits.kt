package com.czh.yuji_widget.dialog

import android.app.Activity
import androidx.appcompat.app.AlertDialog

fun showConfirmCancelDialog(
    activity: Activity,
    title: String = "tip",
    msg: String,
    confirmText: String = "确定",
    cancelText: String = "取消",
    confirmClick: (() -> Unit)? = null
) {
    AlertDialog.Builder(activity)
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton(confirmText) { dialog, _ ->
            dialog.dismiss()
            confirmClick?.invoke()
        }
        .setNegativeButton(cancelText) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}

fun showConfirmDialog(
    activity: Activity,
    title: String = "tip",
    msg: String,
    confirmText: String = "确定",
    confirmClick: (() -> Unit)? = null
) {
    AlertDialog.Builder(activity)
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton(confirmText) { dialog, _ ->
            dialog.dismiss()
            confirmClick?.invoke()
        }
        .create()
        .show()
}