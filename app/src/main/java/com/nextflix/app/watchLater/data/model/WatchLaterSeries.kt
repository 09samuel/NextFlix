package com.nextflix.app.watchLater.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchLaterSeries (
    //val id: Int? = null,
//    @SerialName("created_at")
//    val createdAt: String? =null,
//    @SerialName("user_id")
//    val userId: String,
    @SerialName("series_id")
    val seriesId: Int,
    @SerialName("series_name")
    val seriesName: String,
    @SerialName("poster_path")
    val posterPath: String,

)