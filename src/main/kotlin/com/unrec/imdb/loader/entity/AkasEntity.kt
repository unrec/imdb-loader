package com.unrec.imdb.loader.entity

data class AkasEntity(
    val titleId: Int,
    val ordering: Short,
    val title: String,
    val region: String,
    val language: String?,
    val types: String?,
    val attributes: String?,
    val isOriginalTitle: Boolean?,
) : Entity()