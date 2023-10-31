package com.example.weatherapi.repository;

import com.example.weatherapi.entity.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

@Repository
@RequiredArgsConstructor
public class WeatherRedisDao implements Serializable {

    private final String KEY = "weathers";

    private final ReactiveRedisOperations<String, Weather> redisTemplate;

    public Flux<Long> save(Weather weather) {
        return this.redisTemplate.opsForList().leftPush(KEY, weather).flux();
    }

    public Flux<Weather> findAll() {
        return this.redisTemplate.opsForList().range(KEY, 0, -1);
    }

    public Flux<Weather> findByStationCode(String stationCode) {
        return this.redisTemplate.opsForList().range(KEY, 0, -1)
                .filter(st -> st.getStationCode().equals(stationCode));
    }

    public Mono<Boolean> evict() {
        return this.redisTemplate.opsForList().delete(KEY);
    }
}
