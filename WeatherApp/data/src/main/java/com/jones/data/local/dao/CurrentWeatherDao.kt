package com.jones.data.local.dao

import androidx.room.*
import com.jones.data.local.entity.CurrentWeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM current_weather WHERE id = :cityId")
    fun getCurrentWeather(cityId: Int): Flow<CurrentWeatherEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(weather: CurrentWeatherEntity)

    @Query("DELETE FROM current_weather")
    suspend fun deleteAll()
}

