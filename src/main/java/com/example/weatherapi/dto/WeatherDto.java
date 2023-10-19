package com.example.weatherapi.dto;

import com.example.weatherapi.entity.Station;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherDto {
    private String id;
    private Double tempC;
    private Integer windKph;
    private String windDir;
    private Integer cloudOkt;
    private String cloudType;
    private String conditionsText;
    private Integer conditionCode;
    private String stationCode;
    private Station station;
}