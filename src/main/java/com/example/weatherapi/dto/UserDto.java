package com.example.weatherapi.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public class UserDto {
    private Long id;
    private String password;
    private LocalDate created;
}