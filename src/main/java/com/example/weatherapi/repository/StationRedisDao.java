package com.example.weatherapi.repository;

import com.example.weatherapi.entity.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

@Repository
@RequiredArgsConstructor
public class StationRedisDao implements Serializable {

    private final String KEY = "stations";

    @Autowired
    private final ReactiveRedisOperations<String, Station> redisTemplate;
//    private final ReactiveValueOperations<String, Station> reactiveValueOps = redisTemplate.opsForValue();

    public Mono<Long> save(Station station) {
        return redisTemplate.opsForList().rightPush(KEY, station);
    }

    public Flux<Station> findAll() {
        return redisTemplate.opsForList().range(KEY, 0, -1);
    }
}
