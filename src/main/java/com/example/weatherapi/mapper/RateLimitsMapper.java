package com.example.weatherapi.mapper;

import com.example.weatherapi.dto.RateLimitsDto;
import com.example.weatherapi.entity.RateLimits;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RateLimitsMapper {

    RateLimitsDto map(RateLimits entity);

    @InheritInverseConfiguration
    RateLimits map(RateLimitsDto dto);

}
