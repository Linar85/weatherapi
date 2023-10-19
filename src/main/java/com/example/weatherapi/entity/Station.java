package com.example.weatherapi.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.List;

@Table(name = "stations", schema = "weather_api")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Station implements Serializable {
    @Column("id")
    private String id;
    @Id
    @Column("station_code")
    private String stationCode;
    @Column("name")
    private String name;
    @Column("country")
    private String country;
    @Transient
    @ToString.Exclude
    private List<Weather> weathers;
}