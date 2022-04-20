package com.unrec.imdb.loader.entity

data class EpisodeEntity(
    val titleId: Int,
    val parentId: Int,
    val seasonNumber: Short?,
    val episodeNumber: Int?,
) : Entity()