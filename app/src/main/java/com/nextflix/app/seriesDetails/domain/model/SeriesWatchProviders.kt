package com.nextflix.app.seriesDetails.domain.model

data class SeriesWatchProviders (
    val display_priority: Int,
    val logo_path: String,
    val provider_name: String,
    val link: String,
    val provider_type: String
)