package com.nextflix.app.seriesDetails.domain.model

data class Recommendations(
    val id: Int,
    val poster_path: String,
    val name: String,
    val watch_later: Boolean
)
