package com.unrec.imdb.search.entity

data class EpisodeEntity(
    val titleId: Long,
    val parentId: Long,
    val seasonNumber: Int?,
    val episodeNumber: Int?,
)