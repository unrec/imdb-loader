package com.unrec.imdb.search.repository

import com.unrec.imdb.search.TestDataFactory
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.samePropertyValuesAs
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = DEFINED_PORT)
class BasicsEntityRepositoryTest(@Autowired val repository: BasicsEntityRepository) {

    @Test
    fun `Save test entity`() {

        //given
        val entity = TestDataFactory.testBasicsEntity()

        //when
        val savedEntity = repository.save(entity)

        //then
        assertThat(entity, samePropertyValuesAs(savedEntity))
    }
}
