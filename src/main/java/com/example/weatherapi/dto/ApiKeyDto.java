package com.example.weatherapi.dto;

import com.example.weatherapi.entity.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApiKeyDto {
    private Long id;
    private String apiKey;
    private User user;
    private LocalDate created;
    private LocalDate updated;
}