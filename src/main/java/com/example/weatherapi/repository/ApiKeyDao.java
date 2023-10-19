package com.example.weatherapi.repository;

import com.example.weatherapi.entity.ApiKey;
import com.example.weatherapi.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface ApiKeyDao extends R2dbcRepository<ApiKey, User> {

    ApiKey findByUser(User user);

}
