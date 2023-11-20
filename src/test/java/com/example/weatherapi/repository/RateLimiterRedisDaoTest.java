package com.example.weatherapi.repository;

import com.example.weatherapi.entity.ApiKey;
import com.example.weatherapi.entity.RateLimiter;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

import java.util.Objects;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class RateLimiterRedisDaoTest {

    @Autowired
    private RateLimiterRedisDao rateLimiterRedisDao;

    @Autowired
    private ReactiveRedisOperations<String, RateLimiter> redisOperations;

    @Container
    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
    }
    RateLimiter rateLimiter = RateLimiter.builder()
            .userId(1L)
            .bucketCapacity(2)
            .refillGreedyTokens(2)
            .refillGreedyDurationSeconds(2)
            .build();

    ApiKey apiKey = ApiKey.builder()
            .id(1L)
            .apiKey("qwerty")
            .userId(2L)
            .build();

    String key = "ratelimiter#" + apiKey.getUserId() + "#" + apiKey.getApiKey();

    @Test
    void save() {
        StepVerifier.create(rateLimiterRedisDao.save(apiKey, rateLimiter))
                .verifyComplete();
    }

    @Test
    void findByApiKey() {
        StepVerifier.create(redisOperations.opsForValue().set(key, rateLimiter)
                        .thenMany(rateLimiterRedisDao.findByApiKey(apiKey.getApiKey())))
                .expectNextMatches(k -> Objects.equals(k.getUserId(), rateLimiter.getUserId())
                        && Objects.equals(k.getBucketCapacity(), rateLimiter.getBucketCapacity())
                        && Objects.equals(k.getRefillGreedyTokens(), rateLimiter.getRefillGreedyTokens())
                        && Objects.equals(k.getRefillGreedyDurationSeconds(), rateLimiter.getRefillGreedyDurationSeconds()))
                .verifyComplete();
    }

    @Test
    void findByWrongKey() {
        StepVerifier.create(redisOperations.opsForValue().set(key, rateLimiter)
                        .thenMany(rateLimiterRedisDao.findByApiKey("non-existent")))
                .expectNextCount(0L)
                .verifyComplete();
    }
}