package com.czh.yuji_widget.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CITY")
data class City(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val uid: Long = 0,
    @ColumnInfo var city: String,
    @ColumnInfo var lat: String,
    @ColumnInfo var lon: String,
//    @ColumnInfo var weatherNowJson: String = "",
//    @ColumnInfo var weatherDailyJson: String = "",
//    @ColumnInfo var updateTime: Long = 0,
//    @ColumnInfo var updateState: Int = 0, // 0 更新失败，1 更新中，2 更新成功
    @ColumnInfo var isWidget: Int = 0
)