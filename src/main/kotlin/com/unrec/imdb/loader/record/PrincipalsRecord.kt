package com.unrec.imdb.loader.record

import com.unrec.imdb.loader.entity.Entity
import com.unrec.imdb.loader.entity.PrincipalsEntity

data class PrincipalsRecord(
    var tconst: String = "",
    var ordering: String = "",
    var nconst: String = "",
    var category: String = "",
    var job: String = "",
    var characters: String = "",
) : Record {
    override fun toEntity(): Entity {
        return PrincipalsEntity(
            tconst.removeLeadingChars().toInt(),
            ordering.toShort(),
            nconst.removeLeadingChars().toInt(),
            category,
            job.extractNonEmptyValue(),
            characters.extractNonEmptyValue()
        )
    }
}