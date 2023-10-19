package com.example.weatherapi.dto;

import com.example.weatherapi.entity.Weather;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationDto {
    private String id;
    private String stationCode;
    private String name;
    private String country;
    private List<Weather> weather;
}