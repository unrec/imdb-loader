package com.unrec.imdb.search.batch

import com.unrec.imdb.search.entity.RatingsEntity
import com.unrec.imdb.search.mapper.toEntity
import com.unrec.imdb.search.model.RatingsRecord
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
@ConditionalOnProperty(value = ["imdb.ratings.enabled"], havingValue = "true")
class RatingsBatchConfig {

    @Autowired
    private lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    private lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    private lateinit var gZipBufferedReaderFactory: GZipBufferedReaderFactory

    @Value("\${imdb.source}")
    private lateinit var sourcePath: String

    @Value("\${imdb.ratings.chunkSize}")
    private var chunkSize: Int = 1000

    @Bean
    fun ratingsItemReader(): FlatFileItemReader<RatingsRecord> {
        val mapper = BeanWrapperFieldSetMapper<RatingsRecord>()
        mapper.setTargetType(RatingsRecord::class.java)

        return FlatFileItemReaderBuilder<RatingsRecord>()
            .name("ratingsItemReader")
            .resource(FileSystemResource(Path.of(sourcePath, ratingsZip)))
            .bufferedReaderFactory(gZipBufferedReaderFactory)
            .linesToSkip(1)
            .delimited()
            .delimiter("\t")
            .quoteCharacter('★')
            .names(*ratingsHeaders)
            .fieldSetMapper(mapper)
            .build()
    }

    @Bean
    fun ratingsItemProcessor(): ItemProcessor<RatingsRecord, RatingsEntity> {
        return ItemProcessor { it.toEntity() }
    }

    @Bean
    fun ratingsItemWriter(dataSource: DataSource): JdbcBatchItemWriter<RatingsEntity> {
        return JdbcBatchItemWriterBuilder<RatingsEntity>()
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql(ratingsInsertQuery)
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun ratingsRecordsJob(listener: JobCompletionNotificationListener, ratingsReadStep: Step): Job? {
        return jobBuilderFactory["ratingsRecordsJob"]
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(ratingsReadStep)
            .end()
            .build()
    }

    @Bean
    fun ratingsReadStep(writer: JdbcBatchItemWriter<RatingsEntity>): Step {
        return stepBuilderFactory["ratingsRecordsJob"]
            .chunk<RatingsRecord, RatingsEntity>(chunkSize)
            .reader(ratingsItemReader())
            .processor(ratingsItemProcessor())
            .writer(writer)
            .build()
    }
}