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
    override fun toEntity(): Entity = PrincipalsEntity(
        titleId = tconst.removeLeadingChars().toInt(),
        ordering = ordering.toShort(),
        nameId = nconst.removeLeadingChars().toInt(),
        category = category,
        job = job.extractNonEmptyValue(),
        characters = characters.extractNonEmptyValue()
    )
}