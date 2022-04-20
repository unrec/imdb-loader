package com.unrec.imdb.loader

import com.unrec.imdb.loader.entity.BasicsEntity
import com.unrec.imdb.loader.entity.RatingsEntity
import com.unrec.imdb.loader.record.BasicsRecord
import com.unrec.imdb.loader.record.RatingsRecord

object TestDataFactory {

    val basicsTestString =
        """
        tconst	titleType	primaryTitle	originalTitle	isAdult	startYear	endYear	runtimeMinutes	genres
        tt0000001	short	Carmencita	Carmencita	0	1894	\N	1	Documentary,Short
        tt0000002	short	Le clown et ses chiens	Le clown et ses chiens	0	1892	\N	5	Animation,Short
        tt0000003	short	Pauvre Pierrot	Pauvre Pierrot	0	1892	\N	4	Animation,Comedy,Romance
        tt0000004	short	Un bon bock	Un bon bock	0	1892	\N	12	Animation,Short
        tt0000005	short	Blacksmith Scene	Blacksmith Scene	0	1893	\N	1	Comedy,Short
        tt0000006	short	Chinese Opium Den	Chinese Opium Den	0	1894	\N	1	Short
        tt0000007	short	Corbett and Courtney Before the Kinetograph	Corbett and Courtney Before the Kinetograph	0	1894	\N	1	Short,Sport
        tt0000008	short	Edison Kinetoscopic Record of a Sneeze	Edison Kinetoscopic Record of a Sneeze	0	1894	\N	1	Documentary,Short
        tt0000009	short	Miss Jerry	Miss Jerry	0	1894	\N	40	Romance,Short
        tt0000010	short	Leaving the Factory	La sortie de l'usine Lumière à Lyon	0	1895	\N	1	Documentary,Short
    """.trimIndent()

    val wrongBasicsTestString =
        """
        tconst	titleType	primaryTitle	originalTitle	isAdult	startYear	endYear	runtimeMinutes	genres
        tt0000001	Carmencita	Carmencita	0	1894	\N	1	Documentary,Short
        tt0000002	short	Le clown et ses chiens	Le clown et ses chiens	0	1892	\N	5	Animation,Short
    """.trimIndent()


    fun testBasicsRecord() = BasicsRecord(
        tconst = "tt0110912",
        titleType = "movie",
        primaryTitle = "Pulp Fiction",
        originalTitle = "Pulp Fiction",
        isAdult = "0",
        startYear = "1994",
        endYear = "\\N",
        runtimeMinutes = "154",
        genres = "Crime, Drama"
    )

    fun testBasicsEntity() = BasicsEntity(
        titleId = 110912,
        titleType = "movie",
        primaryTitle = "Pulp Fiction",
        originalTitle = "Pulp Fiction",
        isAdult = false,
        startYear = 1994,
        endYear = null,
        runtimeMinutes = 154,
        genres = "Crime, Drama"
    )

    fun testRatingsRecord() = RatingsRecord(
        tconst = "tt0110912",
        averageRating = "8.9",
        numVotes = "1946783"
    )

    fun testRatingsEntity() = RatingsEntity(
        titleId = 110912,
        averageRating = 8.9F,
        numVotes = 1946783
    )
}
