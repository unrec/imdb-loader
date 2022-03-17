package com.unrec.imdb.search.record

import com.unrec.imdb.search.entity.Entity

interface Record {
    fun toEntity(): Entity
}