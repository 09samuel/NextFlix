package com.nextflix.app.upcoming.presentation

sealed interface UpcomingEpisodesUiEvent {
    object Navigate : UpcomingEpisodesUiEvent
}