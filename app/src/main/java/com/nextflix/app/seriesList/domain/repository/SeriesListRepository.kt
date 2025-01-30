package com.nextflix.app.seriesList.domain.repository

import com.nextflix.app.seriesList.domain.model.Series
import com.nextflix.app.seriesList.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SeriesListRepository {
    suspend fun getSeriesList(forceFetchFromRemote: Boolean, page: Int): Flow<Resource<List<Series>>>
    suspend fun getUserSeriesList(): Flow<Resource<List<Series>>>
    suspend fun getSearchSeriesList(query: String): Flow<Resource<List<Series>>>
    suspend fun addToWatchLater(seriesId: Int, seriesName: String, posterPath: String): Result<Unit>
    suspend fun removeFromWatchLater(seriesId: Int): Result<Unit>
}