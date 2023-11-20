package com.example.weatherapi.repository;

import com.example.weatherapi.entity.ApiKey;
import com.example.weatherapi.entity.RateLimiter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import reactor.test.StepVerifier;
import redis.embedded.RedisServer;

import java.util.Objects;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RateLimiterRedisDaoTest {

    @Autowired
    private RateLimiterRedisDao rateLimiterRedisDao;

    @Autowired
    private ReactiveRedisOperations<String, RateLimiter> redisOperations;
    RedisServer redisServer = new RedisServer(6378);

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

    @BeforeAll
    public void startUpRedisServer() {
        redisServer.start();
    }

    @AfterAll
    public void shutDownRedisServer() {
        redisServer.stop();
    }

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