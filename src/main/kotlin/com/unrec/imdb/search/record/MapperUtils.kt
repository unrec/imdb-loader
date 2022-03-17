package com.unrec.imdb.search.record

import com.unrec.imdb.search.exception.ConvertException

private val leadingRegex = "(nm|tt)0*".toRegex()

fun String.asBoolean(): Boolean? {
    return when (this) {
        "0" -> false
        "1" -> true
        "\\N" -> null
        else -> throw ConvertException("Incorrect boolean descriptor: $this")
    }
}

fun String.asInteger(): Int? {
    return if (this == "\\N") null else try {
        this.toInt()
    } catch (e: NumberFormatException) {
        throw ConvertException("Incorrect boolean descriptor: $this")
    }
}

fun String.extractNonEmptyValue() = if (this == "\\N") null else this

fun String.removeLeadingChars() = this.replace(leadingRegex, "")


