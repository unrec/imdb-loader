package com.unrec.imdb.search.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "basics")
class BasicsEntity(
    @Id
    val titleId: Long,
    val titleType: String,
    val primaryTitle: String,
    val originalTitle: String,
    val isAdult: Boolean?,
    val startYear: Int,
    val endYear: Int?,
    val runtimeMinutes: Int,
    val genres: String
)