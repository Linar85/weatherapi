package com.example.weatherapi.repository;

import com.example.weatherapi.entity.ApiKey;
import com.example.weatherapi.entity.RateLimiter;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface RateLimiterDao extends R2dbcRepository<RateLimiter, Long> {
    Mono<RateLimiter> findByUserId(Long userId);
    @Query("select * from weather_api.ratelimits r join weather_api.apikeys a on r.user_id=a.user_id where a.api_key=?1")
    Mono<RateLimiter> findByKey(String key);
}
