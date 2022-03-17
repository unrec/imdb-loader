package com.unrec.imdb.search.entity

data class CrewEntity(
    val titleId: Long,
    val directors: String?,
    val writers: String?,
) : Entity()