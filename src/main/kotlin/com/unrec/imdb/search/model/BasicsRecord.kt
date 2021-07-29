package com.unrec.imdb.search.model

import com.unrec.imdb.search.entity.Genre

data class BasicsRecord(
    val tconst: String,
    val titleType: String,
    val primaryTitle: String,
    val originalTitle: String,
    val isAdult: String,
    val startYear: String,
    val endYear: String,
    val runtimeMinutes: String,
    val genres: List<Genre>
)
