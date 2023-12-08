package com.example.weatherapi.repository;

import com.example.weatherapi.entity.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;

@Repository
@RequiredArgsConstructor
public class WeatherRedisDao implements Serializable {

    private final ReactiveRedisOperations<String, Weather> redisTemplate;

    public Mono<Void> saveWeather(Weather weather) {
        String key = "weather#" + weather.getStationCode() + "#" + weather.getCreatedAt().format(DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH"));
        return this.redisTemplate.opsForValue().set(key, weather).then();
    }

    public Flux<Weather> findByStationCode(String stationCode) {
        return this.redisTemplate.keys("*" + stationCode + "*")
                .flatMap(key -> this.redisTemplate.opsForValue().get(key));
    }

    public Flux<Long> evict() {
        return this.redisTemplate.keys("*weather*").flatMap(redisTemplate::delete);
    }
}
