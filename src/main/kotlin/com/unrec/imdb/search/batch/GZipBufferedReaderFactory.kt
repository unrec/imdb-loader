package com.unrec.imdb.search.batch

import org.springframework.batch.item.file.BufferedReaderFactory
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream

@Component
class GZipBufferedReaderFactory : BufferedReaderFactory {

    var extensions = listOf(".gz", ".gzip")

    override fun create(resource: Resource, encoding: String): BufferedReader {
        return if (extensions.any {
                resource.filename!!.endsWith(it) || resource.description.endsWith(it)
            }) BufferedReader(
            InputStreamReader(GZIPInputStream(resource.inputStream), encoding)
        ) else BufferedReader(InputStreamReader(resource.inputStream, encoding))
    }
}