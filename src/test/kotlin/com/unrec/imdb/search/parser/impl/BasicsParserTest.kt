package com.unrec.imdb.search.parser.impl

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.unrec.imdb.search.TestDataFactory
import com.unrec.imdb.search.config.BasicsCsvConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [BasicsCsvConfig::class, BasicsParser::class])
class BasicsParserTest(@Autowired val parser: BasicsParser) {

    @Test
    fun removeHeaders() {
        //given
        val input = TestDataFactory.basicsTestString

        //when
        val list = parser.parseRecords(input)

        //then
        assertThat(list.size, equalTo(10))
    }
}