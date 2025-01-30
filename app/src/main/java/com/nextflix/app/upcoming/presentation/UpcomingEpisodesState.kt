package com.nextflix.app.upcoming.presentation

import com.nextflix.app.upcoming.domain.model.UpcomingEpisode

data class UpcomingEpisodesState (
    val isLoading: Boolean = false,
    val error: String? = "",
    val upcomingEpisodesList: List<UpcomingEpisode> = emptyList(),
)