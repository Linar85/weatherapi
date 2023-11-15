package com.example.weatherapi.repository;

import com.example.weatherapi.entity.ApiKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.io.Serializable;

@Repository
@RequiredArgsConstructor
public class ApiKeyRedisDao implements Serializable {

    @Autowired
    private final ReactiveRedisOperations<String, ApiKey> redisTemplate;

    public Mono<Void> save(ApiKey apiKey) {
        String key = "apikey#" + apiKey.getUserId() + "#" + apiKey.getApiKey();
        redisTemplate.keys("apikey#" + apiKey.getUserId() + "*")
                .flatMap(redisTemplate::delete).subscribe();
        return this.redisTemplate.opsForValue().set(key, apiKey).then();
    }

    public Mono<ApiKey> findByUserId(Long userId) {
        return redisTemplate.keys("*" + userId + "*")
                .flatMap(key -> this.redisTemplate.opsForValue().get(key)).next();
    }

    public Mono<ApiKey> findByKey(String apiKey) {
        return redisTemplate.keys("apikey*" + apiKey + "*")
                .flatMap(key -> this.redisTemplate.opsForValue().get(key)).next();
    }
}
