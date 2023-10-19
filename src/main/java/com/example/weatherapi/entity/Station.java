package com.example.weatherapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.relational.core.mapping.Table;

@Table("stations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("Station")
public class Station {
    @Id
    private Long id;
    private String name;
    private String country;
}