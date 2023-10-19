package com.example.weatherapi.dto;

import com.example.weatherapi.entity.ApiKey;
import lombok.Data;

@Data
public class RateLimitsDto {
    private Long id;
    private int rateLimit;
    private ApiKey apiKey;
}