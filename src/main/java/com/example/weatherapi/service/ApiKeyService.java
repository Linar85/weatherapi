package com.example.weatherapi.service;

import com.example.weatherapi.entity.ApiKey;
import com.example.weatherapi.repository.ApiKeyDao;
import com.example.weatherapi.repository.ApiKeyRedisDao;
import com.example.weatherapi.repository.RateLimiterDao;
import com.example.weatherapi.repository.RateLimiterRedisDao;
import com.example.weatherapi.security.CustomPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyDao apiKeyDao;
    private final ApiKeyRedisDao apiKeyRedisDao;
    private final UserService userService;
    private final RateLimiterRedisDao rateLimiterRedisDao;
    private final RateLimiterDao rateLimiterDao;

    public Mono<ApiKey> apiKeyGenerate(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserByUsername(customPrincipal.getName())
                .flatMap(user -> {
                    ApiKey apiKey = ApiKey.builder()
                            .apiKey(UUID.randomUUID().toString())
                            .userId(customPrincipal.getId())
                            .created(LocalDateTime.now())
                            .build();
                    user.setApiKey(apiKey);
                    return rateLimiterDao.findByUserId(user.getId())
                            .flatMap(rateLimiter -> {
                                apiKey.setRateLimiter(rateLimiter);
                                return apiKeyDao.save(apiKey)
                                        .then(apiKeyRedisDao.save(apiKey))
                                        .then(rateLimiterRedisDao.save(apiKey, rateLimiter))
                                        .then(Mono.just(apiKey));
                            });
                });
    }
}