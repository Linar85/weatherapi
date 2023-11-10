package com.example.weatherapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "ratelimits", schema = "weather_api")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class RateLimiter {
    @Id
    private Long id;
    @Column("bucket_capacity")
    private Integer bucketCapacity;
    @Column("refill_greedy_tokens")
    private Integer refillGreedyTokens;
    @Column("refill_greedy_duration_seconds")
    private Integer refillGreedyDurationSeconds;
    @Column("user_id")
    private Long userId;

}