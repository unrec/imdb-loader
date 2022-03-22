package com.unrec.imdb.loader.config.batch

import com.unrec.imdb.loader.batch.GZipBufferedReaderFactory
import com.unrec.imdb.loader.batch.JobCompletionNotificationListener
import com.unrec.imdb.loader.entity.Entity
import com.unrec.imdb.loader.record.Record
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import java.lang.reflect.ParameterizedType
import java.nio.file.Path
import javax.sql.DataSource

abstract class StructuredJob<R : Record, E : Entity>(
    private val zipName: String,
    private val headers: Array<String>,
    private val insertQuery: String,
    private val chunkSize: Int,
    val batchSettings: BatchSettings
) {

    @Autowired
    private lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    private lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    private lateinit var listener: JobCompletionNotificationListener

    @Autowired
    private lateinit var gZipBufferedReaderFactory: GZipBufferedReaderFactory

    @Autowired
    private lateinit var dataSource: DataSource

    @Value("\${imdb.source}")
    private lateinit var sourcePath: String

    fun itemReader(): ItemReader<R> {
        val mapper = BeanWrapperFieldSetMapper<R>()
        mapper.setTargetType(getTypeClass() as Class<out R>)

        return FlatFileItemReaderBuilder<R>()
            .name(batchSettings.reader)
            .resource(FileSystemResource(Path.of(sourcePath, zipName)))
            .bufferedReaderFactory(gZipBufferedReaderFactory)
            .linesToSkip(1)
            .delimited()
            .delimiter("\t")
            .quoteCharacter('☀')
            .names(*headers)
            .fieldSetMapper(mapper)
            .build()
    }

    fun itemProcessor(): ItemProcessor<R, E> {
        return ItemProcessor {
            it.toEntity() as E
        }
    }

    fun itemWriter(): ItemWriter<E> {
        val itemWriter = JdbcBatchItemWriterBuilder<E>()
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql(insertQuery)
            .dataSource(dataSource)
            .build()
        itemWriter.afterPropertiesSet()
        return itemWriter
    }

    fun step(): Step {
        return stepBuilderFactory[batchSettings.step]
            .chunk<R, E>(chunkSize)
            .reader(itemReader())
            .processor(itemProcessor())
            .writer(itemWriter())
            .build()
    }

    fun job(): Job {
        return jobBuilderFactory[batchSettings.job]
            .incrementer(RunIdIncrementer())
            .listener(listener)
            .flow(step())
            .end()
            .build()
    }

    private fun getTypeClass(): Class<out Record> {
        val actualTypeArguments = (this.javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments
        return actualTypeArguments[0] as Class<out Record>
    }
}