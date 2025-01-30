package com.nextflix.app.upcoming.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextflix.app.seriesList.utils.Resource
import com.nextflix.app.upcoming.domain.repository.UpcomingEpisodesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingEpisodesViewModel @Inject constructor(
    private val upcomingEpisodesRepository: UpcomingEpisodesRepository
) : ViewModel() {
    private var _upcomingEpisodesState = MutableStateFlow(UpcomingEpisodesState())
    val upcomingEpisodesState = _upcomingEpisodesState.asStateFlow()


    fun onEvent(event: UpcomingEpisodesUiEvent) {
        when (event) {
            UpcomingEpisodesUiEvent.Navigate -> {
                getUpcomingEpisodes()
            }
        }
    }

    private fun getUpcomingEpisodes() {
        viewModelScope.launch {
            _upcomingEpisodesState.update {
                it.copy(isLoading = true)
            }

            upcomingEpisodesRepository.getUpcomingEpisodesList()
                .collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _upcomingEpisodesState.update {
                                it.copy(isLoading = false, error = result.message)
                            }
                        }

                        is Resource.Success -> {
                            result.data?.let { uList ->
                                _upcomingEpisodesState.update {
                                    it.copy(
                                        upcomingEpisodesList = uList
                                    )
                                }
                            }
                        }

                        is Resource.Loading -> {
                            _upcomingEpisodesState.update {
                                it.copy(isLoading = result.isLoading)
                            }
                        }
                    }

                }
        }
    }
}