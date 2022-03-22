package com.unrec.imdb.loader.record

import com.unrec.imdb.loader.entity.CrewEntity
import com.unrec.imdb.loader.entity.Entity

data class CrewRecord(
    var tconst: String = "",
    var directors: String = "",
    var writers: String = ""
) : Record {
    override fun toEntity(): Entity {
        return CrewEntity(
            tconst.removeLeadingChars().toLong(),
            writers.extractNonEmptyValue()?.removeLeadingChars(),
            directors.extractNonEmptyValue()?.removeLeadingChars()
        )
    }
}