package com.unrec.imdb.search.record

import com.unrec.imdb.search.entity.Entity
import com.unrec.imdb.search.entity.PrincipalsEntity

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
            tconst.removeLeadingChars().toLong(),
            ordering.asInteger(),
            nconst.removeLeadingChars().toLong(),
            category,
            job.extractNonEmptyValue(),
            characters.extractNonEmptyValue()
        )
    }
}