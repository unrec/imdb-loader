package com.unrec.imdb.search.config.batch.job

import com.unrec.imdb.search.config.batch.BatchSettings
import com.unrec.imdb.search.config.batch.StructuredJob
import com.unrec.imdb.search.config.batch.constants.crewHeaders
import com.unrec.imdb.search.config.batch.constants.crewInsertQuery
import com.unrec.imdb.search.config.batch.constants.crewZip
import com.unrec.imdb.search.entity.CrewEntity
import com.unrec.imdb.search.record.CrewRecord
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["imdb.crew.enabled"], havingValue = "true")
class CrewJob : StructuredJob<CrewRecord, CrewEntity>(
    crewZip, crewHeaders, crewInsertQuery, 10000,
    BatchSettings(
        reader = "crewItemReader",
        processor = "crewItemProcessor",
        writer = "crewItemWriter",
        step = "crewReadStep",
        job = "crewRecordsJob"
    )
)