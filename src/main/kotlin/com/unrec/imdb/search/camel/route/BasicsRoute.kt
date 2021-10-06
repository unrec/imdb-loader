package com.unrec.imdb.search.camel.route

import org.apache.camel.LoggingLevel.DEBUG
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("camel")
@Component
class BasicsRoute : FileRoute() {

    @Value("\${imdb.source}")
    private lateinit var source: String

    private val fileName = "title.basics.tsv.gz"
    private val routeId = "BasicsRoute"

    override fun configureRoute() {

        //@formatter:off
        from("file://${source}?noop=true&fileName=${fileName}").id(routeId)
            .log(DEBUG, log, "Route started @ \${date:now:yyyy-MM-dd HH:mm:ssZ}")
            .bean("GZipProcessor", "process")
            .bean("basicsRecordsLoadProcessor", "process")
            .log(DEBUG, log, "Route finished @ \${date:now:yyyy-MM-dd HH:mm:ssZ}")
            .end()
        //@formatter:on
    }
}