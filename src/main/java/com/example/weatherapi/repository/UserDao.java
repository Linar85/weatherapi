package com.example.weatherapi.repository;

import com.example.weatherapi.entity.ApiKey;
import com.example.weatherapi.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserDao extends R2dbcRepository<User, Long> {

    Mono<User> findByUsername(String username);
    Mono<User> findByApiKey(String apiKey);

}
