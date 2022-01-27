package com.unrec.imdb.search.batch

import com.unrec.imdb.search.entity.BasicsEntity
import com.unrec.imdb.search.mapper.toEntity
import com.unrec.imdb.search.model.BasicsRecord
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
@ConditionalOnProperty(value = ["imdb.basics.enabled"], havingValue = "true")
class BasicsBatchConfig {

    @Autowired
    private lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    private lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    private lateinit var gZipBufferedReaderFactory: GZipBufferedReaderFactory

    @Value("\${imdb.source}")
    private lateinit var sourcePath: String

    @Value("\${imdb.basics.chunkSize}")
    private var chunkSize: Int = 1000

    @Bean
    fun basicsItemReader(): FlatFileItemReader<BasicsRecord> {
        val mapper = BeanWrapperFieldSetMapper<BasicsRecord>()
        mapper.setTargetType(BasicsRecord::class.java)

        return FlatFileItemReaderBuilder<BasicsRecord>()
            .name("basicsItemReader")
            .resource(FileSystemResource(Path.of(sourcePath, basicsZip)))
            .bufferedReaderFactory(gZipBufferedReaderFactory)
            .linesToSkip(1)
            .delimited()
            .delimiter("\t")
            .quoteCharacter('★')
            .names(*basicsHeaders)
            .fieldSetMapper(mapper)
            .build()
    }

    @Bean
    fun basicsItemProcessor(): ItemProcessor<BasicsRecord, BasicsEntity> {
        return ItemProcessor { it.toEntity() }
    }

    @Bean
    fun basicsItemWriter(dataSource: DataSource): JdbcBatchItemWriter<BasicsEntity> {
        return JdbcBatchItemWriterBuilder<BasicsEntity>()
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql(basicsInsertQuery)
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun basicsRecordsJob(listener: JobCompletionNotificationListener, basicsReadStep: Step): Job? {
        return jobBuilderFactory["basicsRecordsJob"]
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(basicsReadStep)
            .end()
            .build()
    }

    @Bean
    fun basicsReadStep(writer: JdbcBatchItemWriter<BasicsEntity>): Step {
        return stepBuilderFactory["basicsReadStep"]
            .chunk<BasicsRecord, BasicsEntity>(chunkSize)
            .reader(basicsItemReader())
            .processor(basicsItemProcessor())
            .writer(writer)
            .build()
    }
}