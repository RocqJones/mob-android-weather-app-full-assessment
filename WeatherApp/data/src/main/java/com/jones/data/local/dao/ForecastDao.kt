package com.jones.data.local.dao

import androidx.room.*
import com.jones.data.local.entity.ForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {
    @Query("SELECT * FROM forecast WHERE cityId = :cityId ORDER BY timestamp ASC")
    fun getForecast(cityId: Int): Flow<List<ForecastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: List<ForecastEntity>)

    @Query("DELETE FROM forecast WHERE cityId = :cityId")
    suspend fun deleteForecastByCity(cityId: Int)

    @Query("DELETE FROM forecast")
    suspend fun deleteAll()
}
