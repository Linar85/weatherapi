package com.example.weatherapi.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUpdater {

   private ReactiveRedisOperations template;

   public void update(){

   }

}
