package com.unrec.imdb.loader.record

import com.unrec.imdb.loader.entity.Entity
import com.unrec.imdb.loader.entity.EpisodeEntity

data class EpisodeRecord(
    var tconst: String = "",
    var parentTconst: String = "",
    var seasonNumber: String = "",
    var episodeNumber: String = "",
) : Record {
    override fun toEntity(): Entity = EpisodeEntity(
        titleId = tconst.removeLeadingChars().toInt(),
        parentId = parentTconst.removeLeadingChars().toInt(),
        seasonNumber = seasonNumber.asShort(),
        episodeNumber = episodeNumber.asInteger()
    )
}