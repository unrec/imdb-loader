package com.unrec.imdb.search.batch

val basicsHeaders = arrayOf(
    "tconst",
    "titleType",
    "primaryTitle",
    "originalTitle",
    "isAdult",
    "startYear",
    "endYear",
    "runtimeMinutes",
    "genres"
)

val ratingsHeaders = arrayOf(
    "tconst",
    "averageRating",
    "numVotes"
)

val crewHeaders = arrayOf(
    "tconst",
    "directors",
    "writers"
)

val akasHeaders = arrayOf(
    "titleId",
    "ordering",
    "title",
    "region",
    "language",
    "types",
    "attributes",
    "isOriginalTitle"
)

val principalsHeaders = arrayOf(
    "tconst",
    "ordering",
    "nconst",
    "category",
    "job",
    "characters"
)

val episodeHeaders = arrayOf(
    "tconst",
    "parentTconst",
    "seasonNumber",
    "episodeNumber"
)