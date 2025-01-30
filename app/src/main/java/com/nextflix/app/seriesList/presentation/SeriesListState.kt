package com.nextflix.app.seriesList.presentation

import com.nextflix.app.seriesList.domain.model.Series

data class SeriesListState (
    val isSeriesLoading: Boolean = false,
    val seriesError: String? = "",
    val seriesListPage: Int = 1,
    val seriesList: List<Series> = emptyList(),
    val searchQuery: String = "",

    val isWatchLaterLoading: Map<Int,Boolean> = emptyMap() ,
    val watchLaterError: String? ="",

    val isUserSeriesLoading: Boolean = false,
    val userSeriesError: String? = "",
    val userSeriesList: List<Series> = emptyList(),

    val searchSeriesList: List<Series> = emptyList(),
)