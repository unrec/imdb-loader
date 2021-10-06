package com.unrec.imdb.search.batch

import com.unrec.imdb.search.entity.BasicsEntity
import com.unrec.imdb.search.model.BasicsRecord
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import javax.sql.DataSource

@Profile("batch")
@EnableBatchProcessing
@Configuration
class BasicsBatchConfig {

    @Autowired
    private lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    private lateinit var stepBuilderFactory: StepBuilderFactory

    val basicsHeaders = arrayOf(
        "tconst",
        "titleType",
        "primaryTitle",
        "originalTitle",
        "isAdult",
        "startYear",
        "endYear",
        "runtimeMinutes",
        "genres"
    )

    @Bean
    fun basicsReader(): FlatFileItemReader<BasicsRecord> {
        val mapper = BeanWrapperFieldSetMapper<BasicsRecord>()
        mapper.setTargetType(BasicsRecord::class.java)

        return FlatFileItemReaderBuilder<BasicsRecord>()
            .name("basicsRecordsItemReader")
            .resource(ClassPathResource("data.txt"))
            .linesToSkip(1)
            .delimited()
            .delimiter("\t")
            .quoteCharacter('★')
            .names(*basicsHeaders)
            .fieldSetMapper(mapper)
            .build()
    }

    @Bean
    fun basicsRecordItemProcessor(): BasicsRecordItemProcessor {
        return BasicsRecordItemProcessor()
    }

    @Bean
    fun basicsWriter(dataSource: DataSource): JdbcBatchItemWriter<BasicsEntity> {
        return JdbcBatchItemWriterBuilder<BasicsEntity>()
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql(
                "INSERT INTO basics (title_id, title_type, primary_title, original_title, is_adult, start_year,end_year, runtime_minutes, genres) " +
                        "VALUES(:titleId, :titleType, :primaryTitle, :originalTitle, :isAdult, :startYear, :endYear, :runtimeMinutes, :genres)")
            .dataSource(dataSource)
            .build()
    }

    @Bean
    fun importUserJob(listener: JobCompletionNotificationListener, step1: Step): Job? {
        return jobBuilderFactory["importUserJob"]
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build()
    }

    @Bean
    fun step1(writer: JdbcBatchItemWriter<BasicsEntity>): Step {
        return stepBuilderFactory["step1"]
            .chunk<BasicsRecord, BasicsEntity>(10000)
            .reader(basicsReader())
            .processor(basicsRecordItemProcessor())
            .writer(writer)
            .build()
    }
}