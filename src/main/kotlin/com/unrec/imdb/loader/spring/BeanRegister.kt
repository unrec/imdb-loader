package com.unrec.imdb.loader.spring

import com.unrec.imdb.loader.config.batch.StructuredJob
import org.springframework.batch.core.Job
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.stereotype.Component

@Component
class BeanRegister(beanFactory: BeanFactory) {

    val configurableBeanFactory = beanFactory as ConfigurableBeanFactory

    fun registerJob(structuredJob: StructuredJob<*, *>): Job? {
        registerReader(structuredJob)
        registerProcessor(structuredJob)
        registerWriter(structuredJob)
        registerStep(structuredJob)
        return registerInnerJob(structuredJob)
    }

    private fun registerReader(structuredJob: StructuredJob<*, *>) {
        val jobReaderName: String = structuredJob.batchSettings.reader
        val reader: ItemReader<*> = structuredJob.itemReader()
        configurableBeanFactory.registerSingleton(jobReaderName, reader)
    }

    private fun registerProcessor(structuredJob: StructuredJob<*, *>) {
        val jobProcessorName: String = structuredJob.batchSettings.processor
        val processor: ItemProcessor<*, *> = structuredJob.itemProcessor()
        configurableBeanFactory.registerSingleton(jobProcessorName, processor)
    }

    private fun registerWriter(structuredJob: StructuredJob<*, *>) {
        val jobWriterName: String = structuredJob.batchSettings.writer
        val writer: ItemWriter<*> = structuredJob.itemWriter()
        configurableBeanFactory.registerSingleton(jobWriterName, writer)
    }

    private fun registerStep(structuredJob: StructuredJob<*, *>) {
        val jobStepName: String = structuredJob.batchSettings.step
        val step = structuredJob.step()
        configurableBeanFactory.registerSingleton(jobStepName, step)
    }

    private fun registerInnerJob(structuredJob: StructuredJob<*, *>): Job {
        val jobName: String = structuredJob.batchSettings.job
        val job = structuredJob.job()
        configurableBeanFactory.registerSingleton(jobName, job)
        return job
    }
}