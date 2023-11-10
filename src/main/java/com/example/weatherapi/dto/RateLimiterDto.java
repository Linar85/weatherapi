package com.example.weatherapi.dto;

import lombok.Data;

@Data
public class RateLimiterDto {
    private Long id;
    private Integer rateLimit;
    private Integer duration;
    private Long apiKeyId;
    private Integer limitByDuration;
}