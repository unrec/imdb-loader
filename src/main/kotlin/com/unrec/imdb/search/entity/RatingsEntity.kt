package com.unrec.imdb.search.entity

data class RatingsEntity(
    val titleId: Long,
    val averageRating: Double,
    val numVotes: Int,
)