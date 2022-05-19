package com.unrec.imdb.loader.record

import com.unrec.imdb.loader.entity.Entity
import com.unrec.imdb.loader.entity.RatingsEntity

data class RatingsRecord(
    var tconst: String = "",
    var averageRating: String = "",
    var numVotes: String = "",
) : Record {
    override fun toEntity(): Entity = RatingsEntity(
        titleId = this.tconst.removeLeadingChars().toInt(),
        averageRating = this.averageRating.toFloat(),
        numVotes = this.numVotes.toInt(),
    )
}
