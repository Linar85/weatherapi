package com.example.weatherapi.repository;

import com.example.weatherapi.entity.Weather;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import redis.embedded.RedisServer;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WeatherRedisDaoTest {

    @Autowired
    private WeatherRedisDao weatherRedisDao;

    @Autowired
    private ReactiveRedisOperations<String, Weather> redisOperations;
    RedisServer redisServer = new RedisServer(6378);

    @BeforeAll
    public void startUpRedisServer() {
        redisServer.start();
    }

    @AfterAll
    public void shutDownRedisServer() {
        redisServer.stop();
    }

    @Test
    void saveWeather() {
        Weather weather = Weather.builder()
                .id(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .build();

        StepVerifier.create(weatherRedisDao.saveWeather(weather))
                .verifyComplete();
    }

    @Test
    void findByStationCode() {
        Weather weather = Weather.builder()
                .id(UUID.randomUUID().toString())
                .stationCode("testCode")
                .createdAt(LocalDateTime.now())
                .build();
        Flux<Weather> resultGet = redisOperations.opsForValue().set("testCode", weather).thenMany(weatherRedisDao.findByStationCode("testCode"));

        StepVerifier.create(resultGet)
                .expectNextMatches(x -> x.getStationCode().equals(weather.getStationCode())
                        && x.getId().equals(weather.getId())
                        && x.getCreatedAt().equals(weather.getCreatedAt()))
                .verifyComplete();
    }

    @Test
    void findByWrongStationCode() {
        Weather weather = Weather.builder()
                .id(UUID.randomUUID().toString())
                .stationCode("testCode")
                .createdAt(LocalDateTime.now())
                .build();
        Flux<Weather> resultGet = redisOperations.opsForValue().set("testCode", weather).thenMany(weatherRedisDao.findByStationCode("wrongCode"));

        StepVerifier.create(resultGet)
                .expectNextCount(0L)
                .verifyComplete();
    }
}