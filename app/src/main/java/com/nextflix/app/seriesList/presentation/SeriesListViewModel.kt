package com.nextflix.app.seriesList.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextflix.app.seriesList.domain.model.Series
import com.nextflix.app.seriesList.domain.repository.SeriesListRepository
import com.nextflix.app.seriesList.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesListViewModel @Inject constructor(
    private val seriesListRepository: SeriesListRepository
) : ViewModel() {

    private var _seriesListState = MutableStateFlow(SeriesListState())
    val seriesListState = _seriesListState.asStateFlow()

    private var searchJob: Job? = null

//    init {
//        getSeries(false)
//        getUserSeries()
//    }

    private var _isInitialized = mutableStateOf(false)
    val isInitialized: Boolean get() = _isInitialized.value

    fun initializeData() {
        if (!_isInitialized.value) {
            getSeries(false)  // Call your method
            getUserSeries()   // Call your method
            _isInitialized.value = true
        }
    }

    fun onEvent(event: SeriesListUiEvent) {
        when (event) {
            is SeriesListUiEvent.Paginate -> {
                getSeries(true)
            }

            is SeriesListUiEvent.OnSearchQueryChanged -> {
                onSearch(query = event.query)
            }

            is SeriesListUiEvent.OnWatchLaterSeries -> {
                saveWatchLater(
                    seriesId = event.seriesId,
                    seriesName = event.seriesName,
                    posterPath = event.posterPath
                )
            }

            is SeriesListUiEvent.OnRemoveWatchLaterSeries -> {
                removeWatchLater(seriesId = event.seriesId)
            }

            SeriesListUiEvent.Navigate -> {
//                getSeries(false)
//                getUserSeries()
            }

        }
    }

    private fun getSeries(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _seriesListState.update {
                it.copy(isSeriesLoading = true)
            }

            seriesListRepository.getSeriesList(
                forceFetchFromRemote,
                seriesListState.value.seriesListPage
            )
                .collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _seriesListState.update {
                                it.copy(isSeriesLoading = false, seriesError = result.message)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { sList ->
                                _seriesListState.update {
                                    it.copy(
                                        seriesList = seriesListState.value.seriesList
                                                //+ sList.shuffled(),
                                                + sList,
                                        seriesListPage = seriesListState.value.seriesListPage + 1
                                    )
                                }
                            }
                        }

                        is Resource.Loading -> {
                            _seriesListState.update {
                                it.copy(isSeriesLoading = result.isLoading)
                            }
                        }
                    }

                }
        }
    }

    private fun getUserSeries() {
        viewModelScope.launch {
            _seriesListState.update {
                it.copy(isUserSeriesLoading = true)
            }

            seriesListRepository.getUserSeriesList()
                .collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _seriesListState.update {
                                it.copy(isUserSeriesLoading = false, userSeriesError = result.message)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { sList ->
                                _seriesListState.update {
                                    it.copy(
                                        userSeriesList = sList
                                    )
                                }
                            }
                        }

                        is Resource.Loading -> {
                            _seriesListState.update {
                                it.copy(isUserSeriesLoading = result.isLoading)
                            }
                        }
                    }
                }
        }
    }

    private fun onSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _seriesListState.update {
                it.copy(isSeriesLoading = true)
            }

            delay(300)

            seriesListRepository.getSearchSeriesList(
                query = query
            )
                .collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            Log.i("check123", "1")
                            _seriesListState.update {
                                it.copy(isSeriesLoading = false, seriesError = result.message)
                            }
                        }

                        is Resource.Success -> {
                            Log.i("check123", "2")
                            result.data?.let { sList ->
                                Log.e("ser12345", seriesListState.value.seriesList.toString())
                                _seriesListState.update {
                                    it.copy(
                                        searchSeriesList = sList
                                    )
                                }
                            }
                        }

                        is Resource.Loading -> {
                            Log.i("check123", "3")
                            _seriesListState.update {
                                it.copy(isSeriesLoading = result.isLoading)
                            }
                        }
                    }

                }
        }
    }

    private fun saveWatchLater(seriesId: Int, seriesName: String, posterPath: String) {
        viewModelScope.launch {
            _seriesListState.update {
                it.copy(
                    isWatchLaterLoading = seriesListState.value.isWatchLaterLoading + Pair(
                        seriesId,
                        true
                    )
                )
            }


            val result = seriesListRepository.addToWatchLater(seriesId, seriesName, posterPath)
            if (result.isSuccess) {
                _seriesListState.update {
                    val updatedSeriesList = it.seriesList.map { series ->
                        if (series.id == seriesId) {
                            series.copy(watch_later = true) // Update watchLater for the specific series
                        } else {
                            series
                        }
                    }

                    val updatedSearchSeriesList = it.searchSeriesList.map { series ->
                        if (series.id == seriesId) {
                            series.copy(watch_later = true) // Update watchLater for the specific series
                        } else {
                            series
                        }
                    }

                    it.copy(
                        isWatchLaterLoading = seriesListState.value.isWatchLaterLoading + Pair(
                            seriesId,
                            false
                        ),
                        seriesList = updatedSeriesList,
                        searchSeriesList = updatedSearchSeriesList,
                        userSeriesList = it.userSeriesList + Series(
                            id = seriesId,
                            name = seriesName,
                            poster_path = posterPath,
                            watch_later = true,
                            //default values
                            adult = false,
                            backdrop_path = "",
                            first_air_date = "",
                            genre_ids = emptyList(),
                            origin_country = emptyList(),
                            original_language = "",
                            original_name = "",
                            overview = "",
                            popularity = 0.0,
                            vote_average = 0.0,
                            vote_count = 0
                        )
                    )
                }
            } else {
                _seriesListState.update {
                    it.copy(
                        watchLaterError = "Failed to add to watch later",
                        isWatchLaterLoading = seriesListState.value.isWatchLaterLoading + Pair(
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
            _seriesListState.update {
                it.copy(
                    isWatchLaterLoading = seriesListState.value.isWatchLaterLoading + Pair(
                        seriesId,
                        true
                    )
                )

            }

            val result = seriesListRepository.removeFromWatchLater(seriesId)
            if (result.isSuccess) {
                _seriesListState.update {
                    val updatedSeriesList = it.seriesList.map { series ->
                        if (series.id == seriesId) {
                            series.copy(watch_later = false) // Update watchLater for the specific series
                        } else {
                            series
                        }
                    }

                    val updatedSearchSeriesList = it.searchSeriesList.map { series ->
                        if (series.id == seriesId) {
                            series.copy(watch_later = false) // Update watchLater for the specific series
                        } else {
                            series
                        }
                    }

                    // Remove the series from the user series list
                    val updatedUserSeriesList = it.userSeriesList.filter { series ->
                        series.id != seriesId // Remove the series from the user list
                    }

                    it.copy(
                        isWatchLaterLoading = seriesListState.value.isWatchLaterLoading + Pair(
                            seriesId,
                            false
                        ),
                        seriesList = updatedSeriesList,
                        searchSeriesList = updatedSearchSeriesList,
                        userSeriesList = updatedUserSeriesList
                    )
                }

            } else {
                _seriesListState.update {
                    it.copy(
                        watchLaterError = "Failed to add to watch later",
                        isWatchLaterLoading = seriesListState.value.isWatchLaterLoading + Pair(
                            seriesId,
                            false
                        )
                    )
                }
            }
        }
    }
}