package com.czh.yuji_widget.util

import com.czh.yuji_widget.R

object WeatherIconUtils {

    fun getIcon(code: String): Int {
        return when (code) {
            "100" -> R.drawable.icon_100
            "101" -> R.drawable.icon_101
            "102" -> R.drawable.icon_102
            "103" -> R.drawable.icon_103
            "104" -> R.drawable.icon_104
            "150" -> R.drawable.icon_150
            "153" -> R.drawable.icon_153
            "154" -> R.drawable.icon_154

            "300" -> R.drawable.icon_300
            "301" -> R.drawable.icon_301
            "302" -> R.drawable.icon_302
            "303" -> R.drawable.icon_303
            "304" -> R.drawable.icon_304
            "305" -> R.drawable.icon_305
            "306" -> R.drawable.icon_306
            "307" -> R.drawable.icon_307
            "308" -> R.drawable.icon_308
            "309" -> R.drawable.icon_309
            "310" -> R.drawable.icon_310
            "311" -> R.drawable.icon_311
            "312" -> R.drawable.icon_312
            "313" -> R.drawable.icon_313
            "314" -> R.drawable.icon_314
            "315" -> R.drawable.icon_315
            "316" -> R.drawable.icon_316
            "317" -> R.drawable.icon_317
            "318" -> R.drawable.icon_318
            "399" -> R.drawable.icon_399
            "350" -> R.drawable.icon_350
            "351" -> R.drawable.icon_351

            "400" -> R.drawable.icon_400
            "401" -> R.drawable.icon_401
            "402" -> R.drawable.icon_402
            "403" -> R.drawable.icon_403
            "404" -> R.drawable.icon_404
            "405" -> R.drawable.icon_405
            "406" -> R.drawable.icon_406
            "407" -> R.drawable.icon_407
            "408" -> R.drawable.icon_408
            "409" -> R.drawable.icon_409
            "410" -> R.drawable.icon_410
            "499" -> R.drawable.icon_499
            "456" -> R.drawable.icon_456
            "457" -> R.drawable.icon_457

            "500" -> R.drawable.icon_500
            "501" -> R.drawable.icon_501
            "502" -> R.drawable.icon_502
            "503" -> R.drawable.icon_503
            "504" -> R.drawable.icon_504
            "507" -> R.drawable.icon_507
            "508" -> R.drawable.icon_508
            "509" -> R.drawable.icon_509
            "510" -> R.drawable.icon_510
            "511" -> R.drawable.icon_511
            "512" -> R.drawable.icon_512
            "513" -> R.drawable.icon_513
            "514" -> R.drawable.icon_514
            "515" -> R.drawable.icon_515
            "900" -> R.drawable.icon_900
            "901" -> R.drawable.icon_901
            "999" -> R.drawable.icon_999

            else -> R.drawable.icon_100
        }
    }
}