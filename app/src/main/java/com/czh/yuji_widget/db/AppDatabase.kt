package com.czh.yuji_widget.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.czh.yuji_widget.config.AppConfig

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object {

        private var sInstance: AppDatabase? = null

        fun getInstance(): AppDatabase {
            return sInstance ?: synchronized(AppDatabase::class.java) {
                sInstance ?: Room.databaseBuilder(
                    AppConfig.mContext,
                    AppDatabase::class.java,
                    "yuji_widget.db"
                ).build().also { sInstance = it }
            }
        }
    }
}