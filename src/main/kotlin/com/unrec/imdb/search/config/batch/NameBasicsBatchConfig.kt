package com.unrec.imdb.search.config.batch

import com.unrec.imdb.search.batch.GZipBufferedReaderFactory
import com.unrec.imdb.search.batch.JobCompletionNotificationListener
import com.unrec.imdb.search.config.batch.constants.nameBasicsHeaders
import com.unrec.imdb.search.config.batch.constants.nameBasicsInsertQuery
import com.unrec.imdb.search.config.batch.constants.nameBasicsZip
import com.unrec.imdb.search.entity.NameBasicsEntity
import com.unrec.imdb.search.mapper.toEntity
import com.unrec.imdb.search.model.NameBasicsRecord
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
@ConditionalOnProperty(value = ["imdb.nameBasics.enabled"], havingValue = "true")
class NameBasicsBatchConfig {

    @Autowired
    private lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    private lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    private lateinit var gZipBufferedReaderFactory: GZipBufferedReaderFactory

    @Value("\${imdb.source}")
    private lateinit var sourcePath: String

    @Value("\${imdb.nameBasics.chunkSize}")
    private var chunkSize: Int = 1000

    @Bean
    fun nameBasicsItemReader(): FlatFileItemReader<NameBasicsRecord> {
        val mapper = BeanWrapperFieldSetMapper<NameBasicsRecord>()
        mapper.setTargetType(NameBasicsRecord::class.java)

        return FlatFileItemReaderBuilder<NameBasicsRecord>()
            .name("nameBasicsItemReader")
            .resource(FileSystemResource(Path.of(sourcePath, nameBasicsZip)))
            .bufferedReaderFactory(gZipBufferedReaderFactory)
            .linesToSkip(1)
            .delimited()
            .delimiter("\t")
            .quoteCharacter('☀')
            .names(*nameBasicsHeaders)
            .fieldSetMapper(mapper)
            .build()
    }

    @Bean
    fun nameBasicsItemProcessor(): ItemProcessor<NameBasicsRecord, NameBasicsEntity> {
        return ItemProcessor { it.toEntity() }
    }

    @Bean
    fun nameBasicsItemWriter(dataSource: DataSource): JdbcBatchItemWriter<NameBasicsEntity> {
        return JdbcBatchItemWriterBuilder<NameBasicsEntity>()
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql(nameBasicsInsertQuery)
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun nameBasicsRecordsJob(listener: JobCompletionNotificationListener, nameBasicsReadStep: Step): Job? {
        return jobBuilderFactory["nameBasicsRecordsJob"]
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(nameBasicsReadStep)
            .end()
            .build()
    }

    @Bean
    fun nameBasicsReadStep(writer: JdbcBatchItemWriter<NameBasicsEntity>): Step {
        return stepBuilderFactory["nameBasicsReadStep"]
            .chunk<NameBasicsRecord, NameBasicsEntity>(chunkSize)
            .reader(nameBasicsItemReader())
            .processor(nameBasicsItemProcessor())
            .writer(writer)
            .build()
    }
}