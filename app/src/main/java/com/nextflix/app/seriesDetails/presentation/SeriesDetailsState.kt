package com.nextflix.app.seriesDetails.presentation

import com.nextflix.app.seriesDetails.domain.model.Episode
import com.nextflix.app.seriesDetails.domain.model.EpisodeDetails
import com.nextflix.app.seriesDetails.domain.model.GeneralDetails
import com.nextflix.app.seriesDetails.domain.model.Recommendations
import com.nextflix.app.seriesDetails.domain.model.SeriesWatchProviders
import java.lang.Error

data class SeriesDetailsState(
    val isLoading: Boolean = false,
    val error: String?="",

    val isWatchProviderLoading: Boolean = false,
    val watchProviderError: String? = "",

    val isRecommendationsListLoading: Boolean = false,
    val recommendationsListError: String?= "",

    val details: GeneralDetails? = null,
    val watchProviders: List<SeriesWatchProviders> = emptyList(),
    val recommendationsList: List<Recommendations> = emptyList(),
    val episodesList: List<List<Episode>> = emptyList(),

    val isWatchLaterLoading: Map<Int,Boolean> = emptyMap(),
    val watchLaterError: String? ="",

    val isWatchedLoading: Map<Int,Boolean> = emptyMap(),
    val watchedError: String? ="",

    val episodesDetailsList: List<EpisodeDetails> = emptyList()
)