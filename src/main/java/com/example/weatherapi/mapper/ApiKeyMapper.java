package com.example.weatherapi.mapper;

import com.example.weatherapi.dto.ApiKeyDto;
import com.example.weatherapi.entity.ApiKey;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApiKeyMapper {

    ApiKeyDto map(ApiKey entity);

    @InheritInverseConfiguration
    ApiKey map(ApiKeyDto dto);

}
