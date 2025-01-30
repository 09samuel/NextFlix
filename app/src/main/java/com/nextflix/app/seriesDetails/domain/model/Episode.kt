package com.nextflix.app.seriesDetails.domain.model

data class Episode(
    val id: Int,
    val season_number: Int,
    val episode_number: Int,
    val name: String,
    val still_path: String,
    val air_date: String,
    val episode_type: String,
    val overview: String,
    val runtime: Int,
    val vote_average: Double,
    val vote_count: Int,
    val production_code: String?,

    var watched: Boolean,
    var isReleased: Boolean,
)