package com.unrec.imdb.search.batch

import com.unrec.imdb.search.entity.PrincipalsEntity
import com.unrec.imdb.search.mapper.toEntity
import com.unrec.imdb.search.model.PrincipalsRecord
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
@ConditionalOnProperty(value = ["imdb.principals.enabled"], havingValue = "true")
class PrincipalsBatchConfig {

    @Autowired
    private lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    private lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    private lateinit var gZipBufferedReaderFactory: GZipBufferedReaderFactory

    @Value("\${imdb.source}")
    private lateinit var sourcePath: String

    @Value("\${imdb.principals.chunkSize}")
    private var chunkSize: Int = 1000

    @Bean
    fun principalsItemReader(): FlatFileItemReader<PrincipalsRecord> {
        val mapper = BeanWrapperFieldSetMapper<PrincipalsRecord>()
        mapper.setTargetType(PrincipalsRecord::class.java)

        return FlatFileItemReaderBuilder<PrincipalsRecord>()
            .name("principalsItemReader")
            .resource(FileSystemResource(Path.of(sourcePath, principalsZip)))
            .bufferedReaderFactory(gZipBufferedReaderFactory)
            .linesToSkip(1)
            .delimited()
            .delimiter("\t")
            .quoteCharacter('☀')
            .names(*principalsHeaders)
            .fieldSetMapper(mapper)
            .build()
    }

    @Bean
    fun principalsItemProcessor(): ItemProcessor<PrincipalsRecord, PrincipalsEntity> {
        return ItemProcessor { it.toEntity() }
    }

    @Bean
    fun principalsItemWriter(dataSource: DataSource): JdbcBatchItemWriter<PrincipalsEntity> {
        return JdbcBatchItemWriterBuilder<PrincipalsEntity>()
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql(principalsInsertQuery)
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun principalsRecordsJob(listener: JobCompletionNotificationListener, principalsReadStep: Step): Job? {
        return jobBuilderFactory["principalsRecordsJob"]
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(principalsReadStep)
            .end()
            .build()
    }

    @Bean
    fun principalsReadStep(writer: JdbcBatchItemWriter<PrincipalsEntity>): Step {
        return stepBuilderFactory["principalsReadStep"]
            .chunk<PrincipalsRecord, PrincipalsEntity>(chunkSize)
            .reader(principalsItemReader())
            .processor(principalsItemProcessor())
            .writer(writer)
            .build()
    }
}