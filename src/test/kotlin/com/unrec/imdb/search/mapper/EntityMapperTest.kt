package com.unrec.imdb.search.mapper

import com.unrec.imdb.search.TestDataFactory.testBasicsEntity
import com.unrec.imdb.search.TestDataFactory.testBasicsRecord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EntityMapperTest {

    @Test
    fun `Map Basics Record to Entity`() {
        assertEquals(testBasicsEntity(), testBasicsRecord().toEntity())
        val expected = testBasicsEntity()
        val actual = testBasicsRecord().toEntity()
        assertEquals(expected, actual)
    }
}
