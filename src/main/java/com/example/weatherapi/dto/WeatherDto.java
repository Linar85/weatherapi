package com.example.weatherapi.dto;

import com.example.weatherapi.entity.Station;
import lombok.Data;

@Data
public class WeatherDto {
    private Long id;
    private double temp_c;
    private int wind_kph;
    private String wind_dir;
    private String cloud_okt;
    private String cloud_type;
    private String conditions_text;
    private int condition_code;
    private Station station;
}