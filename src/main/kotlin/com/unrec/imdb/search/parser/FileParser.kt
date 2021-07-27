package com.unrec.imdb.search.parser

interface FileParser<T> {
    fun parseRecords(data: String): List<T>
}