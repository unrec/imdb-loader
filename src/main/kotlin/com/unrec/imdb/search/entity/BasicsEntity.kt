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
    val startYear: Int?,
    val endYear: Int?,
    val runtimeMinutes: Int?,
    val genres: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BasicsEntity
        if (titleId != other.titleId) return false
        if (titleType != other.titleType) return false
        if (primaryTitle != other.primaryTitle) return false
        if (originalTitle != other.originalTitle) return false
        if (isAdult != other.isAdult) return false
        if (startYear != other.startYear) return false
        if (endYear != other.endYear) return false
        if (runtimeMinutes != other.runtimeMinutes) return false
        if (genres != other.genres) return false
        return true
    }

    override fun hashCode(): Int {
        var result = titleId.hashCode()
        result = 31 * result + titleType.hashCode()
        result = 31 * result + primaryTitle.hashCode()
        result = 31 * result + originalTitle.hashCode()
        result = 31 * result + (isAdult?.hashCode() ?: 0)
        result = 31 * result + (endYear ?: 0)
        result = 31 * result + (endYear ?: 0)
        result = 31 * result + (runtimeMinutes ?: 0)
        result = 31 * result + genres.hashCode()
        return result
    }
}