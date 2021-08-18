package com.unrec.imdb.search.route

import com.unrec.imdb.search.parser.impl.BasicsParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class BasicsRoute(private val parser: BasicsParser) : FileRoute() {

    @Value("\${camel.imdb.source}")
    private lateinit var source: String

    private val fileName = "title.basics.tsv.gz"

    override fun configureRoute() {

        //@formatter:off
        from("file://${source}?noop=true&fileName=${fileName}")
            .bean("GZipProcessor", "process")
            .end()
        //@formatter:on
    }
}