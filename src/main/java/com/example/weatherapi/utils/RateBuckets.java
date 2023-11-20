package com.example.weatherapi.utils;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RateBuckets {

    public Map<String, Bucket> usersLimits = new ConcurrentHashMap<>();

    public Bucket getBucket(Integer capacity, Integer refillGreedyTokens, Integer refillGreedyDuration) {

        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(capacity)
                        .refillGreedy(refillGreedyTokens, Duration.ofSeconds(refillGreedyDuration))
                        .build())
                .build();
    }

    public Bucket getDefaultBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(1)
                        .refillGreedy(1, Duration.ofSeconds(3))
                        .build())
                .build();
    }
}