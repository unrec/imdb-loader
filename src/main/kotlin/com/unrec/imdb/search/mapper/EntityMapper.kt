package com.unrec.imdb.search.mapper

import com.unrec.imdb.search.entity.AkasEntity
import com.unrec.imdb.search.entity.BasicsEntity
import com.unrec.imdb.search.entity.CrewEntity
import com.unrec.imdb.search.entity.RatingsEntity
import com.unrec.imdb.search.exception.ConvertException
import com.unrec.imdb.search.model.AkasRecord
import com.unrec.imdb.search.model.BasicsRecord
import com.unrec.imdb.search.model.CrewRecord
import com.unrec.imdb.search.model.RatingsRecord

private val leadingRegex = "(nm|tt)0*".toRegex()

fun BasicsRecord.toEntity(): BasicsEntity {
    return BasicsEntity(
        this.tconst.removeLeadingChars().toLong(),
        this.titleType,
        this.primaryTitle,
        this.originalTitle,
        convertBoolean(this.isAdult),
        convertInteger(this.startYear),
        convertInteger(this.endYear),
        convertInteger(this.runtimeMinutes),
        this.genres
    )
}

fun RatingsRecord.toEntity(): RatingsEntity {
    return RatingsEntity(
        this.tconst.removeLeadingChars().toLong(),
        this.averageRating.toDouble(),
        this.numVotes.toInt(),
    )
}

fun CrewRecord.toEntity(): CrewEntity {
    return CrewEntity(
        this.tconst.removeLeadingChars().toLong(),
        this.writers.extractNonEmptyValue()?.removeLeadingChars(),
        this.directors.extractNonEmptyValue()?.removeLeadingChars()
    )
}

fun AkasRecord.toEntity(): AkasEntity {
    return AkasEntity(
        this.titleId.removeLeadingChars().toLong(),
        this.ordering.toInt(),
        this.title,
        this.region,
        this.language,
        this.types.extractNonEmptyValue(),
        this.attributes.extractNonEmptyValue(),
        convertBoolean(this.isOriginalTitle)
    )
}

private fun convertBoolean(input: String): Boolean? {
    return when (input) {
        "0" -> false
        "1" -> true
        "\\N" -> null
        else -> throw ConvertException("Incorrect boolean descriptor: $input")
    }
}

private fun convertInteger(input: String): Int? {
    return if (input == "\\N") null else try {
        input.toInt()
    } catch (e: NumberFormatException) {
        throw ConvertException("Incorrect boolean descriptor: $input")
    }
}

private fun String.extractNonEmptyValue() = if (this == "\\N") null else this

private fun String.removeLeadingChars() = this.replace(leadingRegex, "")


