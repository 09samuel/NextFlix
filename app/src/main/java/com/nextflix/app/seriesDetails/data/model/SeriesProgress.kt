package com.nextflix.app.seriesDetails.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeriesProgress(
    @SerialName("progress_id")
    val id: Int? = null,
    @SerialName("watched_at")
    val watchedAt: String? = null,
//    @SerialName("user_id")
//    val userId: String,
    @SerialName("episode_id")
    val episodeId: Int,
    @SerialName("series_id")
    val seriesId: Int,
    @SerialName("season_no")
    val seasonNo: Int,
)