package com.unrec.imdb.search.mapper

import com.unrec.imdb.search.entity.BasicsEntity
import com.unrec.imdb.search.entity.RatingsEntity
import com.unrec.imdb.search.exception.ConvertException
import com.unrec.imdb.search.model.BasicsRecord
import com.unrec.imdb.search.model.RatingsRecord

fun BasicsRecord.toEntity(): BasicsEntity {
    return BasicsEntity(
        this.tconst.substring(2).toLong(),
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
        this.tconst.substring(2).toLong(),
        this.averageRating.toDouble(),
        this.numVotes.toInt(),
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
