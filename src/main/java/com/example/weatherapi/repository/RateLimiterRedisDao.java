package com.example.weatherapi.repository;

import com.example.weatherapi.entity.ApiKey;
import com.example.weatherapi.entity.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RateLimiterRedisDao {

    private final ReactiveRedisOperations<String, RateLimiter> redisTemplate;

    public Mono<Void> save(ApiKey apiKey, RateLimiter rateLimiter) {
        String key = "ratelimiter#" + apiKey.getUserId() + "#" + apiKey.getApiKey();
        this.redisTemplate.keys("ratelimiter#" + apiKey.getUserId() +"*").flatMap(redisTemplate::delete).subscribe();
        return this.redisTemplate.opsForValue().set(key, rateLimiter).then();
    }

    public Mono<RateLimiter> findByApiKey(String apiKey) {
        return redisTemplate.keys("ratelimiter*" + apiKey + "*")
                .flatMap(key -> this.redisTemplate.opsForValue().get(key)).next();
    }
}