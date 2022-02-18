package com.unrec.imdb.search.config.batch

import com.unrec.imdb.search.batch.GZipBufferedReaderFactory
import com.unrec.imdb.search.batch.JobCompletionNotificationListener
import com.unrec.imdb.search.config.batch.constants.akaInsertQuery
import com.unrec.imdb.search.config.batch.constants.akasHeaders
import com.unrec.imdb.search.config.batch.constants.akasZip
import com.unrec.imdb.search.entity.AkasEntity
import com.unrec.imdb.search.mapper.toEntity
import com.unrec.imdb.search.model.AkasRecord
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
@ConditionalOnProperty(value = ["imdb.akas.enabled"], havingValue = "true")
class AkasBatchConfig {

    @Autowired
    private lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    private lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    private lateinit var gZipBufferedReaderFactory: GZipBufferedReaderFactory

    @Value("\${imdb.source}")
    private lateinit var sourcePath: String

    @Value("\${imdb.akas.chunkSize}")
    private var chunkSize: Int = 1000

    @Bean
    fun akasItemReader(): FlatFileItemReader<AkasRecord> {
        val mapper = BeanWrapperFieldSetMapper<AkasRecord>()
        mapper.setTargetType(AkasRecord::class.java)

        return FlatFileItemReaderBuilder<AkasRecord>()
            .name("akasItemReader")
            .resource(FileSystemResource(Path.of(sourcePath, akasZip)))
            .bufferedReaderFactory(gZipBufferedReaderFactory)
            .linesToSkip(1)
            .delimited()
            .delimiter("\t")
            .quoteCharacter('☀')
            .names(*akasHeaders)
            .fieldSetMapper(mapper)
            .build()
    }

    @Bean
    fun akasItemProcessor(): ItemProcessor<AkasRecord, AkasEntity> {
        return ItemProcessor { it.toEntity() }
    }

    @Bean
    fun akasItemWriter(dataSource: DataSource): JdbcBatchItemWriter<AkasEntity> {
        return JdbcBatchItemWriterBuilder<AkasEntity>()
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql(akaInsertQuery)
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun akasRecordsJob(listener: JobCompletionNotificationListener, akasReadStep: Step): Job? {
        return jobBuilderFactory["akasRecordsJob"]
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(akasReadStep)
            .end()
            .build()
    }

    @Bean
    fun akasReadStep(writer: JdbcBatchItemWriter<AkasEntity>): Step {
        return stepBuilderFactory["akasReadStep"]
            .chunk<AkasRecord, AkasEntity>(chunkSize)
            .reader(akasItemReader())
            .processor(akasItemProcessor())
            .writer(writer)
            .build()
    }
}