package com.unrec.imdb.loader.entity

data class CrewEntity(
    val titleId: Long,
    val directors: String?,
    val writers: String?,
) : Entity()