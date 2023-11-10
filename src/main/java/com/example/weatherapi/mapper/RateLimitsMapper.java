package com.example.weatherapi.mapper;

import com.example.weatherapi.dto.RateLimiterDto;
import com.example.weatherapi.entity.RateLimiter;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RateLimitsMapper {

    RateLimiterDto map(RateLimiter entity);

    @InheritInverseConfiguration
    RateLimiter map(RateLimiterDto dto);

}
