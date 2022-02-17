package com.unrec.imdb.search.entity

data class NameBasicsEntity(
    val nameId: Long,
    val primaryName: String,
    val birthYear: Int?,
    val deathYear: Int?,
    val primaryProfession: String?,
    val knownForTitles: String?,
)