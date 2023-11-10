package com.example.weatherapi.config;

import com.example.weatherapi.entity.Weather;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@TestConfiguration
public class RedisTestConfiguration {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean(name = "weatherTest")
    public ReactiveRedisTemplate<String, Weather> reactiveJsonPersonRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory) {

        var serializer = new Jackson2JsonRedisSerializer<Weather>(Weather.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, Weather> builder = RedisSerializationContext
                .newSerializationContext(new StringRedisSerializer());

        var serializationContext = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

    @Bean
    public ReactiveRedisTemplate<String, Object> reactiveJsonObjectRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory) {

        RedisSerializationContext.RedisSerializationContextBuilder<String, Object> builder = RedisSerializationContext
                .newSerializationContext(new StringRedisSerializer());

        var serializationContext = builder
                .value(new GenericJackson2JsonRedisSerializer("_type")).build();

        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

    public @PreDestroy
    void flushTestDb() {
        redisConnectionFactory().getConnection().flushDb();
    }
}
