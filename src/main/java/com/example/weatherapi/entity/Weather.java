package com.example.weatherapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.relational.core.mapping.Table;

@Table("weather")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@RedisHash("weather")
public class Weather {
    @Id
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