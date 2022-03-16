package com.unrec.imdb.search.record

import com.unrec.imdb.search.entity.BasicsEntity
import com.unrec.imdb.search.entity.Entity

data class BasicsRecord(
    var tconst: String = "",
    var titleType: String = "",
    var primaryTitle: String = "",
    var originalTitle: String = "",
    var isAdult: String = "",
    var startYear: String = "",
    var endYear: String = "",
    var runtimeMinutes: String = "",
    var genres: String = "",
) : Record {
    override fun toEntity(): Entity {
        return BasicsEntity(
            tconst.removeLeadingChars().toLong(),
            titleType,
            primaryTitle,
            originalTitle,
            isAdult.asBoolean(),
            startYear.asInteger(),
            endYear.asInteger(),
            runtimeMinutes.asInteger(),
            genres
        )
    }
}
