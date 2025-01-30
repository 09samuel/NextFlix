package com.nextflix.app.seriesList.data.remote.respond

data class SeriesListDto(
    val page: Int,
    val results: List<SeriesDto>,
    val total_pages: Int,
    val total_results: Int
)