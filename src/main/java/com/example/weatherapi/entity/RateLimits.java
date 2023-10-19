package com.example.weatherapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("ratelimits")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RateLimits {
    @Id
    private Long id;
    private int rateLimit;
    private ApiKey apiKey;
}