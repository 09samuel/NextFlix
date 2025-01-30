package com.nextflix.app.seriesList.presentation

sealed interface SeriesListUiEvent {
    object Paginate : SeriesListUiEvent
    data class OnSearchQueryChanged(val query: String) : SeriesListUiEvent
    data class OnWatchLaterSeries(val seriesId: Int, val seriesName: String, val posterPath: String) : SeriesListUiEvent
    data class OnRemoveWatchLaterSeries(val seriesId: Int) : SeriesListUiEvent
    object Navigate : SeriesListUiEvent
}