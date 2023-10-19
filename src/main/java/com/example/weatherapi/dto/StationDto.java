package com.example.weatherapi.dto;

import com.example.weatherapi.entity.Weather;
import lombok.Data;

@Data
public class StationDto {
    private Long id;
    private String name;
    private String country;
}