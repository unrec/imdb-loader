package com.unrec.imdb.loader.record

import com.unrec.imdb.loader.entity.AkasEntity
import com.unrec.imdb.loader.entity.Entity

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
    override fun toEntity(): Entity = AkasEntity(
        titleId = titleId.removeLeadingChars().toInt(),
        ordering = ordering.toShort(),
        title = title,
        region = region,
        language = language.extractNonEmptyValue(),
        types = types.extractNonEmptyValue(),
        attributes = attributes.extractNonEmptyValue(),
        isOriginalTitle = isOriginalTitle.asBoolean()
    )
}