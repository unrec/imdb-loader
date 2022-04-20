package com.unrec.imdb.loader.entity

data class CrewEntity(
    val titleId: Int,
    val directors: String?,
    val writers: String?,
) : Entity()