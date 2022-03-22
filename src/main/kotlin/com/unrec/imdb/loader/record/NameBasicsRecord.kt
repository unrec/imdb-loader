package com.unrec.imdb.loader.record

import com.unrec.imdb.loader.entity.Entity
import com.unrec.imdb.loader.entity.NameBasicsEntity

data class NameBasicsRecord(
    var nconst: String = "",
    var primaryName: String = "",
    var birthYear: String = "",
    var deathYear: String = "",
    var primaryProfession: String = "",
    var knownForTitles: String = "",
) : Record {
    override fun toEntity(): Entity {
        return NameBasicsEntity(
            nconst.removeLeadingChars().toLong(),
            primaryName,
            birthYear.asInteger(),
            birthYear.asInteger(),
            primaryProfession.extractNonEmptyValue(),
            knownForTitles.extractNonEmptyValue()?.removeLeadingChars()
        )
    }
}