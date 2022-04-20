package com.unrec.imdb.loader.entity

data class RatingsEntity(
    val titleId: Int,
    val averageRating: Float,
    val numVotes: Int,
) : Entity()