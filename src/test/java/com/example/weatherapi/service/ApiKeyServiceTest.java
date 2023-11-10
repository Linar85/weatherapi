package com.example.weatherapi.service;

import com.example.weatherapi.repository.ApiKeyDao;
import com.example.weatherapi.repository.ApiKeyRedisDao;
import com.example.weatherapi.repository.RateLimiterDao;
import com.example.weatherapi.repository.RateLimiterRedisDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ApiKeyServiceTest {

    @MockBean
    ApiKeyDao apiKeyDao;
    @MockBean
    ApiKeyRedisDao apiKeyRedisDao;
    @MockBean
    UserService userService;
    @MockBean
    RateLimiterRedisDao rateLimiterRedisDao;
    @MockBean
    RateLimiterDao rateLimiterDao;

    @Test
    void apiKeyGenerate() {
    }
}