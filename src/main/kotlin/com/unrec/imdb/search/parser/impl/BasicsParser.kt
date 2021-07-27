package com.unrec.imdb.search.parser.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.unrec.imdb.search.model.BasicsRecord
import com.unrec.imdb.search.parser.FileParser
import org.springframework.stereotype.Component

@Component
class BasicsParser(
    private val basicsMapper: ObjectMapper,
    private val basicsSchema: CsvSchema
) : FileParser<BasicsRecord> {

    private val headers =
        "tconst\ttitleType\tprimaryTitle\toriginalTitle\tisAdult\tstartYear\tendYear\truntimeMinutes\tgenres\n"

    override fun parseRecords(data: String): List<BasicsRecord> {
        return basicsMapper.readerFor(BasicsRecord::class.java)
            .with(basicsSchema)
            .readValues<BasicsRecord>(data)
            .readAll()
    }
}