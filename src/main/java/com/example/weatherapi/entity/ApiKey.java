package com.example.weatherapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
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
    @Column("api_key")
    private String apiKey;
    @Column("created")
    private LocalDate created;
    @Column("updated")
    private LocalDate updated;
    @Transient
    private User user;
}