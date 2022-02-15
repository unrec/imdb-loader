package com.unrec.imdb.search.batch

import com.unrec.imdb.search.entity.EpisodeEntity
import com.unrec.imdb.search.mapper.toEntity
import com.unrec.imdb.search.model.EpisodeRecord
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.FileSystemResource
import java.nio.file.Path
import javax.sql.DataSource

@Profile("batch")
@EnableBatchProcessing
@Configuration
@ConditionalOnProperty(value = ["imdb.episode.enabled"], havingValue = "true")
class EpisodeBatchConfig {

    @Autowired
    private lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    private lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    private lateinit var gZipBufferedReaderFactory: GZipBufferedReaderFactory

    @Value("\${imdb.source}")
    private lateinit var sourcePath: String

    @Value("\${imdb.episode.chunkSize}")
    private var chunkSize: Int = 1000

    @Bean
    fun episodeItemReader(): FlatFileItemReader<EpisodeRecord> {
        val mapper = BeanWrapperFieldSetMapper<EpisodeRecord>()
        mapper.setTargetType(EpisodeRecord::class.java)

        return FlatFileItemReaderBuilder<EpisodeRecord>()
            .name("episodeItemReader")
            .resource(FileSystemResource(Path.of(sourcePath, episodeZip)))
            .bufferedReaderFactory(gZipBufferedReaderFactory)
            .linesToSkip(1)
            .delimited()
            .delimiter("\t")
            .quoteCharacter('☀')
            .names(*episodeHeaders)
            .fieldSetMapper(mapper)
            .build()
    }

    @Bean
    fun episodeItemProcessor(): ItemProcessor<EpisodeRecord, EpisodeEntity> {
        return ItemProcessor { it.toEntity() }
    }

    @Bean
    fun episodeItemWriter(dataSource: DataSource): JdbcBatchItemWriter<EpisodeEntity> {
        return JdbcBatchItemWriterBuilder<EpisodeEntity>()
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql(episodesInsertQuery)
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun episodeRecordsJob(listener: JobCompletionNotificationListener, episodeReadStep: Step): Job? {
        return jobBuilderFactory["episodeRecordsJob"]
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(episodeReadStep)
            .end()
            .build()
    }

    @Bean
    fun episodeReadStep(writer: JdbcBatchItemWriter<EpisodeEntity>): Step {
        return stepBuilderFactory["episodeReadStep"]
            .chunk<EpisodeRecord, EpisodeEntity>(chunkSize)
            .reader(episodeItemReader())
            .processor(episodeItemProcessor())
            .writer(writer)
            .build()
    }
}