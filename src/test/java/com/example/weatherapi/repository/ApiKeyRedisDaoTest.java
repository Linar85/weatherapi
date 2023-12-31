package com.example.weatherapi.repository;

import com.example.weatherapi.entity.ApiKey;
import com.example.weatherapi.utils.DataUpdater;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.test.StepVerifier;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class ApiKeyRedisDaoTest {

    @Autowired
    private ApiKeyRedisDao apiKeyRedisDao;
    @Autowired
    private ReactiveRedisOperations<String, ApiKey> redisOperations;
    @MockBean
    DataUpdater dataUpdater;
    @Container
    private static final RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getFirstMappedPort().toString());
    }

    ApiKey apiKey = ApiKey.builder()
            .id(1L)
            .apiKey("qwerty")
            .userId(2L)
            .build();
    String akey = "apikey#" + apiKey.getUserId() + "#" + apiKey.getApiKey();

    @Test
    void save() {

        StepVerifier.create(apiKeyRedisDao.save(apiKey)
                        .thenMany(redisOperations.opsForValue().get(akey)))
                .expectNextMatches(key -> key.getApiKey().equals(apiKey.getApiKey()))
                .verifyComplete();
    }

    @Test
    void findByKey() {

        StepVerifier.create(redisOperations.opsForValue().set(akey, apiKey)
                        .thenMany(apiKeyRedisDao.findByKey("qwerty")))
                .expectNext(apiKey)
                .verifyComplete();
    }

    @Test
    void findByWrongKey() {

        StepVerifier.create(redisOperations.opsForValue().set(akey, apiKey)
                        .thenMany(apiKeyRedisDao.findByKey("another_key")))
                .verifyComplete();
    }
}