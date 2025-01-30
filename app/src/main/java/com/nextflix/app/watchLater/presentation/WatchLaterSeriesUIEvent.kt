package com.nextflix.app.watchLater.presentation

import com.nextflix.app.seriesList.presentation.SeriesListUiEvent

sealed interface WatchLaterSeriesUIEvent {
    data class OnWatchLaterSeries(val id: String) : WatchLaterSeriesUIEvent
}