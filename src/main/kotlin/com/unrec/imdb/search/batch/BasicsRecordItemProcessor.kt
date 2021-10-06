package com.unrec.imdb.search.batch

import com.unrec.imdb.search.entity.BasicsEntity
import com.unrec.imdb.search.mapper.toEntity
import com.unrec.imdb.search.model.BasicsRecord
import org.springframework.batch.item.ItemProcessor
import org.springframework.context.annotation.Profile

@Profile("batch")
class BasicsRecordItemProcessor : ItemProcessor<BasicsRecord, BasicsEntity> {
    override fun process(record: BasicsRecord): BasicsEntity = record.toEntity()
}