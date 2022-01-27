package com.unrec.imdb.search.mapper

import com.unrec.imdb.search.TestDataFactory.testBasicsEntity
import com.unrec.imdb.search.TestDataFactory.testBasicsRecord
import com.unrec.imdb.search.TestDataFactory.testRatingsEntity
import com.unrec.imdb.search.TestDataFactory.testRatingsRecord
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
