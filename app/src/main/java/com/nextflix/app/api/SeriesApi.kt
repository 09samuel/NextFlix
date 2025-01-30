package com.nextflix.app.api

import com.nextflix.app.BuildConfig

import com.nextflix.app.seriesDetails.data.respond.episodes.EpisodesListDto
import com.nextflix.app.seriesDetails.data.respond.generalDetails.GeneralDetailsDto
import com.nextflix.app.seriesDetails.data.respond.recommendations.RecommendationsListDto
//import com.nextflix.app.seriesDetails.data.respond.SeriesDetailsDto
import com.nextflix.app.seriesDetails.data.respond.watchProvider.SeriesWatchProvidersDto
import com.nextflix.app.seriesList.data.remote.respond.SeriesListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SeriesApi {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780"
        const val API_KEY = BuildConfig.tmdbKey
    }

    @GET("tv/top_rated")
    suspend fun getSeriesList(
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String = API_KEY
    ): SeriesListDto

    @GET("search/tv")
    suspend fun getSearchSeriesList(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): SeriesListDto

    @GET("tv/{series_id}/season/1/watch/providers")
    suspend fun getWatchProviders(
         @Path("series_id") seriesId: Int,
         @Query("language") language: String = "en-US",
         @Query("api_key") apiKey: String =API_KEY
    ): SeriesWatchProvidersDto

    @GET("tv/{series_id}")
    suspend fun getGeneralDetails(
        @Path("series_id") seriesId: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String =API_KEY
    ): GeneralDetailsDto

    @GET("tv/{series_id}/recommendations")
    suspend fun getRecommendations(
        @Path("series_id") seriesId: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String =API_KEY
    ): RecommendationsListDto

    @GET("tv/{series_id}/season/{season_number}")
    suspend fun getEpisodes(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNo: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String =API_KEY
    ): EpisodesListDto
}