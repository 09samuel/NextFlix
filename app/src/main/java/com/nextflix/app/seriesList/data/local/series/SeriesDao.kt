package com.nextflix.app.seriesList.data.local.series

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface SeriesDao {
    @Upsert
    suspend fun upsertSeriesList(seriesList: List<SeriesEntity>)

    @Query("SELECT * FROM SeriesEntity WHERE id=:id AND userId=:userId")
    suspend fun getSeriesById(id: Int, userId: String): SeriesEntity

    @Query("SELECT * FROM SeriesEntity WHERE userId=:userId ORDER BY popularity DESC")
    suspend fun getLocalSeries(userId: String): List<SeriesEntity>

    @Query("UPDATE SeriesEntity SET watch_later=1 WHERE id=:id AND userId=:userId")
    suspend fun addToWatchLater(id: Int, userId: String)

    @Query("UPDATE SeriesEntity SET watch_later=0 WHERE id=:id AND userId=:userId")
    suspend fun removeFromWatchLater(id: Int, userId: String)
}
