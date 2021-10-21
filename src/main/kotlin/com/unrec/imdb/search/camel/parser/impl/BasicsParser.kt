package com.unrec.imdb.search.camel.parser.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvMappingException
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.unrec.imdb.search.camel.parser.FileParser
import com.unrec.imdb.search.exception.FileParseException
import com.unrec.imdb.search.model.BasicsRecord
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.io.InputStream

@Profile("camel")
@Component
class BasicsParser(
    private val basicsMapper: ObjectMapper,
    private val basicsSchema: CsvSchema,
) : FileParser<BasicsRecord> {

    override fun parseRecords(data: String): List<BasicsRecord> {
        return try {
            basicsMapper.readerFor(BasicsRecord::class.java)
                .with(basicsSchema)
                .readValues<BasicsRecord>(data)
                .readAll()
        } catch (e: CsvMappingException) {
            throw FileParseException("Wrong format of input data: ${e.message}")
        }
    }

    override fun parseRecords(data: InputStream): List<BasicsRecord> {
        return try {
            basicsMapper.readerFor(BasicsRecord::class.java)
                .with(basicsSchema)
                .readValues<BasicsRecord>(data)
                .readAll()
        } catch (e: CsvMappingException) {
            throw FileParseException("Wrong format of input data: ${e.message}")
        }
    }
}