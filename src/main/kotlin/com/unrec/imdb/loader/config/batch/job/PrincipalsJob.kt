package com.unrec.imdb.loader.config.batch.job

import com.unrec.imdb.loader.config.batch.BatchSettings
import com.unrec.imdb.loader.config.batch.StructuredJob
import com.unrec.imdb.loader.config.batch.constants.principalsHeaders
import com.unrec.imdb.loader.config.batch.constants.principalsInsertQuery
import com.unrec.imdb.loader.config.batch.constants.principalsZip
import com.unrec.imdb.loader.entity.PrincipalsEntity
import com.unrec.imdb.loader.record.PrincipalsRecord
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