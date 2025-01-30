package com.nextflix.app.seriesDetails.data.respond.episodes

//Season
data class EpisodesListDto(
    val _id: String?,
    val air_date: String?,
    val episodes: List<EpisodeDto>?,
    val id: Int?,
    val name: String?,

    //below is commented because they are details of season
    //val overview: String?,
    //val poster_path: String?,
    //val season_number: Int?,
    //val vote_average: Double?
)