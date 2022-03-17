package com.unrec.imdb.search.spring

import com.unrec.imdb.search.config.batch.StructuredJob
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class JobProcessor(
    val jobLauncher: JobLauncher,
    val structuredJobs: List<StructuredJob<*, *>>,
    val beanRegister: BeanRegister,
    var jobsToLaunch: List<Job>? = null
) {

    private val logger = KotlinLogging.logger {}

    fun registerJobs() {
        jobsToLaunch = structuredJobs.stream()
            .map(beanRegister::registerJob)
            .collect(Collectors.toList<Job>())
    }

    fun launchJobs() {
        jobsToLaunch?.forEach(this::launch)
    }

    private fun launch(job: Job) {
        try {
            val params =
                JobParametersBuilder().addString(job.name, System.currentTimeMillis().toString())
                    .toJobParameters()
            jobLauncher.run(job, params)
        } catch (e: Exception) {
            logger.error { e.message }
        }
    }
}