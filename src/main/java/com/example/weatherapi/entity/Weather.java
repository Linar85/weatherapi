package com.example.weatherapi.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "weather", schema = "weather_api")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
public class Weather implements Serializable {
    @Id
    private String id;
    @Column("temp_c")
    private Integer tempC;
    @Column("wind_kph")
    private Integer windKph;
    @Column("wind_dir")
    private String windDir;
    @Column("cloud_okt")
    private Integer cloudOkt;
    @Column("cloud_type")
    private String cloudType;
    @Column("conditions_text")
    private String conditionsText;
    @Column("condition_code")
    private Integer conditionCode;
    @Column("station_code")
    private String stationCode;
    @Column("created_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    @Transient
    @ToString.Exclude
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Station station;
}