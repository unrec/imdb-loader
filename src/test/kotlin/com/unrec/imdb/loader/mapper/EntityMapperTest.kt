package com.unrec.imdb.loader.mapper

import com.unrec.imdb.loader.TestDataFactory.testBasicsEntity
import com.unrec.imdb.loader.TestDataFactory.testBasicsRecord
import com.unrec.imdb.loader.TestDataFactory.testRatingsEntity
import com.unrec.imdb.loader.TestDataFactory.testRatingsRecord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EntityMapperTest {

    @Test
    fun `Map Basics Record to Entity`() {
        assertEquals(testBasicsEntity(), testBasicsRecord().toEntity())
    }

    @Test
    fun `Map Ratings Record to Entity`() {
        assertEquals(testRatingsEntity(), testRatingsRecord().toEntity())
    }
}
