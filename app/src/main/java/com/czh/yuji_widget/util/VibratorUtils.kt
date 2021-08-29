package com.czh.yuji_widget.util

import android.content.Context
import android.os.Vibrator
import com.czh.yuji_widget.config.AppConfig

/**
 * 手机震动相关工具
 */
object VibratorUtils {

    private val vibrator = AppConfig.mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    /**
     * 开始震动
     * @param context
     * @param pattern 震动规则
     * @param repeat 循环次数
     */
    fun startVibrator(context: Context, pattern: LongArray, repeat: Int) {
        vibrator.cancel()
        vibrator.vibrate(pattern, repeat)
    }

    /**
     * 关闭震动
     *
     * @param context
     */
    fun cancelVibrator(context: Context) {
        vibrator.cancel()
    }

    /**
     * 短震动
     */
    fun shortVibrate(context: Context) {
        vibrator.cancel()
        vibrator.vibrate(1)
    }

    /**
     * 长震动
     */
    fun longVibrate(context: Context) {
        vibrator.cancel()
        vibrator.vibrate(500)
    }
}



