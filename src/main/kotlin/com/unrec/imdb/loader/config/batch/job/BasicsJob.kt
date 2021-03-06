package com.unrec.imdb.loader.config.batch.job

import com.unrec.imdb.loader.config.batch.BatchSettings
import com.unrec.imdb.loader.config.batch.StructuredJob
import com.unrec.imdb.loader.config.batch.constants.basicsHeaders
import com.unrec.imdb.loader.config.batch.constants.basicsInsertQuery
import com.unrec.imdb.loader.config.batch.constants.basicsZip
import com.unrec.imdb.loader.entity.BasicsEntity
import com.unrec.imdb.loader.record.BasicsRecord
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