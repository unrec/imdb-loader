package com.unrec.imdb.search.config.batch.job

import com.unrec.imdb.search.config.batch.BatchSettings
import com.unrec.imdb.search.config.batch.StructuredJob
import com.unrec.imdb.search.config.batch.constants.episodeHeaders
import com.unrec.imdb.search.config.batch.constants.episodeZip
import com.unrec.imdb.search.config.batch.constants.episodesInsertQuery
import com.unrec.imdb.search.entity.EpisodeEntity
import com.unrec.imdb.search.record.EpisodeRecord
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(value = ["imdb.episode.enabled"], havingValue = "true")
class EpisodeJob : StructuredJob<EpisodeRecord, EpisodeEntity>(
    episodeZip, episodeHeaders, episodesInsertQuery, 10000,
    BatchSettings(
        reader = "episodeItemReader",
        processor = "episodeItemProcessor",
        writer = "episodeItemWriter",
        step = "episodeReadStep",
        job = "episodeRecordsJob"
    )
)