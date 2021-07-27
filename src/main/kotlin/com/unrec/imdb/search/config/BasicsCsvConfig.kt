package com.unrec.imdb.search.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BasicsCsvConfig {

    @Bean
    fun basicsMapper(): ObjectMapper {
        return CsvMapper().registerModule(KotlinModule())
    }

    @Bean
    fun basicsSchema(): CsvSchema {
        return CsvSchema.builder()
            .addColumn("tconst")
            .addColumn("titleType")
            .addColumn("primaryTitle")
            .addColumn("originalTitle")
            .addColumn("isAdult")
            .addColumn("startYear")
            .addColumn("endYear")
            .addColumn("runtimeMinutes")
            .addColumn("genres")
            .setColumnSeparator('\t')
            .setSkipFirstDataRow(true)
            .setLineSeparator(System.lineSeparator())
            .disableQuoteChar()
            .build()
    }
}
