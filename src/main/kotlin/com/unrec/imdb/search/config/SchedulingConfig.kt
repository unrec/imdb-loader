package com.unrec.imdb.search.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@Profile("!test")
@Configuration
class SchedulingConfig