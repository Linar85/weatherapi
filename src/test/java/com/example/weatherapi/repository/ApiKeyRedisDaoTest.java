package com.example.weatherapi.repository;

import com.example.weatherapi.entity.ApiKey;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import reactor.test.StepVerifier;
import redis.embedded.RedisServer;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiKeyRedisDaoTest {

    @Autowired
    private ApiKeyRedisDao apiKeyRedisDao;

    @Autowired
    private ReactiveRedisOperations<String, ApiKey> redisOperations;
    RedisServer redisServer = new RedisServer(6378);

    @BeforeAll
    public void setUp() {
        redisServer.start();
    }

    @AfterAll
    public void tearDown() {
        redisServer.stop();
    }

    @BeforeEach
    public void evictRedis(){

    }

    @Test
    void save() {
        ApiKey apiKey = ApiKey.builder()
                .id(1L)
                .apiKey("qwerty")
                .userId(2L)
                .build();

        String akey = "apikey#" + apiKey.getUserId() + "#" + apiKey.getApiKey();

        StepVerifier.create(apiKeyRedisDao.save(apiKey)
                        .thenMany(redisOperations.opsForValue().get(akey)))
                .expectNextMatches(key -> key.getApiKey().equals(apiKey.getApiKey()))
                .verifyComplete();
    }

    @Test
    void findByUserId() {
    }

    @Test
    void findByKey() {
        ApiKey apiKey = ApiKey.builder()
                .id(1L)
                .apiKey("qwerty")
                .userId(2L)
                .build();

        String akey = "apikey#" + apiKey.getUserId() + "#" + apiKey.getApiKey();

        StepVerifier.create(redisOperations.opsForValue().set(akey, apiKey)
                .thenMany(apiKeyRedisDao.findByKey("qwerty")))
                .expectNext(apiKey)
                .verifyComplete();
    }

    @Test
    void findByWrongKey() {
        ApiKey apiKey = ApiKey.builder()
                .id(1L)
                .apiKey("qwerty")
                .userId(2L)
                .build();

        String akey = "apikey#" + apiKey.getUserId() + "#" + apiKey.getApiKey();

        StepVerifier.create(redisOperations.opsForValue().set(akey, apiKey)
                        .thenMany(apiKeyRedisDao.findByKey("another_key")))
                .verifyComplete();
    }
}