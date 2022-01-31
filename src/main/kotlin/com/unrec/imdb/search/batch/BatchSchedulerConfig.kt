package com.unrec.imdb.search.batch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.util.*

@EnableScheduling
@Profile("!test", "batch")
@ConditionalOnProperty(value = ["batch.scheduler.enabled"], havingValue = "true")
@Configuration
class BatchSchedulerConfig(private val launcher: JobLauncher, val jobList: List<Job>) {

    @Scheduled(cron = "\${batch.scheduler.cron}")
    fun perform() {
        println("Job started at :" + Date())
        val param = JobParametersBuilder().addString("JobID", System.currentTimeMillis().toString())
            .toJobParameters()
        val execution: JobExecution = launcher.run(jobList[0], param)
        println("Job finished with status :" + execution.status)
    }
}