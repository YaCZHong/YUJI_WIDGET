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
     * @param pattern 震动规则
     * @param repeat 循环次数
     */
    fun startVibrator(pattern: LongArray, repeat: Int) {
        vibrator.cancel()
        vibrator.vibrate(pattern, repeat)
    }

    /**
     * 关闭震动
     */
    fun cancelVibrator() {
        vibrator.cancel()
    }

    /**
     * 短震动
     */
    fun shortVibrate() {
        vibrator.cancel()
        vibrator.vibrate(20)
    }

    /**
     * 长震动
     */
    fun longVibrate() {
        vibrator.cancel()
        vibrator.vibrate(500)
    }
}



