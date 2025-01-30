package com.nextflix.app.seriesDetails.data.respond.episodes

data class EpisodeDto(
    val id: Int?,
    val season_number: Int?,
    val episode_number: Int?,
    val name: String?,
    val still_path: String?,
    val air_date: String?,
    val episode_type: String?,
    val overview: String?,
    val production_code: String?,
    val runtime: Int?,
    val vote_average: Double?,
    val vote_count: Int?
    //val show_id: Int?,  //seriesId
    //val crew: List<CrewDto>?,
    //val guest_stars: List<GuestStarDto>?,
)