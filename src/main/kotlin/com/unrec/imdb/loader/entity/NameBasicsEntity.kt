package com.unrec.imdb.loader.entity

data class NameBasicsEntity(
    val nameId: Int,
    val primaryName: String,
    val birthYear: Short?,
    val deathYear: Short?,
    val primaryProfession: String?,
    val knownForTitles: String?,
) : Entity()