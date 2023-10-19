package com.example.weatherapi.repository;

import com.example.weatherapi.entity.RateLimits;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface RateLimitDao extends R2dbcRepository<RateLimits, Long> {

}
