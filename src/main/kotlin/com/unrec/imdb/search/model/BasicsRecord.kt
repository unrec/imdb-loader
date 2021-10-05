package com.unrec.imdb.search.model

data class BasicsRecord(
    var tconst: String = "",
    var titleType: String = "",
    var primaryTitle: String = "",
    var originalTitle: String = "",
    var isAdult: String = "",
    var startYear: String = "",
    var endYear: String = "",
    var runtimeMinutes: String = "",
    var genres: String = "",
)
