# imdb-search-service

### Apache Camel vs. Spring Batch

Initially Apache Camel was selected as core framework for ETL process due to its simplicity and flexibility. When it started to process huge amount of data some problems occurred due to usage of JPA. Even using batch saving it takes a lot of time to load IMDb datasets in SQL Database.

The next option was Spring Batch due to ability to use native SQL queries for saving records and batching in its core. 

Here are statistics for processing `basics` dataset. As you can see Spring Batch is about two time faster. Probably we can speed up JPA saving process by migrating to `JdbcTemplate`, but in this case we need to reinvent batch processing for Camel. As per docs Camel itself has support for Spring Batch, but I'm not sure it works well and that it will boost performance so dramatically.

**Whole `basics` dataset - Spring Batch**
````
2021-10-04 15:00:36.411  INFO 11756 --- [           main] .i.s.b.JobCompletionNotificationListener : JOB FINISHED! @ Mon Oct 04 15:00:36 MSK 2021
2021-10-04 15:00:36.426  INFO 11756 --- [           main] .i.s.b.JobCompletionNotificationListener : started @ Mon Oct 04 14:43:33 MSK 2021, finished @ Mon Oct 04 15:00:36 MSK 2021 }
2021-10-04 15:00:36.426  INFO 11756 --- [           main] .i.s.b.JobCompletionNotificationListener : job duration = 1023 seconds
````
**3 million of records - Spring Batch**
````
2021-10-05 15:33:13.717  INFO 7760 --- [           main] .i.s.b.JobCompletionNotificationListener : JOB FINISHED! @ Tue Oct 05 15:33:13 MSK 2021
2021-10-05 15:33:13.725  INFO 7760 --- [           main] .i.s.b.JobCompletionNotificationListener : started @ Tue Oct 05 15:28:43 MSK 2021, finished @ Tue Oct 05 15:33:13 MSK 2021 }
2021-10-05 15:33:13.726  INFO 7760 --- [           main] .i.s.b.JobCompletionNotificationListener : job duration = 269 seconds
````
**3 million of records - Apache Camel with JPA batching**
````
021-10-05 16:16:02.031 DEBUG 19388 --- [e://C:/imdb/zip] c.u.imdb.search.camel.route.BasicsRoute  : Route started @ 2021-10-05 16:16:02+0300
2021-10-05 16:16:02.040  INFO 19388 --- [e://C:/imdb/zip] c.u.i.s.camel.processor.GZipProcessor    : Starting to unzip file: C:\imdb\zip\3m_title.basics.tsv.gz
2021-10-05 16:16:04.043  INFO 19388 --- [e://C:/imdb/zip] c.u.i.s.camel.processor.GZipProcessor    : Unzipping C:\imdb\zip\3m_title.basics.tsv.gz took 1998 ms
2021-10-05 16:16:04.044  INFO 19388 --- [e://C:/imdb/zip] c.u.i.s.c.p.BasicsRecordsLoadProcessor   : Starting to parse file: C:\imdb\zip\3m_title.basics.txt
2021-10-05 16:16:37.980  INFO 19388 --- [e://C:/imdb/zip] c.u.i.s.c.p.BasicsRecordsLoadProcessor   : Parsing C:\imdb\zip\3m_title.basics.txt took 33532 ms
2021-10-05 16:16:38.084  INFO 19388 --- [e://C:/imdb/zip] c.u.i.s.c.p.BasicsRecordsLoadProcessor   : Total records: 3000000
2021-10-05 16:16:38.091  INFO 19388 --- [e://C:/imdb/zip] c.u.i.s.c.p.BasicsRecordsLoadProcessor   : Starting to map records from C:\imdb\zip\3m_title.basics.txt
2021-10-05 16:16:47.913  INFO 19388 --- [e://C:/imdb/zip] c.u.i.s.c.p.BasicsRecordsLoadProcessor   : Mapping records from C:\imdb\zip\3m_title.basics.txt took 9781 ms
2021-10-05 16:16:47.925  INFO 19388 --- [e://C:/imdb/zip] c.u.i.s.c.p.BasicsRecordsLoadProcessor   : Total entities: 3000000
2021-10-05 16:16:47.927  INFO 19388 --- [e://C:/imdb/zip] c.u.i.s.c.p.BasicsRecordsLoadProcessor   : Starting to load entities from C:\imdb\zip\3m_title.basics.txt
2021-10-05 16:16:48.055 DEBUG 19388 --- [e://C:/imdb/zip] o.s.orm.jpa.JpaTransactionManager        : Creating new transaction with name [org.springframework.data.jpa.repository.support.SimpleJpaRepository.saveAll]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
2021-10-05 16:16:48.066 DEBUG 19388 --- [e://C:/imdb/zip] o.s.orm.jpa.JpaTransactionManager        : Opened new EntityManager [SessionImpl(827344486<open>)] for JPA transaction
2021-10-05 16:16:48.099 DEBUG 19388 --- [e://C:/imdb/zip] o.s.orm.jpa.JpaTransactionManager        : Exposing JPA transaction as JDBC [org.springframework.orm.jpa.vendor.HibernateJpaDialect$HibernateConnectionHandle@d593d35]
2021-10-05 16:24:37.505 DEBUG 19388 --- [e://C:/imdb/zip] o.s.orm.jpa.JpaTransactionManager        : Initiating transaction commit
2021-10-05 16:24:37.505 DEBUG 19388 --- [e://C:/imdb/zip] o.s.orm.jpa.JpaTransactionManager        : Committing JPA transaction on EntityManager [SessionImpl(827344486<open>)]
2021-10-05 16:26:10.130 DEBUG 19388 --- [e://C:/imdb/zip] o.s.orm.jpa.JpaTransactionManager        : Closing JPA EntityManager [SessionImpl(827344486<open>)] after transaction
2021-10-05 16:26:10.138  INFO 19388 --- [e://C:/imdb/zip] i.StatisticalLoggingSessionEventListener : Session Metrics {
9305800 nanoseconds spent acquiring 1 JDBC connections;
0 nanoseconds spent releasing 0 JDBC connections;
4175827200 nanoseconds spent preparing 3000001 JDBC statements;
330530045100 nanoseconds spent executing 3000000 JDBC statements;
70922382000 nanoseconds spent executing 100000 JDBC batches;
0 nanoseconds spent performing 0 L2C puts;
0 nanoseconds spent performing 0 L2C hits;
0 nanoseconds spent performing 0 L2C misses;
92545803200 nanoseconds spent executing 1 flushes (flushing a total of 3000000 entities and 0 collections);
0 nanoseconds spent executing 0 partial-flushes (flushing a total of 0 entities and 0 collections)
}
2021-10-05 16:26:10.408  INFO 19388 --- [e://C:/imdb/zip] c.u.i.s.c.p.BasicsRecordsLoadProcessor   : Loading entities to Database took 562473 ms
2021-10-05 16:26:10.423 DEBUG 19388 --- [e://C:/imdb/zip] c.u.imdb.search.camel.route.BasicsRoute  : Route finished @ 2021-10-05 16:26:10+0300
````