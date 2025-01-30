package com.nextflix.app.seriesDetails.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextflix.app.seriesDetails.domain.repository.SeriesDetailsRepository
import com.nextflix.app.seriesList.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesDetailsViewModel @Inject constructor(
    private val seriesDetailsRepository: SeriesDetailsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val seriesId = savedStateHandle.get<String>("seriesId")

    private var _detailsState = MutableStateFlow(SeriesDetailsState())
    val detailsState = _detailsState.asStateFlow()

    init {
        if (seriesId != null) {
            getSeries(seriesId.toInt())
        }
        if (seriesId != null) {
            getSeriesWatchProviders(seriesId.toInt())
        }
        if (seriesId != null) {
            getRecommendations(seriesId.toInt())
        }
    }

    fun onEvent(event: SeriesDetailsUiEvent) {
        when (event) {
            is SeriesDetailsUiEvent.OnWatchLaterSeries -> {
                saveWatchLater(seriesId = event.seriesId, seriesName = event.seriesName, posterPath = event.posterPath)
            }

            is SeriesDetailsUiEvent.OnRemoveWatchLaterSeries -> {
                removeWatchLater(seriesId = event.seriesId)
            }

            is SeriesDetailsUiEvent.OnEpisodes -> {
                getEpisodes(seriesId = event.seriesId, seasonNo = event.seasonNo)
            }

            is SeriesDetailsUiEvent.OnRemoveFromWatched -> {
                removeFromWatched(seriesId = event.seriesId, episodeId = event.episodeId)
            }
            is SeriesDetailsUiEvent.OnWatched -> {
                saveToWatched(episodeId = event.episodeId, seriesId = event.seriesId, seasonNo = event.seasonNo, seriesName = event.seriesName, posterPath = event.posterPath)
            }
        }
    }

    private fun getSeries(seriesId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }

            seriesDetailsRepository.getSeries(seriesId).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detailsState.update {
                            it.copy(isLoading = false, error = result.message)
                        }
                    }

                    is Resource.Loading -> {
                        _detailsState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { series ->
                            _detailsState.update {
                                it.copy(details = series)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getSeriesWatchProviders(seriesId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }

            seriesDetailsRepository.getSeriesWatchProviders(seriesId, "IN").collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detailsState.update {
                            it.copy(isWatchProviderLoading = false, watchProviderError = result.message)
                        }
                    }

                    is Resource.Loading -> {
                        _detailsState.update {
                            it.copy(isWatchProviderLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { watchProviders ->
                            _detailsState.update {
                                it.copy(watchProviders = watchProviders)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getRecommendations(seriesId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }

            seriesDetailsRepository.getRecommendations(seriesId).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detailsState.update {
                            it.copy(isRecommendationsListLoading = false, recommendationsListError = result.message)
                        }
                    }

                    is Resource.Loading -> {
                        _detailsState.update {
                            it.copy(isRecommendationsListLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { recommendations ->
                            _detailsState.update {
                                it.copy(recommendationsList = recommendations)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveWatchLater(seriesId: Int, seriesName: String, posterPath: String) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(
                    isWatchLaterLoading = detailsState.value.isWatchLaterLoading + Pair(
                        seriesId,
                        true
                    )
                )
            }

            val result = seriesDetailsRepository.addToWatchLater(seriesId, seriesName, posterPath)
            if (result.isSuccess) {
                _detailsState.update {
                    val updatedSeriesList = it.recommendationsList.map { series ->
                        if (series.id== seriesId) {
                            series.copy(watch_later = true) // Update watchLater for the specific series
                        } else {
                            series
                        }
                    }

                    it.copy(
                        isWatchLaterLoading = detailsState.value.isWatchLaterLoading + Pair(
                            seriesId,
                            false
                        ),
                        recommendationsList = updatedSeriesList
                    )


                }
            } else {
                _detailsState.update {
                    it.copy(
                        watchLaterError = "Failed to add to watch later",
                        isWatchLaterLoading = detailsState.value.isWatchLaterLoading + Pair(
                            seriesId,
                            false
                        )
                    )
                }
            }
        }
    }

    private fun removeWatchLater(seriesId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(
                    isWatchLaterLoading = detailsState.value.isWatchLaterLoading + Pair(
                        seriesId,
                        true
                    )
                )

            }

            val result = seriesDetailsRepository.removeFromWatchLater(seriesId)
            if (result.isSuccess) {
                _detailsState.update {
                    val updatedSeriesList = it.recommendationsList.map { series ->
                        if (series.id == seriesId) {
                            series.copy(watch_later = false) // Update watchLater for the specific series
                        } else {
                            series
                        }
                    }

                    it.copy(
                        isWatchLaterLoading = detailsState.value.isWatchLaterLoading + Pair(
                            seriesId,
                            false
                        ),
                        recommendationsList = updatedSeriesList
                    )
                }

            } else {
                _detailsState.update {
                    it.copy(
                        watchLaterError = "Failed to add to watch later",
                        isWatchLaterLoading = detailsState.value.isWatchLaterLoading + Pair(
                            seriesId,
                            false
                        )
                    )
                }

            }
        }
    }

    private fun getEpisodes(seriesId: Int, seasonNo: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }

            seriesDetailsRepository.getEpisodes(seriesId, seasonNo).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detailsState.update {
                            it.copy(isLoading = false, error = result.message)
                        }
                    }

                    is Resource.Loading -> {
                        _detailsState.update {
                            it.copy(isLoading = result.isLoading)
                        }

                    }

                    is Resource.Success -> {
                        result.data?.let { episodes ->
                            _detailsState.update {
                                it.copy(
                                    //isLoading = false,
                                    episodesList = it.episodesList.toMutableList().apply {
                                        if (seasonNo >= size) {
                                            // Expand the list to accommodate the new season
                                            repeat(seasonNo - size + 1) { add(emptyList()) }
                                        }
                                        // Replace the list for the given seasonNo
                                        this[seasonNo] = episodes
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveToWatched(episodeId: Int, seriesId: Int, seasonNo: Int, seriesName: String, posterPath: String) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(
                    isWatchedLoading = detailsState.value.isWatchedLoading + Pair(
                        episodeId,
                        true
                    )
                )
            }

            val result = seriesDetailsRepository.addToWatched(episodeId, seriesId, seasonNo, seriesName, posterPath)
            if (result.isSuccess) {
                _detailsState.update {
                    // Update the watched status for the matching episodes
                    val updatedEpisodesList = it.episodesList.map { episodeList ->
                        episodeList.map { episode ->
                            if (episode.id == episodeId) {
                                episode.copy(watched = true) // Set watched to true
                            } else {
                                episode
                            }
                        }
                    }

                    it.copy(
                        isWatchedLoading = detailsState.value.isWatchedLoading + Pair(
                            episodeId,
                            false
                        ),
                        episodesList = updatedEpisodesList // Update the state with the modified list
                    )
                }
            } else {
                _detailsState.update {
                    it.copy(
                        watchedError = "Failed to add to watched",
                        isWatchedLoading = detailsState.value.isWatchedLoading + Pair(
                            episodeId,
                            false
                        )
                    )
                }
            }
        }
    }

    private fun removeFromWatched(seriesId: Int, episodeId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(
                    isWatchedLoading = detailsState.value.isWatchedLoading + Pair(
                        episodeId,
                        true
                    )
                )
            }

            val result = seriesDetailsRepository.removeFromWatched(seriesId,episodeId)
            if (result.isSuccess) {
                _detailsState.update {
                    val updatedEpisodesList = it.episodesList.map { episodeList ->
                        episodeList.map { episode ->
                            if (episode.id == episodeId) {
                                episode.copy(watched = false)
                            } else {
                                episode
                            }
                        }
                    }

                    it.copy(
                        isWatchedLoading = detailsState.value.isWatchedLoading + Pair(
                            episodeId,
                            false
                        ),
                        episodesList = updatedEpisodesList // Update the state with the modified list
                    )
                }

            } else {
                _detailsState.update {
                    it.copy(
                        watchedError = "Failed to remove from watched",
                        isWatchedLoading = detailsState.value.isWatchedLoading + Pair(
                            episodeId,
                            false
                        )
                    )
                }

            }
        }
    }

//    fun realtimeDb(scope: CoroutineScope) {
//        viewModelScope.launch {
//            try {
//                _detailsState.update {
//                    it.copy(isLoading = true)
//                }
//
//                val channel = client.channel("UserEpisodeProgress")
//                val dataFlow = channel.postgresChangeFlow<PostgresAction>(schema = "public")
//
//                dataFlow.onEach {
//                    when(it) {
//                        is PostgresAction.Delete -> {
//
//                        }
//                        is PostgresAction.Insert -> {
//
//                        }
//                        is PostgresAction.Select -> {
//
//                        }
//                        is PostgresAction.Update -> {
//                            val stringifiedData = it.record.toString()
//                            val data = Json.decodeFromString<>(stringifiedData)
//
//                        }
//                    }
//                }.launchIn(scope)
//                channel.subscribe()
//            } catch (e: Exception) {
//                _detailsState.update {
//                    it.copy(isLoading = false, error = e.message)
//                }
//            }
//        }
//    }

}