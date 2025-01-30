package com.nextflix.app.seriesDetails.data.mappers

import com.nextflix.app.seriesDetails.data.respond.episodes.EpisodeDto
import com.nextflix.app.seriesDetails.data.respond.generalDetails.GeneralDetailsDto
import com.nextflix.app.seriesDetails.data.respond.recommendations.RecommendationsDto
import com.nextflix.app.seriesDetails.domain.model.Episode
import com.nextflix.app.seriesDetails.domain.model.EpisodeDetails
import com.nextflix.app.seriesDetails.domain.model.GeneralDetails
import com.nextflix.app.seriesDetails.domain.model.Recommendations

fun GeneralDetailsDto.toGeneralDetails(): GeneralDetails {
    return GeneralDetails(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        created_by = created_by ?: emptyList(),
        episode_run_time = episode_run_time ?: emptyList(),
        first_air_date = first_air_date ?: "",
        genres = genres ?: emptyList(),
        homepage = homepage ?: "",
        id = id ?: 0,
        in_production = in_production ?: false,
        languages = languages ?: emptyList(),
        last_air_date = last_air_date ?: "",
        name = name ?: "",
        networks = networks ?: emptyList(),
        number_of_episodes = number_of_episodes ?: 0,
        number_of_seasons = number_of_seasons ?: 0,
        origin_country = origin_country ?: emptyList(),
        original_language = original_language ?: "",
        original_name = original_name ?: "",
        overview = overview ?: "",
        popularity = popularity ?: 0.0,
        poster_path = poster_path ?: "",
        production_companies = production_companies ?: emptyList(),
        production_countries = production_countries ?: emptyList(),
        seasons = seasons ?: emptyList(),
        spoken_languages = spoken_languages ?: emptyList(),
        status = status ?: "",
        tagline = tagline ?: "",
        type = type ?: "",
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0
    )
}

fun RecommendationsDto.toRecommendations(): Recommendations {
    return Recommendations(
        id = id ?: 0,
        name = name ?: "",
        poster_path = poster_path ?: "",
        watch_later = false
    )
}

fun EpisodeDto.toEpisode(): Episode {
    return Episode(
        id = id ?: 0,
        name = name ?: "",
        season_number = season_number ?: 0,
        episode_number = episode_number ?: 0,
        air_date = air_date ?: "",
        still_path = still_path ?: "",
        watched = false,
        isReleased = false,
        overview = overview ?: "",
        vote_average = vote_average ?: 0.0,
        vote_count = vote_count ?: 0,
        production_code = production_code ?: "",
        runtime = runtime ?: 0,
        episode_type = episode_type ?: "",
    )
}

//fun EpisodeDetailsDto.toEpisodeDetails(): EpisodeDetails {
//    return EpisodeDetails(
//        id = id ?: 0,
//        name = name ?: "",
//        season_number = season_number ?: 0,
//        episode_number = episode_number ?: 0,
//        air_date = air_date ?: "",
//        still_path = still_path ?: "",
//        overview = overview ?: "",
//        production_code = production_code ?: "",
//        runtime = runtime ?: 0,
//        vote_average = vote_average ?: 0.0,
//        vote_count = vote_count ?: 0,
////        crew = crew ?: emptyList(),
////        guest_stars = guest_stars ?: emptyList(),
//    )
//}