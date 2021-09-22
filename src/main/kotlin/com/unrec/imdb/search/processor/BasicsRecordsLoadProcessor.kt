package com.unrec.imdb.search.processor

import com.unrec.imdb.search.entity.BasicsEntity
import com.unrec.imdb.search.mapper.toEntity
import com.unrec.imdb.search.model.BasicsRecord
import com.unrec.imdb.search.parser.impl.BasicsParser
import com.unrec.imdb.search.repository.BasicsEntityRepository
import lombok.RequiredArgsConstructor
import mu.KotlinLogging
import org.apache.camel.Exchange
import org.springframework.stereotype.Component
import java.io.File
import kotlin.system.measureTimeMillis

@RequiredArgsConstructor
@Component
class BasicsRecordsLoadProcessor(
    private val parser: BasicsParser,
    private val repository: BasicsEntityRepository,
) {

    private val logger = KotlinLogging.logger {}

    fun process(exchange: Exchange) {
        val unzippedFile = exchange.getProperty("unzippedFile") as String

        logger.info { "Starting to parse file: $unzippedFile" }
        val records: List<BasicsRecord>
        var timeInMillis = measureTimeMillis {
            records = parser.parseRecords(File(unzippedFile).inputStream().buffered(256 * 1024))
        }
        logger.info { "Parsing $unzippedFile took $timeInMillis ms" }
        logger.info { "Total records: ${records.size}" }

        logger.info { "Starting to map records from $unzippedFile" }
        val entities: List<BasicsEntity>
        timeInMillis = measureTimeMillis { entities = records.map { it.toEntity() } }
        logger.info { "Mapping records from $unzippedFile took $timeInMillis ms" }
        logger.info { "Total entities: ${entities.size} " }

        logger.info { "Starting to load entities from $unzippedFile" }
        timeInMillis = measureTimeMillis { repository.saveAll(entities) }
        logger.info { "Loading entities to Database took $timeInMillis ms" }
    }
}
