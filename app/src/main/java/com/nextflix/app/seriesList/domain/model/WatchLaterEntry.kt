package com.nextflix.app.seriesList.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WatchLaterEntry(
    val series_id: Int,
    val series_name: String,
    val poster_path: String,
)