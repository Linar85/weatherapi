package com.example.weatherapi.repository;

import com.example.weatherapi.entity.Weather;
import com.redis.testcontainers.RedisContainer;
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

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class WeatherRedisDaoTest {

    @Autowired
    private WeatherRedisDao weatherRedisDao;

    @Autowired
    private ReactiveRedisOperations<String, Weather> redisOperations;

    @Container
    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
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