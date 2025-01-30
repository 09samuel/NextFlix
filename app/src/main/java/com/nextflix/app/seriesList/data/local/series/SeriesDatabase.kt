package com.nextflix.app.seriesList.data.local.series

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SeriesEntity::class], version = 1, exportSchema = false)
abstract class SeriesDatabase: RoomDatabase() {
    abstract  val seriesDao: SeriesDao
}