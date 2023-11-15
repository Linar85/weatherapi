package com.example.weatherapi.repository;

import com.example.weatherapi.entity.Station;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import redis.embedded.RedisServer;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StationRedisDaoTest {

    @Autowired
    private StationRedisDao stationRedisDao;

    @Autowired
    private ReactiveRedisOperations<String, Station> redisOperations;
    RedisServer redisServer = new RedisServer(6378);


    String KEY = "stations";

    Station station1 = Station.builder()
            .id("1")
            .stationCode("KGM")
            .country("RF")
            .build();

    Station station2 = Station.builder()
            .id("2")
            .stationCode("OMG")
            .country("KZ")
            .build();

    @BeforeAll
    public void setUpAll() {
            redisServer.start();
    }

    @AfterAll
    public void tearDown() {
        redisServer.stop();
    }

    @BeforeEach
    void setUp() {
        redisOperations.opsForList().delete(KEY).subscribe();
    }

    @Test
    void save() {
        StepVerifier.create(stationRedisDao.save(station1))
                .verifyComplete();
    }

    @Test
    void findAll() {
        Flux<Station> stations = Flux.just(station1, station2);

        StepVerifier.create(stations
                        .flatMap(st -> redisOperations.opsForList().leftPushAll(KEY, st))
                        .thenMany(stationRedisDao.findAll()))
                .expectNextCount(2L)
                .verifyComplete();
    }

    @Test
    void findByStationCode() {

        Flux<Station> stations = Flux.just(station1, station2);

        StepVerifier.create(stations.flatMap(st -> redisOperations.opsForList().leftPushAll(KEY, st))
                        .thenMany(stationRedisDao.findByStationCode("KGM")))
                .expectNext(station1)
                .verifyComplete();
    }

    @Test
    void findByStationCodeCount() {

        Flux<Station> stations = Flux.just(station1, station2);

        StepVerifier.create(stations.flatMap(st -> redisOperations.opsForList().leftPushAll(KEY, st))
                        .thenMany(stationRedisDao.findByStationCode("KGM")))
                .expectNextCount(1L)
                .verifyComplete();
    }
}