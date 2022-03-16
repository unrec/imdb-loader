package com.unrec.imdb.search.record

import com.unrec.imdb.search.entity.AkasEntity
import com.unrec.imdb.search.entity.Entity

data class AkasRecord(
    var titleId: String = "",
    var ordering: String = "",
    var title: String = "",
    var region: String = "",
    var language: String = "",
    var types: String = "",
    var attributes: String = "",
    var isOriginalTitle: String = "",
) : Record {
    override fun toEntity(): Entity {
        return AkasEntity(
            titleId.removeLeadingChars().toLong(),
            ordering.toInt(),
            title,
            region,
            language,
            types.extractNonEmptyValue(),
            attributes.extractNonEmptyValue(),
            isOriginalTitle.asBoolean()
        )
    }
}