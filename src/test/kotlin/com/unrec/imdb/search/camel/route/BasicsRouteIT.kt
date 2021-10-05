package com.unrec.imdb.search.camel.route

import com.unrec.imdb.search.Application
import com.unrec.imdb.search.repository.BasicsEntityRepository
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.apache.camel.CamelContext
import org.apache.camel.builder.AdviceWith.adviceWith
import org.apache.camel.test.spring.junit5.CamelSpringBootTest
import org.apache.camel.test.spring.junit5.UseAdviceWith
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode
import org.springframework.test.context.ActiveProfiles
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@UseAdviceWith
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@CamelSpringBootTest
@AutoConfigureEmbeddedDatabase
@ActiveProfiles("test", "camel")
@SpringBootTest(
    classes = [Application::class],
    properties = ["camel.springboot.java-routes-include-pattern=**/BasicsRoute*"]
)
class BasicsRouteIT {

    @Autowired
    private lateinit var repository: BasicsEntityRepository

    @Autowired
    private lateinit var camelContext: CamelContext

    private val routeId = "BasicsRoute"

    @Test
    internal fun loadRecords() {

        //given
        val fileName = "10k_title.basics.tsv.gz"
        val source = ClassLoader.getSystemResource("data")
        val path = Paths.get(source.toURI())
        val testFrom = "file:$path?noop=true&fileName=$fileName"

        adviceWith(camelContext, routeId) { routeBuilder -> routeBuilder.replaceFromWith(testFrom) }

        //when
        camelContext.start()
        await()
            .pollDelay(20, TimeUnit.SECONDS)
            .atMost(30, TimeUnit.SECONDS)
            .until { true }

        //then
        assertEquals(10000, repository.count())
    }
}