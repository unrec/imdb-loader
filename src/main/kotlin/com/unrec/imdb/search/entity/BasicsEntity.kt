package com.unrec.imdb.search.entity

data class BasicsEntity(
    val titleId: Long,
    val titleType: String,
    val primaryTitle: String,
    val originalTitle: String,
    val isAdult: Boolean?,
    val startYear: Int?,
    val endYear: Int?,
    val runtimeMinutes: Int?,
    val genres: String,
)