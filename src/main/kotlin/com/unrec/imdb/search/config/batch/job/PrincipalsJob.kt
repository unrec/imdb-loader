package com.unrec.imdb.search.config.batch.job

import com.unrec.imdb.search.config.batch.BatchSettings
import com.unrec.imdb.search.config.batch.StructuredJob
import com.unrec.imdb.search.config.batch.constants.principalsHeaders
import com.unrec.imdb.search.config.batch.constants.principalsInsertQuery
import com.unrec.imdb.search.config.batch.constants.principalsZip
import com.unrec.imdb.search.entity.PrincipalsEntity
import com.unrec.imdb.search.record.PrincipalsRecord
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["imdb.principals.enabled"], havingValue = "true")
class PrincipalsJob : StructuredJob<PrincipalsRecord, PrincipalsEntity>(
    principalsZip, principalsHeaders, principalsInsertQuery, 10000,
    BatchSettings(
        reader = "principalsItemReader",
        processor = "principalsItemProcessor",
        writer = "principalsItemWriter",
        step = "principalsReadStep",
        job = "principalsRecordsJob"
    )
)