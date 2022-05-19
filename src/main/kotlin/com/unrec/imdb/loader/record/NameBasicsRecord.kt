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
    override fun toEntity(): Entity = NameBasicsEntity(
        nameId = nconst.removeLeadingChars().toInt(),
        primaryName = primaryName,
        birthYear = birthYear.asShort(),
        deathYear = deathYear.asShort(),
        primaryProfession = primaryProfession.extractNonEmptyValue(),
        knownForTitles = knownForTitles.extractNonEmptyValue()?.removeLeadingChars()
    )
}