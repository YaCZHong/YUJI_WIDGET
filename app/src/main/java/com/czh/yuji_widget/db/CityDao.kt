package com.czh.yuji_widget.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {
    @Query("select * from city")
    suspend fun getCities(): List<City>

    @Query("select * from city")
    fun cities(): LiveData<List<City>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCity(city: City): Long

    @Delete
    suspend fun deleteCity(city: City): Int

    @Update
    suspend fun updateCity(city: City): Int

    @Query("select * from city where isWidget = 1")
    suspend fun getWidgetCity(): City
}