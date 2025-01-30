package com.nextflix.app.upcoming.domain.repository

import com.nextflix.app.seriesList.utils.Resource
import com.nextflix.app.upcoming.domain.model.UpcomingEpisode
import kotlinx.coroutines.flow.Flow

interface UpcomingEpisodesRepository {
    suspend fun getUpcomingEpisodesList(): Flow<Resource<List<UpcomingEpisode>>>
}