package com.example.weatherapi.entity;

import com.example.weatherapi.dto.WeatherDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Table(name = "stations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Station implements Serializable {
    @Id
    private String id;
    @Column("station_code")
    private String stationCode;
    @Column("name")
    private String name;
    @Column("country")
    private String country;
    @Transient
    @ToString.Exclude
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private WeatherDto weather;
}