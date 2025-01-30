package com.nextflix.app.seriesList.data.local.series

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface SeriesDao {
    @Upsert
    suspend fun upsertSeriesList(seriesList: List<SeriesEntity>)

    @Query("SELECT * FROM SeriesEntity WHERE id=:id")
    suspend fun getSeriesById(id: Int): SeriesEntity

    @Query("SELECT * FROM SeriesEntity ORDER BY popularity DESC")
    suspend fun getLocalSeries(): List<SeriesEntity>

    @Query("UPDATE SeriesEntity SET watch_later=1  WHERE id=:id")
    suspend fun addToWatchLater(id: Int)

    @Query("UPDATE SeriesEntity SET watch_later=0  WHERE id=:id")
    suspend fun removeFromWatchLater(id: Int)
}