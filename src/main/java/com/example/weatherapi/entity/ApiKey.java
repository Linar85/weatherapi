package com.example.weatherapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("apikeys")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ApiKey {
    @Id
    private Long id;
    private String apiKey;
    private LocalDate created;
    private LocalDate updated;
    private User user;
}