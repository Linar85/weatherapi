package com.example.weatherapi.repository;

import com.example.weatherapi.entity.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StationRedisDao implements Serializable {

    private final String KEY = "stations";

    private final ReactiveRedisOperations<String, Station> redisTemplate;

    public Mono<Void> save(Station station) {
        return this.redisTemplate.opsForList().leftPush(KEY, station).then();
    }

    public Flux<Station> findAll() {
        return this.redisTemplate.opsForList().range(KEY, 0, -1);
    }

    public Mono<Station> findByStationCode(String stationCode) {
        return this.redisTemplate.opsForList().range(KEY, 0, -1)
                .filter(station -> station.getStationCode().equals(stationCode)).next();
    }

    public Mono<Boolean> evict(){
        return this.redisTemplate.opsForList().delete(KEY);
    }
}