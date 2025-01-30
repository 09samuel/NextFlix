package com.nextflix.app.seriesDetails.domain.model

import com.nextflix.app.seriesDetails.data.respond.generalDetails.CreatedBy
import com.nextflix.app.seriesDetails.data.respond.generalDetails.Genre
import com.nextflix.app.seriesDetails.data.respond.generalDetails.Network
import com.nextflix.app.seriesDetails.data.respond.generalDetails.ProductionCompany
import com.nextflix.app.seriesDetails.data.respond.generalDetails.ProductionCountry
import com.nextflix.app.seriesDetails.data.respond.generalDetails.Season
import com.nextflix.app.seriesDetails.data.respond.generalDetails.SpokenLanguage

data class GeneralDetails(
    val adult: Boolean,
    val backdrop_path: String,
    val created_by: List<CreatedBy>,
    val episode_run_time: List<Any>,
    val first_air_date: String,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val in_production: Boolean,
    val languages: List<String>,
    val last_air_date: String,
    val name: String,
    val networks: List<Network>,
    val number_of_episodes: Int,
    val number_of_seasons: Int,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val seasons: List<Season>,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val type: String,
    val vote_average: Double,
    val vote_count: Int
)