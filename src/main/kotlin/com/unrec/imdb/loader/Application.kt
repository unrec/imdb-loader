package com.unrec.imdb.loader

import com.unrec.imdb.loader.spring.JobProcessor
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
@EnableBatchProcessing
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Component
class CommandLineAppStartupRunner : CommandLineRunner {

    @Autowired
    private lateinit var jobProcessor: JobProcessor

    override fun run(vararg args: String) {
        jobProcessor.registerJobs()
        jobProcessor.launchJobs()
    }
}