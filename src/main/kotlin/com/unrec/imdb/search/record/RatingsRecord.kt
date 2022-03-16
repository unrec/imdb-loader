package com.unrec.imdb.search.record

import com.unrec.imdb.search.entity.Entity
import com.unrec.imdb.search.entity.RatingsEntity

data class RatingsRecord(
    var tconst: String = "",
    var averageRating: String = "",
    var numVotes: String = "",
) : Record {
    override fun toEntity(): Entity {
        return RatingsEntity(
            this.tconst.removeLeadingChars().toLong(),
            this.averageRating.toDouble(),
            this.numVotes.toInt(),
        )
    }
}
