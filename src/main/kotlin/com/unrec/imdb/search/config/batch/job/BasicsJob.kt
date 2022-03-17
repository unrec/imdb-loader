package com.unrec.imdb.search.config.batch.job

import com.unrec.imdb.search.config.batch.BatchSettings
import com.unrec.imdb.search.config.batch.StructuredJob
import com.unrec.imdb.search.config.batch.constants.basicsHeaders
import com.unrec.imdb.search.config.batch.constants.basicsInsertQuery
import com.unrec.imdb.search.config.batch.constants.basicsZip
import com.unrec.imdb.search.entity.BasicsEntity
import com.unrec.imdb.search.record.BasicsRecord
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["imdb.basics.enabled"], havingValue = "true")
class BasicsJob : StructuredJob<BasicsRecord, BasicsEntity>(
    basicsZip, basicsHeaders, basicsInsertQuery, 10000,
    BatchSettings(
        reader = "basicsItemReader",
        processor = "basicsItemProcessor",
        writer = "basicsItemWriter",
        step = "basicsReadStep",
        job = "basicsRecordsJob"
    )
)