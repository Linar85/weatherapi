package com.example.weatherapi.dto;

import com.example.weatherapi.entity.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ApiKeyDto {
    private Long id;
    private String apiKey;
    private LocalDateTime created;
    private Long userId;
    private User user;

}