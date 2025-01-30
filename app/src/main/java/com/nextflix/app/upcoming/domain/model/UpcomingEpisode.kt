package com.nextflix.app.upcoming.domain.model

data class UpcomingEpisode (
    val id: Int,
    val season_number: Int,
    val episode_number: Int,
    val name: String,
    val still_path: String,
    val air_date: String,
    val series_id: Int,
    val series_name: String,
    val overview: String,
    val vote_average: Double,
    val vote_count: Int,
    val daysLeft: Int,
    //var watched: Boolean
)