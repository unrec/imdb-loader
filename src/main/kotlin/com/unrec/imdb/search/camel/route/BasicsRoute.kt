package com.unrec.imdb.search.camel.route

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
            .bean("GZipProcessor", "process")
            .bean("basicsRecordsLoadProcessor", "process")
            .end()
        //@formatter:on
    }
}