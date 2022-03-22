package com.unrec.imdb.loader.config.batch.job

import com.unrec.imdb.loader.config.batch.BatchSettings
import com.unrec.imdb.loader.config.batch.StructuredJob
import com.unrec.imdb.loader.config.batch.constants.ratingsHeaders
import com.unrec.imdb.loader.config.batch.constants.ratingsInsertQuery
import com.unrec.imdb.loader.config.batch.constants.ratingsZip
import com.unrec.imdb.loader.entity.RatingsEntity
import com.unrec.imdb.loader.record.RatingsRecord
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["imdb.ratings.enabled"], havingValue = "true")
class RatingsJob : StructuredJob<RatingsRecord, RatingsEntity>(
    ratingsZip, ratingsHeaders, ratingsInsertQuery, 10000,
    BatchSettings(
        reader = "ratingsItemReader",
        processor = "ratingsItemProcessor",
        writer = "ratingsItemWriter",
        step = "ratingsReadStep",
        job = "ratingsRecordsJob"
    )
)