package com.unrec.imdb.loader.entity

data class RatingsEntity(
    val titleId: Long,
    val averageRating: Double,
    val numVotes: Int,
) : Entity()