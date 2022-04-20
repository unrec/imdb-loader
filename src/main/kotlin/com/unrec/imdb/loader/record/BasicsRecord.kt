package com.unrec.imdb.loader.record

import com.unrec.imdb.loader.entity.BasicsEntity
import com.unrec.imdb.loader.entity.Entity

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
            tconst.removeLeadingChars().toInt(),
            titleType,
            primaryTitle,
            originalTitle,
            isAdult.asBoolean(),
            startYear.asShort(),
            endYear.asShort(),
            runtimeMinutes.asInteger(),
            genres
        )
    }
}
