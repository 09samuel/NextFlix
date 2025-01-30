package com.nextflix.app.seriesDetails.data.respond.watchProvider

data class SeriesWatchProvidersDto(
    val id: Int,
    val results: Map<String, CountryWatchProvider>
)


