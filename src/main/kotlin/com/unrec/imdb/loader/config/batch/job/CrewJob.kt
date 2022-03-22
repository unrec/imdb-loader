package com.unrec.imdb.loader.config.batch.job

import com.unrec.imdb.loader.config.batch.BatchSettings
import com.unrec.imdb.loader.config.batch.StructuredJob
import com.unrec.imdb.loader.config.batch.constants.crewHeaders
import com.unrec.imdb.loader.config.batch.constants.crewInsertQuery
import com.unrec.imdb.loader.config.batch.constants.crewZip
import com.unrec.imdb.loader.entity.CrewEntity
import com.unrec.imdb.loader.record.CrewRecord
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