package com.unrec.imdb.search.repository

import com.unrec.imdb.search.entity.BasicsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BasicsEntityRepository : JpaRepository<BasicsEntity, Long>