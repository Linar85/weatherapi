package com.example.weatherapi.repository;

import com.example.weatherapi.entity.ApiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

@Repository
@RequiredArgsConstructor
public class ApiKeyRedisDao implements Serializable {

    private final String KEY = "apikeys";

//    @Autowired
//    private final ReactiveRedisOperations<String, ApiKey> redisTemplate;
//
//    public Mono<Long> save(ApiKey apiKey) {
//        return redisTemplate.opsForList().rightPush(KEY, apiKey);
//    }
//
//    public Flux<ApiKey> findAll() {
//        return redisTemplate.opsForList().range(KEY, 0, -1);
//    }
}
