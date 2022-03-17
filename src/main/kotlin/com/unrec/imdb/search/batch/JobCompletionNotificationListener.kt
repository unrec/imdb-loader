package com.unrec.imdb.search.batch

import mu.KotlinLogging
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.listener.JobExecutionListenerSupport
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import kotlin.math.abs

@Profile("batch")
@Component
class JobCompletionNotificationListener : JobExecutionListenerSupport() {

    private val logger = KotlinLogging.logger {}

    override fun beforeJob(jobExecution: JobExecution) {
        logger.info { "JOB '${jobExecution.jobInstance.jobName}' STARTED @ ${jobExecution.startTime}" }
    }

    override fun afterJob(jobExecution: JobExecution) {
        if (jobExecution.status == BatchStatus.COMPLETED) {
            logger.info("JOB '${jobExecution.jobInstance.jobName}' FINISHED @ ${jobExecution.endTime}")
            val diffInMills: Long = abs(jobExecution.endTime.time - jobExecution.startTime.time)
            val diffInSeconds = TimeUnit.SECONDS.convert(diffInMills, TimeUnit.MILLISECONDS)
            logger.info("JOB '${jobExecution.jobInstance.jobName}' DURATION = $diffInSeconds SECONDS")
        }
    }
}