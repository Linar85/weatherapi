package com.example.weatherapi.mapper;

import com.example.weatherapi.dto.WeatherDto;
import com.example.weatherapi.entity.Weather;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WeatherMapper {

    WeatherDto map(Weather entity);

    @InheritInverseConfiguration
    Weather map(WeatherDto dto);

}
