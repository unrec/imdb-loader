package com.unrec.imdb.search.config.batch.job

import com.unrec.imdb.search.config.batch.BatchSettings
import com.unrec.imdb.search.config.batch.StructuredJob
import com.unrec.imdb.search.config.batch.constants.nameBasicsHeaders
import com.unrec.imdb.search.config.batch.constants.nameBasicsInsertQuery
import com.unrec.imdb.search.config.batch.constants.nameBasicsZip
import com.unrec.imdb.search.entity.NameBasicsEntity
import com.unrec.imdb.search.record.NameBasicsRecord
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["imdb.nameBasics.enabled"], havingValue = "true")
class NameBasicsJob : StructuredJob<NameBasicsRecord, NameBasicsEntity>(
    nameBasicsZip, nameBasicsHeaders, nameBasicsInsertQuery, 10000,
    BatchSettings(
        reader = "nameBasicsItemReader",
        processor = "nameBasicsItemProcessor",
        writer = "nameBasicsItemWriter",
        step = "nameBasicsReadStep",
        job = "nameBasicsRecordsJob"
    )
)