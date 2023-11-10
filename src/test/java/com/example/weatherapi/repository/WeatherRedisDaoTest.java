package com.example.weatherapi.repository;

import com.example.weatherapi.config.RedisTestConfiguration;
import com.example.weatherapi.entity.Weather;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RedisTestConfiguration.class)
class WeatherRedisDaoTest {

    @Autowired
    private WeatherRedisDao weatherRedisDao;

    @Autowired
    @Qualifier("weatherTest")
    private ReactiveRedisTemplate<String, Weather> redisTemplate;

    @Test
    void saveWeather() {
        Weather weather = Weather.builder()
                .id(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .build();
        Mono<Void> result = weatherRedisDao.saveWeather(any(String.class), any(String.class), weather);
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void findByStationCode() {
        Weather weather = Weather.builder()
                .id(UUID.randomUUID().toString())
                .stationCode("UFA")
                .createdAt(LocalDateTime.now())
                .build();
        Flux<Weather> resultGet = redisTemplate.opsForValue().set("weather", weather).thenMany(weatherRedisDao.findByStationCode(any(String.class)));

        StepVerifier.create(resultGet)
                .expectNextMatches(x -> x.getStationCode().equals(weather.getStationCode())
                        && x.getId().equals(weather.getId())
                        && x.getCreatedAt().equals(weather.getCreatedAt()))
                .verifyComplete();
    }
}