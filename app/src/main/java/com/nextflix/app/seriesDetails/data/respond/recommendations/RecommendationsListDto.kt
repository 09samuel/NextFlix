package com.nextflix.app.seriesDetails.data.respond.recommendations

data class RecommendationsListDto(
    val page: Int?,
    val results: List<RecommendationsDto>?,
    val total_pages: Int?,
    val total_results: Int?
)