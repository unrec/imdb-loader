package com.unrec.imdb.search.route

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class BasicsRoute : FileRoute() {

    @Value("\${camel.imdb.source}")
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