package com.unrec.imdb.search.parser

import java.io.InputStream

interface FileParser<T> {
    fun parseRecords(data: String): List<T>
    fun parseRecords(data: InputStream): List<T>
}