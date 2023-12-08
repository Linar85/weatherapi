package com.example.weatherapi.mapper;

import com.example.weatherapi.dto.StationDto;
import com.example.weatherapi.entity.Station;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StationMapper {

    StationDto map(Station entity);

    @InheritInverseConfiguration
    Station map(StationDto dto);
}
