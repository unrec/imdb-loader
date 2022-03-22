package com.unrec.imdb.loader.entity

data class PrincipalsEntity(
    val titleId: Long,
    val ordering: Int?,
    val nameId: Long,
    val category: String,
    val job: String?,
    val characters: String?,
) : Entity()