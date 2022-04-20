package com.unrec.imdb.loader.entity

data class PrincipalsEntity(
    val titleId: Int,
    val ordering: Short,
    val nameId: Int,
    val category: String,
    val job: String?,
    val characters: String?,
) : Entity()