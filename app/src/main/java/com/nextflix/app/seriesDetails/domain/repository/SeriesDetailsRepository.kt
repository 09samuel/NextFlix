package com.nextflix.app.seriesDetails.domain.repository

import com.nextflix.app.seriesDetails.domain.model.Episode
import com.nextflix.app.seriesDetails.domain.model.GeneralDetails
import com.nextflix.app.seriesDetails.domain.model.Recommendations
import com.nextflix.app.seriesDetails.domain.model.SeriesWatchProviders
import com.nextflix.app.seriesList.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SeriesDetailsRepository {
    suspend fun getSeries(seriesId: Int): Flow<Resource<GeneralDetails>>
    suspend fun getSeriesWatchProviders(id: Int, countryCode: String): Flow<Resource<List<SeriesWatchProviders>>>
    suspend fun getRecommendations(seriesId: Int): Flow<Resource<List<Recommendations>>>
    suspend fun addToWatchLater(seriesId: Int, seriesName: String, posterPath: String): Result<Unit>
    suspend fun removeFromWatchLater(seriesId: Int): Result<Unit>
    suspend fun getEpisodes(seriesId: Int, seasonNumber: Int): Flow<Resource<List<Episode>>>
    suspend fun addToWatched(episodeId: Int, seriesId: Int, seasonNo:Int, seriesName: String, posterPath: String): Result<Unit>
    suspend fun removeFromWatched(seriesId: Int, episodeId: Int): Result<Unit>
}