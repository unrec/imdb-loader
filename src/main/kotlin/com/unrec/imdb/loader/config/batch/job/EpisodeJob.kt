package com.unrec.imdb.loader.config.batch.job

import com.unrec.imdb.loader.config.batch.BatchSettings
import com.unrec.imdb.loader.config.batch.StructuredJob
import com.unrec.imdb.loader.config.batch.constants.episodeHeaders
import com.unrec.imdb.loader.config.batch.constants.episodeZip
import com.unrec.imdb.loader.config.batch.constants.episodesInsertQuery
import com.unrec.imdb.loader.entity.EpisodeEntity
import com.unrec.imdb.loader.record.EpisodeRecord
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