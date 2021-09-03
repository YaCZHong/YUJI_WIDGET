package com.czh.yuji_widget.util

import java.text.SimpleDateFormat
import java.util.*

fun parseTime(time: Long): String {
    var datetime = time
    var sysdateTime = System.currentTimeMillis()
    var subTime = sysdateTime - datetime
    when {
        subTime < 60000 -> {
            return "刚刚"
        }
        subTime < 3600000 -> {
            return "${(subTime / 60000).toInt()}分钟前"
        }
        subTime > 3600000 -> {
            var sysdate = Date(sysdateTime)
            val calendar = Calendar.getInstance()
            calendar.time = Date(time)
            val sysCalendar = Calendar.getInstance()
            sysCalendar.time = sysdate

            if (sysCalendar.get(Calendar.YEAR) > calendar.get(Calendar.YEAR)) {
                var format = "yyyy/MM/dd"
                var formatter = SimpleDateFormat(format)
                return formatter.format(time)
            } else if (sysCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) > 1) {
                var format = "MM/dd"
                var formatter = SimpleDateFormat(format)
                return formatter.format(time)
            } else if (sysCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) == 1) {
                var format = "HH:mm"
                var formatter = SimpleDateFormat(format)
                return "昨天 ${formatter.format(time)}"
            } else if (sysCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) {
                return "${(subTime / 3600000).toInt()}小时前"
            }
        }
    }
    var formatter = SimpleDateFormat("yyyy/MM/dd")
    return formatter.format(time)
}

/**
 * 获取某个日期是星期几
 */
fun getWeek(year: Int, month: Int, day: Int): String {
    val calendar = Calendar.getInstance().apply {
        set(year, month - 1, day)
    }
    return when (calendar[Calendar.DAY_OF_WEEK] - 1) {
        1 -> "星期一"
        2 -> "星期二"
        3 -> "星期三"
        4 -> "星期四"
        5 -> "星期五"
        6 -> "星期六"
        else -> "星期日"
    }
}