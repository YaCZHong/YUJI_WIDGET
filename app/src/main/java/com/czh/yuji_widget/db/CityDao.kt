package com.czh.yuji_widget.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCity(city: City): Long

    @Delete
    suspend fun deleteCity(city: City): Int

    @Update
    suspend fun updateCity(city: City): Int

    @Query("select * from CITY")
    fun getCities(): LiveData<List<City>>
}