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
                    user.setApiKey(ApiKey.builder()
                            .apiKey(UUID.randomUUID().toString())
                            .userId(customPrincipal.getId())
                            .created(LocalDateTime.now())
                            .build());
                    return Mono.just(user.getApiKey());
                }).flatMap(apiKey -> {
                    rateLimiterDao.findByUserId(apiKey.getUserId())
                            .flatMap(rateLimiter -> {
                                apiKey.setRateLimiter(rateLimiter);
                                apiKeyDao.save(apiKey).subscribe();
                                apiKeyRedisDao.save(apiKey).subscribe();
                                rateLimiterRedisDao.save(apiKey, rateLimiter).subscribe();
                                return Mono.when();
                            }).subscribe();
                    return Mono.just(apiKey);
                });
    }
}