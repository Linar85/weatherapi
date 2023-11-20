package com.example.weatherapi.service;

import com.example.weatherapi.entity.RateLimiter;
import com.example.weatherapi.entity.User;
import com.example.weatherapi.repository.ApiKeyDao;
import com.example.weatherapi.repository.ApiKeyRedisDao;
import com.example.weatherapi.repository.RateLimiterDao;
import com.example.weatherapi.repository.RateLimiterRedisDao;
import com.example.weatherapi.security.CustomPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class ApiKeyServiceTest {

    @InjectMocks
    ApiKeyService apiKeyService;
    @Mock
    UserService userService;
    @Mock
    ApiKeyDao apiKeyDao;
    @Mock
    ApiKeyRedisDao apiKeyRedisDao;
    @Mock
    RateLimiterRedisDao rateLimiterRedisDao;
    @Mock
    RateLimiterDao rateLimiterDao;
    @Mock
    Authentication authentication;


    @Test
    void apiKeyGenerate() {

        CustomPrincipal customPrincipal = new CustomPrincipal();
        customPrincipal.setName("user");
        customPrincipal.setId(1L);
        User user = User.builder()
                .id(1L)
                .username("user")
                .build();

        Mockito.when(authentication.getPrincipal()).thenReturn(customPrincipal);
        Mockito.when(userService.getUserByUsername(anyString())).thenReturn(Mono.just(user));
        Mockito.when(rateLimiterDao.findByUserId(any())).thenReturn(Mono.just(new RateLimiter()));
        Mockito.when(apiKeyDao.save(any())).thenReturn(Mono.empty());
        Mockito.when(apiKeyRedisDao.save(any())).thenReturn(Mono.empty());
        Mockito.when(rateLimiterRedisDao.save(any(), any())).thenReturn(Mono.empty());

        StepVerifier.create(apiKeyService.apiKeyGenerate(authentication))
                .consumeNextWith(apiKey -> {
                    Assertions.assertEquals(apiKey.getUserId(), user.getId());
                })
                .verifyComplete();

    }
}