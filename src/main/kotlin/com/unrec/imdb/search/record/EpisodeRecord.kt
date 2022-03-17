package com.unrec.imdb.search.record

import com.unrec.imdb.search.entity.Entity
import com.unrec.imdb.search.entity.EpisodeEntity

data class EpisodeRecord(
    var tconst: String = "",
    var parentTconst: String = "",
    var seasonNumber: String = "",
    var episodeNumber: String = "",
) : Record {
    override fun toEntity(): Entity {
        return EpisodeEntity(
            tconst.removeLeadingChars().toLong(),
            parentTconst.removeLeadingChars().toLong(),
            seasonNumber.asInteger(),
            episodeNumber.asInteger()
        )
    }
}