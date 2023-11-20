package com.example.weatherapi.repository;

import com.example.weatherapi.entity.Station;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class StationRedisDaoTest {

    @Autowired
    private StationRedisDao stationRedisDao;

    @Autowired
    private ReactiveRedisOperations<String, Station> redisOperations;
    @Container
    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);
    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }
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