package com.nextflix.app.upcoming.data.mappers

import com.nextflix.app.seriesDetails.data.respond.episodes.EpisodeDto
import com.nextflix.app.seriesDetails.domain.model.Episode
import com.nextflix.app.upcoming.domain.model.UpcomingEpisode

fun EpisodeDto.toUpcomingEpisode(): UpcomingEpisode {
    return UpcomingEpisode(
        id = id ?: 0,
        name = name ?: "",
        season_number = season_number ?: 0,
        episode_number = episode_number ?: 0,
        air_date = air_date ?: "",
        still_path = still_path ?: "",
        series_id = 0,
        overview = overview?: "",
        series_name = "",
        vote_average = vote_average?: 0.0,
        vote_count = vote_count?: 0,
        daysLeft = 0,
    )
}