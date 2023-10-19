package com.example.weatherapi.repository;

import com.example.weatherapi.entity.Station;
import com.example.weatherapi.entity.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WeatherDao {

    private final ReactiveRedisOperations<String, Weather> reactiveRedisOperations;

    public Flux<Weather> findAll() {
        return reactiveRedisOperations.opsForList().range("weather", 0, -1);
    }

    public Mono<Weather> findByStation(Station station) {
        return findAll().filter(p -> p.getStation().equals(station)).last();
    }

    public Mono<List<Station>> getStations() {
        return findAll().map(Weather::getStation).collectList();
    }

    //нету методов для сохранения из реляционной БД

}
