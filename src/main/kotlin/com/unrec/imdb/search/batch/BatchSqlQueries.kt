package com.unrec.imdb.search.batch

const val basicsInsertQuery = """
    INSERT INTO basics (title_id, title_type, primary_title, original_title, is_adult, start_year,end_year, runtime_minutes, genres)
    VALUES(:titleId, :titleType, :primaryTitle, :originalTitle, :isAdult, :startYear, :endYear, :runtimeMinutes, :genres)"""

const val ratingsInsertQuery = """
    INSERT INTO ratings (title_id, average_rating, num_votes)
    VALUES(:titleId, :averageRating, :numVotes)"""

const val crewInsertQuery = """
    INSERT INTO crew (title_id, directors, writers)
    VALUES(:titleId, :directors, :writers)"""

const val akaInsertQuery = """
    INSERT INTO akas (title_id, ordering, title, region, language, types,attributes,is_original_title)
    VALUES(:titleId, :ordering, :title, :region, :language, string_to_array(:types,','), string_to_array(:attributes, ','), :isOriginalTitle)"""

const val principalsInsertQuery = """
    INSERT INTO principals (title_id, ordering, name_id, category, job, characters)
    VALUES(:titleId, :ordering, :nameId, :category, :job, :characters)"""


const val episodesInsertQuery = """
    INSERT INTO episodes (title_id, parent_id, season_number, episode_number)
    VALUES(:titleId, :parentId, :seasonNumber, :episodeNumber)"""