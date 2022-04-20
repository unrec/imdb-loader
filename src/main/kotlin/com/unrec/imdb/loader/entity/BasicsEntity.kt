package com.unrec.imdb.loader.entity

data class BasicsEntity(
    val titleId: Int,
    val titleType: String,
    val primaryTitle: String,
    val originalTitle: String,
    val isAdult: Boolean?,
    val startYear: Short?,
    val endYear: Short?,
    val runtimeMinutes: Int?,
    val genres: String,
) : Entity()