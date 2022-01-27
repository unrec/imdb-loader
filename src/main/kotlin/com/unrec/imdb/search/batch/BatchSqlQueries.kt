package com.unrec.imdb.search.batch

const val basicsInsertQuery = """
    INSERT INTO basics (title_id, title_type, primary_title, original_title, is_adult, start_year,end_year, runtime_minutes, genres)
    VALUES(:titleId, :titleType, :primaryTitle, :originalTitle, :isAdult, :startYear, :endYear, :runtimeMinutes, :genres)"""

const val ratingsInsertQuery = """
    INSERT INTO ratings (title_id, average_rating, num_votes)
    VALUES(:titleId, :averageRating, :numVotes)"""
