package com.nextflix.app.seriesList.data.mappers

import com.nextflix.app.seriesList.data.local.series.SeriesEntity
import com.nextflix.app.seriesList.data.remote.respond.SeriesDto
import com.nextflix.app.seriesList.domain.model.Series
import com.nextflix.app.seriesList.domain.model.WatchLaterEntry
import com.nextflix.app.watchLater.data.model.WatchLaterSeries

fun SeriesDto.toSeriesEntity(userId: String): SeriesEntity {
    return SeriesEntity(
        userId = userId,
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        first_air_date = first_air_date ?: "",
        id = id ?: -1,
        name = name ?: "",
        original_language = original_language ?: "",
        original_name = original_name ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,
        watch_later = false,

        genre_ids = try {
            genre_ids?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        },

        origin_country = try {
            origin_country?.joinToString(",") ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }
    )
}

fun SeriesEntity.toSeries(): Series{
    return Series(
        adult = adult,
        backdrop_path = backdrop_path,
        first_air_date = first_air_date,
        id = id,
        name = name,

        original_language = original_language,
        original_name = original_name ,
        overview = overview,
        popularity = popularity,
        poster_path = poster_path,
        vote_average = vote_average,
        vote_count = vote_count,
        watch_later = watch_later,

        genre_ids = try {
            genre_ids.split(",").map { it.toInt() }
        } catch (e: Exception) {
            listOf(-1, -2)
        },

        origin_country = try {
            origin_country.split(",").map { it }
        } catch (e: Exception) {
            listOf("Unknown")
        }
    )
}

fun SeriesDto.toSeries(): Series {
    return Series(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        first_air_date = first_air_date ?: "",
        id = id ?: -1,
        name = name ?: "",
        original_language = original_language ?: "",
        original_name = original_name ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,
        watch_later = false,

        genre_ids = try {
            genre_ids?.toList() ?: listOf(-1, -2)
        } catch (e: Exception) {
            listOf(-1, -2)
        },

        origin_country = try {
            origin_country?.toList() ?: listOf("Unknown")
        } catch (e: Exception) {
            listOf("Unknown")
        }
    )
}

fun WatchLaterEntry.toSeries(): Series{
    return Series(
        adult = false,
        backdrop_path = "",
        first_air_date = "",

        id = series_id,
        name = series_name,
        watch_later = true,
        poster_path = poster_path?:"",


        original_language = "",
        original_name = "" ,
        overview = "",
        popularity = 0.0,
        vote_average = 0.0,
        vote_count = 0,
        genre_ids = emptyList(),
        origin_country = emptyList()
    )
}

fun WatchLaterSeries.toSeries(): Series{
    return Series(
        adult = false,
        backdrop_path = "",
        first_air_date = "",

        id = seriesId,
        name = seriesName,
        watch_later = true,
        poster_path = posterPath?:"",


        original_language = "",
        original_name = "" ,
        overview = "",
        popularity = 0.0,
        vote_average = 0.0,
        vote_count = 0,
        genre_ids = emptyList(),
        origin_country = emptyList()
    )
}