package com.unrec.imdb.search.processor

import com.unrec.imdb.search.parser.impl.BasicsParser
import lombok.RequiredArgsConstructor
import mu.KotlinLogging
import org.apache.camel.Body
import org.apache.camel.Exchange
import org.apache.camel.Headers
import org.springframework.stereotype.Component
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.GZIPInputStream
import kotlin.system.measureTimeMillis

@RequiredArgsConstructor
@Component
class GZipProcessor(private val parser: BasicsParser) {

    private val logger = KotlinLogging.logger {}
    private val gzExtension = ".tsv.gz"
    private val txtExtension = ".txt"

    fun process(
        exchange: Exchange,
        @Headers headers: Map<String, String>,
        @Body input: InputStream
    ) {
        val filePath = headers["CamelFileAbsolutePath"]
        val parentDir = headers["CamelFileParent"]
        val fileName = headers["CamelFileName"]?.substringBefore(gzExtension).plus(txtExtension)

        logger.info { "Starting to unzip file: $filePath" }
        val unzippedFile = Path.of(parentDir, fileName)
        Files.deleteIfExists(unzippedFile)
        val gis = GZIPInputStream(input)
        val timeInMillis = measureTimeMillis { Files.copy(gis, unzippedFile) }
        logger.info { "Unzipping $filePath took $timeInMillis ms" }
        exchange.setProperty("unzippedFile", unzippedFile.toAbsolutePath())
    }
}