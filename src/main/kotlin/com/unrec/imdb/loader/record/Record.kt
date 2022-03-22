package com.unrec.imdb.loader.record

import com.unrec.imdb.loader.entity.Entity

interface Record {
    fun toEntity(): Entity
}