package com.example.weatherapi.repository;

import com.example.weatherapi.entity.ApiKey;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ApiKeyDao extends R2dbcRepository<ApiKey, Long> {

    @Query("select * from weather_api.weather group by user_id having ")
    Mono<ApiKey> findByApiKey(String key);
}
