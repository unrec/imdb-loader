package com.unrec.imdb.search.entity

data class AkasEntity(
    val titleId: Long,
    val ordering: Int,
    val title: String,
    val region: String,
    val language: String?,
    val types: String?,
    val attributes: String?,
    val isOriginalTitle: Boolean?,
) : Entity()