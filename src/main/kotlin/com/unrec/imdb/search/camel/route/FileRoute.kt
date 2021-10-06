package com.unrec.imdb.search.camel.route

import org.apache.camel.LoggingLevel.ERROR
import org.apache.camel.builder.RouteBuilder

abstract class FileRoute : RouteBuilder() {
    override fun configure() {
        errorHandler(
            defaultErrorHandler()
                .loggingLevel(ERROR)
                .log("SOME ERROR OCCURRED!")
        )
        configureRoute()
    }

    abstract fun configureRoute()
}