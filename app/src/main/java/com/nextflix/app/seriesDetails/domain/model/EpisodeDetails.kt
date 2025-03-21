package com.nextflix.app.seriesDetails.domain.model

data class EpisodeDetails(
    val air_date: String,
   // val crew: List<Crew>,
    val episode_number: Int,
    //val guest_stars: List<GuestStar>,
    val id: Int,
    val name: String,
    val overview: String,
    val production_code: String,
    val runtime: Int,
    val season_number: Int,
    val still_path: String,
    val vote_average: Double,
    val vote_count: Int
)