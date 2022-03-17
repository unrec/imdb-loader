package com.unrec.imdb.search.config.batch.job

import com.unrec.imdb.search.config.batch.BatchSettings
import com.unrec.imdb.search.config.batch.StructuredJob
import com.unrec.imdb.search.config.batch.constants.ratingsHeaders
import com.unrec.imdb.search.config.batch.constants.ratingsInsertQuery
import com.unrec.imdb.search.config.batch.constants.ratingsZip
import com.unrec.imdb.search.entity.RatingsEntity
import com.unrec.imdb.search.record.RatingsRecord
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