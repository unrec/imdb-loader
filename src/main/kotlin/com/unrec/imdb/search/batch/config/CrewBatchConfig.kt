package com.unrec.imdb.search.batch.config

import com.unrec.imdb.search.batch.*
import com.unrec.imdb.search.entity.CrewEntity
import com.unrec.imdb.search.mapper.toEntity
import com.unrec.imdb.search.model.CrewRecord
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
@ConditionalOnProperty(value = ["imdb.crew.enabled"], havingValue = "true")
class CrewBatchConfig {

    @Autowired
    private lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    private lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    private lateinit var gZipBufferedReaderFactory: GZipBufferedReaderFactory

    @Value("\${imdb.source}")
    private lateinit var sourcePath: String

    @Value("\${imdb.crew.chunkSize}")
    private var chunkSize: Int = 1000

    @Bean
    fun crewItemReader(): FlatFileItemReader<CrewRecord> {
        val mapper = BeanWrapperFieldSetMapper<CrewRecord>()
        mapper.setTargetType(CrewRecord::class.java)

        return FlatFileItemReaderBuilder<CrewRecord>()
            .name("crewItemReader")
            .resource(FileSystemResource(Path.of(sourcePath, crewZip)))
            .bufferedReaderFactory(gZipBufferedReaderFactory)
            .linesToSkip(1)
            .delimited()
            .delimiter("\t")
            .quoteCharacter('★')
            .names(*crewHeaders)
            .fieldSetMapper(mapper)
            .build()
    }

    @Bean
    fun crewItemProcessor(): ItemProcessor<CrewRecord, CrewEntity> {
        return ItemProcessor { it.toEntity() }
    }

    @Bean
    fun crewItemWriter(dataSource: DataSource): JdbcBatchItemWriter<CrewEntity> {
        return JdbcBatchItemWriterBuilder<CrewEntity>()
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql(crewInsertQuery)
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun crewRecordsJob(listener: JobCompletionNotificationListener, crewReadStep: Step): Job? {
        return jobBuilderFactory["crewRecordsJob"]
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(crewReadStep)
            .end()
            .build()
    }

    @Bean
    fun crewReadStep(writer: JdbcBatchItemWriter<CrewEntity>): Step {
        return stepBuilderFactory["crewReadStep"]
            .chunk<CrewRecord, CrewEntity>(chunkSize)
            .reader(crewItemReader())
            .processor(crewItemProcessor())
            .writer(writer)
            .build()
    }
}