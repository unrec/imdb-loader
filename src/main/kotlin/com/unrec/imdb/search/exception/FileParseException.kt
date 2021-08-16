package com.unrec.imdb.search.exception

class FileParseException: RuntimeException {
    constructor(message: String?): super(message)
    constructor(message: String?, cause: Throwable) : super(message, cause)
}