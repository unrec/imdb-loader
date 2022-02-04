package com.unrec.imdb.search.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "crew")
class CrewEntity(
    @Id
    val titleId: Long,
    val directors: String?,
    val writers: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CrewEntity

        if (titleId != other.titleId) return false

        return true
    }

    override fun hashCode(): Int {
        return titleId.hashCode()
    }
}