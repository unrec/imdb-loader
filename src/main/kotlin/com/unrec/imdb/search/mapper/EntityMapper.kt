package com.unrec.imdb.search.mapper

import com.unrec.imdb.search.entity.BasicsEntity
import com.unrec.imdb.search.exception.ConvertException
import com.unrec.imdb.search.model.BasicsRecord

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

fun convertBoolean(input: String): Boolean? {
    return when (input) {
        "0" -> false
        "1" -> true
        "\\N" -> null
        else -> throw ConvertException("Incorrect boolean descriptor: $input")
    }
}

fun convertInteger(input: String): Int? {
    return if (input == "\\N") null else try {
        input.toInt()
    } catch (e: NumberFormatException) {
        throw ConvertException("Incorrect boolean descriptor: $input")
    }
}
