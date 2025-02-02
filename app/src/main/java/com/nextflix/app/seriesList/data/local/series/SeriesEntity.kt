package com.nextflix.app.seriesList.data.local.series

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SeriesEntity(
    val userId: String,
    val adult: Boolean,
    val backdrop_path: String,
    val first_air_date: String,
    val genre_ids: String,
    val name: String,
    val origin_country: String,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val vote_average: Double,
    val vote_count: Int,
    var watch_later: Boolean,

    @PrimaryKey
    val id: Int
)