package com.nextflix.app.seriesDetails.presentation

sealed interface SeriesDetailsUiEvent {
    data class OnWatchLaterSeries(val seriesId: Int, val seriesName: String, val posterPath: String) : SeriesDetailsUiEvent
    data class OnRemoveWatchLaterSeries(val seriesId: Int) : SeriesDetailsUiEvent
    data class OnEpisodes(val seriesId: Int, val seasonNo: Int): SeriesDetailsUiEvent
    data class OnWatched(val episodeId: Int, val seriesId: Int, val seasonNo: Int, val seriesName: String, val posterPath: String): SeriesDetailsUiEvent
    data class OnRemoveFromWatched(val seriesId: Int, val episodeId: Int): SeriesDetailsUiEvent
}
