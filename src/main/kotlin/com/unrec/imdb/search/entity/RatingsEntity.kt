package com.unrec.imdb.search.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "ratings")
class RatingsEntity(
    @Id
    val titleId: Long,
    val averageRating: Double,
    val numVotes: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RatingsEntity

        if (titleId != other.titleId) return false

        return true
    }

    override fun hashCode(): Int {
        return titleId.hashCode()
    }
}