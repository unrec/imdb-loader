package com.unrec.imdb.loader.config.batch.job

import com.unrec.imdb.loader.config.batch.BatchSettings
import com.unrec.imdb.loader.config.batch.StructuredJob
import com.unrec.imdb.loader.config.batch.constants.akaInsertQuery
import com.unrec.imdb.loader.config.batch.constants.akasHeaders
import com.unrec.imdb.loader.config.batch.constants.akasZip
import com.unrec.imdb.loader.entity.AkasEntity
import com.unrec.imdb.loader.record.AkasRecord
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["imdb.akas.enabled"], havingValue = "true")
class AkaJob : StructuredJob<AkasRecord, AkasEntity>(
    akasZip, akasHeaders, akaInsertQuery, 10000,
    BatchSettings(
        reader = "akasItemReader",
        processor = "akasItemProcessor",
        writer = "akasItemWriter",
        step = "akasReadStep",
        job = "akasRecordsJob"
    )
)